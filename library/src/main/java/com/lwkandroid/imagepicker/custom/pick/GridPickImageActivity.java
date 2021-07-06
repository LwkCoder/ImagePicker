package com.lwkandroid.imagepicker.custom.pick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.BucketBean;
import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.config.CustomPickImageOptions;
import com.lwkandroid.imagepicker.config.CustomPickImageStyle;
import com.lwkandroid.imagepicker.constants.ImageConstants;
import com.lwkandroid.imagepicker.custom.model.MediaLoaderEngine;
import com.lwkandroid.imagepicker.utils.Utils;
import com.lwkandroid.imagepicker.widget.IpRcvLoadMoreView;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;
import com.lwkandroid.rcvadapter.ui.RcvLoadingView;
import com.lwkandroid.rcvadapter.utils.RcvLinearDecoration;
import com.lwkandroid.widget.ComActionBar;
import com.lwkandroid.widget.StateFrameLayout;

import java.util.List;
import java.util.Objects;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @description: 网格浏览界面
 * @author: LWK
 * @date: 2021/6/15 10:30
 */
public class GridPickImageActivity extends AppCompatActivity implements RcvLoadMoreListener
{
    private CustomPickImageOptions mOptions;

    private View mRootContainer;
    private ComActionBar mActionBar;
    private View mBottomContainer;
    private RecyclerView mRecyclerView;
    private StateFrameLayout mStateFrameLayout;
    private TextView mTvCurrentBucket;
    private TextView mTvDone;
    private CheckBox mCkOriginalFile;

    private final MediaLoaderEngine mMediaLoaderEngine = new MediaLoaderEngine();
    private GridPickAdapter mAdapter;

    private final MutableLiveData<List<BucketBean>> mAllBucketLiveData = new MutableLiveData<>();
    private final MutableLiveData<BucketBean> mCurrentBucketLiveData = new MutableLiveData<>();
    private int mCurrentPageIndex = 1;
    private ActivityResultLauncher<PagerLauncherOptions> mPagerLauncher;
    private ActivityResultLauncher<CustomPickImageOptions> mPreViewLauncher;
    private BottomSheetDialog mBucketListSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_pick_image);

        mOptions = getIntent().getParcelableExtra(ImageConstants.KEY_INTENT_OPTIONS);
        if (mOptions == null)
        {
            setResult(Activity.RESULT_CANCELED);
            Log.w("ImagePicker", "Can not pick image because of a null CustomPickImageOptions!");
            finish();
            return;
        }
        if (mOptions.getStyle() == null)
        {
            mOptions.setStyle(CustomPickImageStyle.dark(this));
        }

        mRootContainer = findViewById(R.id.v_root_container);
        mActionBar = findViewById(R.id.actionBar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mBottomContainer = findViewById(R.id.v_bottom_operation);
        mStateFrameLayout = findViewById(R.id.stateFrameLayout);
        RcvLoadingView loadingView = findViewById(R.id.loadingView);
        mTvCurrentBucket = findViewById(R.id.tv_current_bucket);
        mTvDone = findViewById(R.id.tv_done);
        mCkOriginalFile = findViewById(R.id.ck_original_file);

        mTvDone.setOnClickListener(v -> callSelectedDone());
        loadingView.setColor(mOptions.getStyle().getLoadingColor());

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getHorizontalChildCount()));
        //只有多选模式下才能出现复选框
        mAdapter = new GridPickAdapter(this, null, getListChildSize(),
                mOptions.getStyle().getDoneTextColor(), mOptions.getMaxPickNumber() > 1);
        IpRcvLoadMoreView loadMoreView = new IpRcvLoadMoreView(this);
        loadMoreView.setLoadingColor(mOptions.getStyle().getLoadingColor());
        mAdapter.setLoadMoreLayout(loadMoreView);
        mAdapter.setOnLoadMoreListener(this);
        mAdapter.setOnChildClickListener(R.id.imgContent, (viewId, view, mediaBean, layoutPosition) -> {
            //单选模式下直接返回
            if (mOptions.getMaxPickNumber() == 1)
            {
                PickTempStorage.getInstance().getSelectedMediaLiveData().removeObservers(GridPickImageActivity.this);
                PickTempStorage.getInstance().addMediaData(mediaBean);
                callSelectedDone();
            } else
            {
                PagerLauncherOptions pagerLauncherOptions = new PagerLauncherOptions();
                pagerLauncherOptions.setIndex(layoutPosition);
                pagerLauncherOptions.setBucketBean(mCurrentBucketLiveData.getValue());
                pagerLauncherOptions.setOptions(mOptions);
                mPagerLauncher.launch(pagerLauncherOptions);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        initStyle();
        initData();
        startLoadAllBuckets();
    }

    @Override
    public void onLoadMoreRequest()
    {
        int nextPage = mCurrentPageIndex + 1;
        mMediaLoaderEngine.loadPageImage(this, this, mOptions, mCurrentBucketLiveData.getValue().getBucketId(),
                nextPage, mOptions.getPageLoadSize(), new PickCallBack<List<MediaBean>>()
                {
                    @Override
                    public void onPickSuccess(List<MediaBean> result)
                    {
                        mAdapter.notifyLoadMoreSuccess(result, result != null
                                && result.size() >= mOptions.getPageLoadSize());
                        mCurrentPageIndex = nextPage;
                    }

                    @Override
                    public void onPickFailed(int errorCode, String message)
                    {

                    }
                });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (mRecyclerView != null)
        {
            GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
            layoutManager.setSpanCount(getHorizontalChildCount());
            mRecyclerView.setLayoutManager(layoutManager);
        }
        if (mAdapter != null)
        {
            mAdapter.updateChildSize(getListChildSize());
        }
        if (mBucketListSheetDialog != null && mBucketListSheetDialog.isShowing())
        {
            int orientation = getResources().getConfiguration().orientation;
            mBucketListSheetDialog.getBehavior().setState(
                    orientation == Configuration.ORIENTATION_PORTRAIT ?
                            BottomSheetBehavior.STATE_COLLAPSED :
                            BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        PickTempStorage.getInstance().getSelectedMediaLiveData().removeObservers(this);
        PickTempStorage.getInstance().getOriginFileStateLiveData().removeObservers(this);
        mPagerLauncher.unregister();
    }

    /**
     * 初始化风格配置
     */
    private void initStyle()
    {
        CustomPickImageStyle style = mOptions.getStyle();

        Utils.setStatusBarColor(this, style.getStatusBarColor(), true);
        //智能调节状态栏文字颜色
        Utils.setStatusBarDarkMode(this, !Utils.isDarkColor(style.getStatusBarColor()));
        Utils.setNavigationBarColor(this, style.getNavigationBarColor());

        mRootContainer.setBackgroundColor(style.getRootBackgroundColor());

        mActionBar.setBackgroundColor(style.getActionBarColor());
        Drawable leftBackDrawable = AppCompatResources.getDrawable(this, R.drawable.image_picker_action_bar_arrow);
        leftBackDrawable.setTint(style.getActionBarTextColor());
        mActionBar.setLeftIconDrawable(leftBackDrawable);
        mActionBar.setTitleTextColor(style.getActionBarTextColor());
        mActionBar.setLeftTextColor(style.getActionBarTextColor());
        mActionBar.setRightTextColor01(style.getActionBarTextColor());
        mActionBar.setRightTextColor02(style.getActionBarTextColor());

        mBottomContainer.setBackgroundColor(style.getNavigationBarColor());
        Drawable bucketDrawable = AppCompatResources.getDrawable(this, R.drawable.image_picker_album);
        bucketDrawable.setBounds(0, 0, bucketDrawable.getIntrinsicWidth(), bucketDrawable.getIntrinsicHeight());
        bucketDrawable.setTint(style.getBucketNameTextColor());
        mTvCurrentBucket.setCompoundDrawables(bucketDrawable, null, null, null);
        Drawable[] drawables = mTvCurrentBucket.getCompoundDrawables();
        drawables[0] = bucketDrawable;
        mTvCurrentBucket.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        mTvCurrentBucket.setTextColor(style.getBucketNameTextColor());

        mTvDone.setTextColor(style.getDoneTextColor());
        mCkOriginalFile.setVisibility(mOptions.isShowOriginalFileCheckBox() ? View.VISIBLE : View.GONE);
        mCkOriginalFile.setTextColor(mOptions.getStyle().getCheckWidgetNormalColor());
        mCkOriginalFile.setButtonTintList(Utils.createCheckBoxColorStateList(
                mOptions.getStyle().getCheckWidgetCheckedColor(),
                mOptions.getStyle().getCheckWidgetNormalColor()));
    }

    /**
     * 初始化其他配置
     */
    private void initData()
    {
        //单选模式下不需要显示“完成”按钮
        mTvDone.setVisibility(mOptions.getMaxPickNumber() > 1 ? View.VISIBLE : View.GONE);
        //所有文件夹加载完成后的监听
        mAllBucketLiveData.observe(this, bucketBeans -> {
            if (bucketBeans != null && bucketBeans.size() > 0)
            {
                mCurrentBucketLiveData.postValue(bucketBeans.get(0));
            }
        });
        //当前所选文件夹更改后的监听
        mCurrentBucketLiveData.observe(this, this::updateListAfterBucketChanged);
        //同步临时存储中的最大选择数量
        PickTempStorage.getInstance().setMaxNumber(mOptions.getMaxPickNumber());
        //临时存储的监听
        PickTempStorage.getInstance().getSelectedMediaLiveData().observe(this, mediaList -> {
            if (mediaList == null || mediaList.size() == 0)
            {
                mActionBar.setRightText01(null);
                mActionBar.setRightOnItemClickListener01(null);
                mTvDone.setVisibility(View.GONE);
            } else
            {
                mActionBar.setRightText01(getString(R.string.preview_placeholder, mediaList.size()));
                mActionBar.setRightOnItemClickListener01((viewId, textView, dividerLine) -> {
                    // 预览
                    mPreViewLauncher.launch(mOptions);
                });
                mTvDone.setVisibility(View.VISIBLE);
                mTvDone.setText(getString(R.string.done_placeholder, mediaList.size(), mOptions.getMaxPickNumber()));
            }
        });
        PickTempStorage.getInstance().getOriginFileStateLiveData().observe(this, checked -> mCkOriginalFile.setChecked(checked));
        //“原图”checkbox状态同步
        mCkOriginalFile.setOnCheckedChangeListener((buttonView, isChecked) ->
                PickTempStorage.getInstance().getOriginFileStateLiveData().postValue(isChecked));
        mCkOriginalFile.setChecked(false);
        //切换文件夹的事件
        mTvCurrentBucket.setOnClickListener(v -> showBucketListSheet());
        //注册大图浏览跳转
        mPagerLauncher = registerForActivityResult(new ActivityResultContract<PagerLauncherOptions, Integer>()
        {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, PagerLauncherOptions input)
            {
                Intent intent = new Intent(context, PagerPickImageActivity.class);
                intent.putExtra(ImageConstants.KEY_INTENT_OPTIONS, input.getOptions());
                intent.putExtra(ImageConstants.KEY_INTENT_BUCKET, input.getBucketBean());
                intent.putExtra(ImageConstants.KEY_INTENT_INDEX, input.getIndex());
                return intent;
            }

            @Override
            public Integer parseResult(int resultCode, @Nullable Intent intent)
            {
                return resultCode;
            }
        }, result -> {
            if (result == RESULT_OK)
            {
                //完成
                callSelectedDone();
            } else
            {
                if (mAdapter != null)
                {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        //注册预览图片跳转
        mPreViewLauncher = registerForActivityResult(new ActivityResultContract<CustomPickImageOptions, Integer>()
        {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, CustomPickImageOptions input)
            {
                Intent intent = new Intent(context, PagerPreviewActivity.class);
                intent.putExtra(ImageConstants.KEY_INTENT_OPTIONS, input);
                return intent;
            }

            @Override
            public Integer parseResult(int resultCode, @Nullable Intent intent)
            {
                return resultCode;
            }
        }, result -> {
            if (result == RESULT_OK)
            {
                //完成
                callSelectedDone();
            } else
            {
                if (mAdapter != null)
                {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 扫描所有媒体文件夹
     */
    private void startLoadAllBuckets()
    {
        mStateFrameLayout.switchToLoadingState();

        mMediaLoaderEngine.loadAllBucket(GridPickImageActivity.this, GridPickImageActivity.this, mOptions,
                new PickCallBack<List<BucketBean>>()
                {
                    @Override
                    public void onPickSuccess(List<BucketBean> result)
                    {
                        mStateFrameLayout.switchToContentState();
                        mAllBucketLiveData.postValue(result);
                    }

                    @Override
                    public void onPickFailed(int errorCode, String message)
                    {
                        Toast.makeText(GridPickImageActivity.this, R.string.can_not_scan_media_data, Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_CANCELED);
                        finish();
                    }
                });
    }

    /**
     * 切换文件夹
     *
     * @param bucketBean
     */
    private void updateListAfterBucketChanged(BucketBean bucketBean)
    {
        mTvCurrentBucket.setText(bucketBean.getName());
        mAdapter.enableLoadMore(false);
        mMediaLoaderEngine.loadPageImage(this, this, mOptions, bucketBean.getBucketId(),
                1, mOptions.getPageLoadSize(), new PickCallBack<List<MediaBean>>()
                {
                    @Override
                    public void onPickSuccess(List<MediaBean> result)
                    {
                        mCurrentPageIndex = 1;
                        mAdapter.refreshDatas(result);
                        mAdapter.enableLoadMore(result != null && result.size() >= mOptions.getPageLoadSize());
                        mRecyclerView.scrollToPosition(0);
                    }

                    @Override
                    public void onPickFailed(int errorCode, String message)
                    {

                    }
                });
    }

    /**
     * 计算水平方向上图片数量
     */
    private int getHorizontalChildCount()
    {
        int orientation = getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE ? 6 : 4;
    }

    /**
     * 计算每个图片尺寸
     */
    private int getListChildSize()
    {
        return getResources().getDisplayMetrics().widthPixels / getHorizontalChildCount();
    }

    /**
     * 完成选择
     */
    private void callSelectedDone()
    {
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 显示文件夹列表
     */
    private void showBucketListSheet()
    {
        int orientation = getResources().getConfiguration().orientation;
        int windowHeight = getResources().getDisplayMetrics().heightPixels;
        mBucketListSheetDialog = new BottomSheetDialog(this, R.style.ImagePickerSheetStyle);
        View contentView = getLayoutInflater().inflate(R.layout.layout_sheet_bucket_list, null);
        Drawable bgDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_bucket_list_background, getTheme());
        bgDrawable.setTint(mOptions.getStyle().getBucketListBackgroundColor());
        contentView.setBackground(bgDrawable);
        RecyclerView recyclerView = contentView.findViewById(R.id.rcvBucketList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(RcvLinearDecoration.createDefaultVertical(this));
        BucketListAdapter adapter = new BucketListAdapter(this, mAllBucketLiveData.getValue(),
                mOptions.getStyle().getBucketNameTextColor());
        adapter.setOnItemClickListener((holder, bucketBean, position) -> {
            if (!Objects.equals(mCurrentBucketLiveData.getValue(), bucketBean))
            {
                mCurrentBucketLiveData.postValue(bucketBean);
            }
            mBucketListSheetDialog.dismiss();
            mBucketListSheetDialog = null;
        });
        recyclerView.setAdapter(adapter);
        mBucketListSheetDialog.setContentView(contentView);
        mBucketListSheetDialog.getWindow().setWindowAnimations(R.style.ImagePickerSheetAnim);
        mBucketListSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, windowHeight / 3 * 2);
        mBucketListSheetDialog.setCancelable(true);
        mBucketListSheetDialog.setCanceledOnTouchOutside(true);
        mBucketListSheetDialog.getBehavior().setState(
                orientation == Configuration.ORIENTATION_PORTRAIT ?
                        BottomSheetBehavior.STATE_COLLAPSED :
                        BottomSheetBehavior.STATE_EXPANDED);
        mBucketListSheetDialog.show();
    }

    /**
     * 跳转大图浏览界面参数
     */
    private static class PagerLauncherOptions
    {
        private BucketBean bucketBean;
        private int index;
        private CustomPickImageOptions options;

        public BucketBean getBucketBean()
        {
            return bucketBean;
        }

        public void setBucketBean(BucketBean bucketBean)
        {
            this.bucketBean = bucketBean;
        }

        public int getIndex()
        {
            return index;
        }

        public void setIndex(int index)
        {
            this.index = index;
        }

        public CustomPickImageOptions getOptions()
        {
            return options;
        }

        public void setOptions(CustomPickImageOptions options)
        {
            this.options = options;
        }
    }
}
package com.lwkandroid.imagepicker.custom.pick;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.BucketBean;
import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.config.CustomPickImageOptions;
import com.lwkandroid.imagepicker.config.CustomPickImageStyle;
import com.lwkandroid.imagepicker.constants.ImageConstants;
import com.lwkandroid.imagepicker.custom.model.MediaLoaderEngine;
import com.lwkandroid.imagepicker.utils.Utils;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;
import com.lwkandroid.rcvadapter.ui.RcvDefLoadMoreView;
import com.lwkandroid.rcvadapter.ui.RcvLoadingView;
import com.lwkandroid.widget.ComActionBar;
import com.lwkandroid.widget.StateFrameLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 网格图片界面
 *
 * @author LWK
 */
public class GridImageActivity extends AppCompatActivity implements RcvLoadMoreListener
{
    private static final int PAGE_SIZE = 60;

    private CustomPickImageOptions mOptions;

    private LinearLayout mRootLinearLayout;
    private ComActionBar mActionBar;
    private RecyclerView mRecyclerView;
    private LinearLayout mBottomLinearLayout;
    private StateFrameLayout mStateFrameLayout;
    private RcvLoadingView mLoadingView;
    private TextView mTvCurrentBucket;
    private TextView mTvDone;

    private MediaLoaderEngine mMediaLoaderEngine = new MediaLoaderEngine();
    private GridAdapter mAdapter;

    private MutableLiveData<List<BucketBean>> mAllBucketLiveData = new MutableLiveData<>();
    private MutableLiveData<BucketBean> mCurrentBucketLiveData = new MutableLiveData<>();
    private int mCurrentPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_image);

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

        mRootLinearLayout = findViewById(R.id.ll_root_container);
        mActionBar = findViewById(R.id.actionBar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mBottomLinearLayout = findViewById(R.id.ll_bottom_operation);
        mStateFrameLayout = findViewById(R.id.stateFrameLayout);
        mLoadingView = findViewById(R.id.loadingView);
        mTvCurrentBucket = findViewById(R.id.tv_current_bucket);
        mTvDone = findViewById(R.id.tv_done);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getHorizontalChildCount()));
        mAdapter = new GridAdapter(this, null, getListChildSize(), mOptions.getStyle().getDoneTextColor());
        mLoadingView.setColor(mOptions.getStyle().getLoadingColor());
        RcvDefLoadMoreView loadMoreView = new RcvDefLoadMoreView.Builder(this)
                .setTextColor(mOptions.getStyle().getLoadingColor())
                .setTextSize(TypedValue.COMPLEX_UNIT_PX, 0)
                .setFailDrawable(null)
                .setSuccessDrawable(null)
                .build();
        mAdapter.setLoadMoreLayout(loadMoreView);
        mAdapter.setOnLoadMoreListener(this);
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
                nextPage, PAGE_SIZE, new PickCallBack<List<MediaBean>>()
                {
                    @Override
                    public void onPickSuccess(List<MediaBean> result)
                    {
                        mAdapter.notifyLoadMoreSuccess(result, result != null && result.size() >= PAGE_SIZE);
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
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        PickTempStorage.getInstance().removeObservers(this);
        PickTempStorage.getInstance().clear();
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

        mRootLinearLayout.setBackgroundColor(style.getRootBackgroundColor());

        mActionBar.setBackgroundColor(style.getActionBarColor());
        Drawable leftBackDrawable = AppCompatResources.getDrawable(this, R.drawable.image_picker_action_bar_arrow);
        leftBackDrawable.setTint(style.getActionBarTextColor());
        mActionBar.setLeftIconDrawable(leftBackDrawable);
        mActionBar.setTitleTextColor(style.getActionBarTextColor());
        mActionBar.setLeftTextColor(style.getActionBarTextColor());
        mActionBar.setRightTextColor01(style.getActionBarTextColor());
        mActionBar.setRightTextColor02(style.getActionBarTextColor());

        mBottomLinearLayout.setBackgroundColor(style.getNavigationBarColor());
        Drawable bucketDrawable = AppCompatResources.getDrawable(this, R.drawable.image_picker_album);
        bucketDrawable.setBounds(0, 0, bucketDrawable.getIntrinsicWidth(), bucketDrawable.getIntrinsicHeight());
        bucketDrawable.setTint(style.getBucketNameTextColor());
        mTvCurrentBucket.setCompoundDrawables(bucketDrawable, null, null, null);
        Drawable[] drawables = mTvCurrentBucket.getCompoundDrawables();
        drawables[0] = bucketDrawable;
        mTvCurrentBucket.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        mTvCurrentBucket.setTextColor(style.getBucketNameTextColor());
        mTvDone.setTextColor(style.getDoneTextColor());


    }

    /**
     * 初始化其他配置
     */
    private void initData()
    {
        //单选模式下不需要显示“完成”按钮
        mTvDone.setVisibility(mOptions.getMaxPickNumber() > 1 ? View.VISIBLE : View.GONE);

        mAllBucketLiveData.observe(this, bucketBeans -> {
            if (bucketBeans != null && bucketBeans.size() > 0)
            {
                mCurrentBucketLiveData.postValue(bucketBeans.get(0));
            }
        });

        mCurrentBucketLiveData.observe(this, this::updateListAfterBucketChanged);

        PickTempStorage.getInstance().setMaxNumber(mOptions.getMaxPickNumber());
        PickTempStorage.getInstance().addObserver(this, mediaList -> {
            if (mediaList == null || mediaList.size() == 0)
            {
                mActionBar.setRightText01(null);
                mActionBar.setRightOnItemClickListener01(null);
                mTvDone.setVisibility(View.GONE);
            } else
            {
                mActionBar.setRightText01(getString(R.string.preview_placeholder, mediaList.size()));
                mActionBar.setRightOnItemClickListener01(new ComActionBar.OnItemClickListener()
                {
                    @Override
                    public void onActionBarItemClicked(int viewId, TextView textView, View dividerLine)
                    {
                        //TODO 预览
                    }
                });
                mTvDone.setVisibility(View.VISIBLE);
                mTvDone.setText(getString(R.string.done_placeholder, mediaList.size(), mOptions.getMaxPickNumber()));
            }
        });
    }

    /**
     * 扫描所有媒体文件夹
     */
    private void startLoadAllBuckets()
    {
        mStateFrameLayout.switchToLoadingState();

        XXPermissions.with(this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback()
                {
                    @Override
                    public void onGranted(List<String> permissions, boolean all)
                    {
                        mMediaLoaderEngine.loadAllBucket(GridImageActivity.this, GridImageActivity.this, mOptions,
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
                                        Toast.makeText(GridImageActivity.this, R.string.can_not_scan_media_data, Toast.LENGTH_SHORT).show();
                                        setResult(Activity.RESULT_CANCELED);
                                        finish();
                                    }
                                });
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never)
                    {
                        Toast.makeText(GridImageActivity.this, R.string.permission_denied_of_pick_image, Toast.LENGTH_SHORT).show();
                        if (never)
                        {
                            XXPermissions.startPermissionActivity(GridImageActivity.this, permissions);
                        }
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
                1, PAGE_SIZE, new PickCallBack<List<MediaBean>>()
                {
                    @Override
                    public void onPickSuccess(List<MediaBean> result)
                    {
                        mCurrentPageIndex = 1;
                        mAdapter.refreshDatas(result);
                        mAdapter.enableLoadMore(result != null && result.size() >= PAGE_SIZE);
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
}
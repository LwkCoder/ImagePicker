package com.lwkandroid.imagepicker.custom.pick;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.BucketBean;
import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.config.CustomPickImageOptions;
import com.lwkandroid.imagepicker.config.CustomPickImageStyle;
import com.lwkandroid.imagepicker.constants.ImageConstants;
import com.lwkandroid.imagepicker.custom.model.MediaLoaderEngine;
import com.lwkandroid.imagepicker.utils.Utils;
import com.lwkandroid.imagepicker.widget.CheckView;
import com.lwkandroid.widget.ComActionBar;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager2.widget.ViewPager2;

/**
 * @description: 大图浏览界面
 * @author: LWK
 * @date: 2021/6/17 10:30
 */
public class PagerPickImageActivity extends AppCompatActivity implements PagerPickAdapter.IMediaDataSupplier
{
    private BucketBean mCurrentBucket;
    private int mCurrentIndex;
    private CustomPickImageOptions mOptions;

    private ViewPager2 mViewPager;
    private View mRootContainer;
    private ComActionBar mActionBar;
    private View mBottomContainer;
    private TextView mTvDone;
    private CheckBox mCkOriginalFile;
    private View mSelectContainer;
    private CheckView mCvSelect;
    private TextView mTvSelect;

    private PagerPickAdapter mAdapter;
    private final MediaLoaderEngine mMediaLoaderEngine = new MediaLoaderEngine();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_pick_image);

        mCurrentBucket = getIntent().getParcelableExtra(ImageConstants.KEY_INTENT_BUCKET);
        mOptions = getIntent().getParcelableExtra(ImageConstants.KEY_INTENT_OPTIONS);
        mCurrentIndex = getIntent().getIntExtra(ImageConstants.KEY_INTENT_INDEX, 0);

        mRootContainer = findViewById(R.id.v_root_container);
        mActionBar = findViewById(R.id.actionBar);
        mBottomContainer = findViewById(R.id.v_bottom_operation);
        mTvDone = findViewById(R.id.tv_done);
        mCkOriginalFile = findViewById(R.id.ck_original_file);
        mViewPager = findViewById(R.id.viewPager);
        mSelectContainer = findViewById(R.id.v_select_container);
        mCvSelect = findViewById(R.id.cv_select);
        mTvSelect = findViewById(R.id.tv_select);

        mTvDone.setOnClickListener(v -> callSelectedDone());

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position)
            {
                super.onPageSelected(position);
                mCurrentIndex = position;
                mActionBar.setTitleText(getResources().getString(R.string.media_position_placeholder,
                        mCurrentIndex + 1, mCurrentBucket.getFileNumber()));
                //判断是否选中
                mCvSelect.setChecked(PickTempStorage.getInstance().contains(mAdapter.getDatas().get(position)), false);
            }
        });
        mAdapter = new PagerPickAdapter(this, this);
        mViewPager.setAdapter(mAdapter);

        initStyle();
        initData();
    }

    @Override
    public void onMediaDataRequest(int position, PickCallBack<MediaBean> callBack)
    {
        mMediaLoaderEngine.loadPageImage(
                PagerPickImageActivity.this,
                PagerPickImageActivity.this,
                mOptions,
                mCurrentBucket.getBucketId(),
                (int) Math.min(position + 1, mCurrentBucket.getFileNumber()),
                1,
                new PickCallBack<List<MediaBean>>()
                {
                    @Override
                    public void onPickSuccess(List<MediaBean> result)
                    {
                        MediaBean mediaBean = result.get(0);
                        //判断是否选中
                        mCvSelect.setChecked(PickTempStorage.getInstance().contains(mediaBean), false);
                        if (callBack != null)
                        {
                            callBack.onPickSuccess(mediaBean);
                        }
                    }

                    @Override
                    public void onPickFailed(int errorCode, String message)
                    {
                        if (callBack != null)
                        {
                            callBack.onPickFailed(errorCode, message);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        PickTempStorage.getInstance().getSelectedMediaLiveData().removeObservers(this);
        PickTempStorage.getInstance().getOriginFileStateLiveData().removeObservers(this);
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
        mTvDone.setTextColor(style.getDoneTextColor());
        mCkOriginalFile.setVisibility(mOptions.isShowOriginalFileCheckBox() ? View.VISIBLE : View.GONE);
        mCkOriginalFile.setTextColor(mOptions.getStyle().getCheckWidgetNormalColor());
        mCkOriginalFile.setButtonTintList(Utils.createCheckBoxColorStateList(
                mOptions.getStyle().getCheckWidgetCheckedColor(),
                mOptions.getStyle().getCheckWidgetNormalColor()));

        mTvSelect.setTextColor(mOptions.getStyle().getCheckWidgetNormalColor());
        mCvSelect.setBorderColor(mOptions.getStyle().getCheckWidgetNormalColor());
        mCvSelect.setCheckedColor(mOptions.getStyle().getCheckWidgetCheckedColor());
    }

    /**
     * 初始化其他配置
     */
    private void initData()
    {
        //复选框联动
        mCvSelect.setOnCheckedChangeListener((checkView, isChecked) -> {
            if (isChecked)
            {
                PickTempStorage.getInstance().addMediaData(mAdapter.getDatas().get(mCurrentIndex));
            } else
            {
                PickTempStorage.getInstance().removeMediaData(mAdapter.getDatas().get(mCurrentIndex));
            }
        });
        mSelectContainer.setOnClickListener(v -> mCvSelect.setChecked(!mCvSelect.isChecked(), true));
        //TODO 注册跳转
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
                    //TODO 预览
                });
                mTvDone.setVisibility(View.VISIBLE);
                mTvDone.setText(getString(R.string.done_placeholder, mediaList.size(), mOptions.getMaxPickNumber()));
            }
        });
        PickTempStorage.getInstance().getOriginFileStateLiveData().observe(this, checked -> mCkOriginalFile.setChecked(checked));
        //“原图”checkbox状态同步
        mCkOriginalFile.setChecked(PickTempStorage.getInstance().getOriginFileStateLiveData().getValue());
        mCkOriginalFile.setOnCheckedChangeListener((buttonView, isChecked) ->
                PickTempStorage.getInstance().getOriginFileStateLiveData().postValue(isChecked));


        List<MediaBean> list = new LinkedList<>();
        for (int i = 0; i < mCurrentBucket.getFileNumber(); i++)
        {
            list.add(null);
        }
        mAdapter.refreshDatas(list);
        Log.e("AA", "设置容量->" + mAdapter.getItemCount());
        mViewPager.setCurrentItem(mCurrentIndex, false);
    }

    /**
     * 完成选择
     */
    private void callSelectedDone()
    {
        setResult(RESULT_OK);
        finish();
    }
}

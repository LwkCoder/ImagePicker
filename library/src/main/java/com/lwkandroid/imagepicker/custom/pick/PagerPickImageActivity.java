package com.lwkandroid.imagepicker.custom.pick;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.BucketBean;
import com.lwkandroid.imagepicker.config.CustomPickImageOptions;
import com.lwkandroid.imagepicker.config.CustomPickImageStyle;
import com.lwkandroid.imagepicker.constants.ImageConstants;
import com.lwkandroid.imagepicker.utils.Utils;
import com.lwkandroid.widget.ComActionBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager2.widget.ViewPager2;

/**
 * @description: 大图浏览界面
 * @author: LWK
 * @date: 2021/6/17 10:30
 */
public class PagerPickImageActivity extends AppCompatActivity
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

        mTvDone.setOnClickListener(v -> callSelectedDone());

        initStyle();
        initData();
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
        mCkOriginalFile.setTextColor(mOptions.getStyle().getOriginFileCheckBoxTextColor());
        mCkOriginalFile.setButtonTintList(Utils.createCheckBoxColorStateList(
                mOptions.getStyle().getOriginFileCheckBoxButtonTintColor(),
                mOptions.getStyle().getOriginFileCheckBoxTextColor()));
    }

    /**
     * 初始化其他配置
     */
    private void initData()
    {
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
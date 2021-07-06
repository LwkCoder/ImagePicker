package com.lwkandroid.imagepicker.custom.pick;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.config.CustomPickImageOptions;
import com.lwkandroid.imagepicker.config.CustomPickImageStyle;
import com.lwkandroid.imagepicker.constants.ImageConstants;
import com.lwkandroid.imagepicker.utils.Utils;
import com.lwkandroid.imagepicker.widget.CheckView;
import com.lwkandroid.imagepicker.widget.photoview.OnViewTapListener;
import com.lwkandroid.widget.ComActionBar;

import java.util.LinkedList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager2.widget.ViewPager2;

/**
 * @description: 浏览界面
 * @author: LWK
 * @date: 2021年6月29日 14:21:31
 */
public class PagerPreviewActivity extends AppCompatActivity implements OnViewTapListener
{
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

    private PagerPreviewAdapter mAdapter;

    private final List<MediaBean> mOriginSelectedList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_preview);

        mOptions = getIntent().getParcelableExtra(ImageConstants.KEY_INTENT_OPTIONS);

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
                // 更新标题
                mActionBar.setTitleText(getResources().getString(R.string.media_position_placeholder,
                        mCurrentIndex + 1, mAdapter.getItemCount()));
                // 判断是否选中
                updateCurrentSelectedState();
            }
        });

        mAdapter = new PagerPreviewAdapter(this, mOptions.getStyle().getFileSizeTextBackgroundColor(),
                mOptions.getStyle().getFileSizeTextColor(), this);
        mViewPager.setAdapter(mAdapter);

        initStyle();
        initData();
    }

    /**
     * 初始化风格配置
     */
    private void initStyle()
    {
        CustomPickImageStyle style = mOptions.getStyle();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        Utils.setStatusBarColor(this, style.getStatusBarColor(), false);
        Utils.setStatusBarDarkMode(this, Utils.isDarkColor(style.getStatusBarColor()));
        Utils.compatPaddingWithStatusBar(mActionBar);
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
                if (!PickTempStorage.getInstance().addMediaData(mAdapter.getDatas().get(mCurrentIndex)))
                {
                    mCvSelect.setChecked(false, false);
                }
            } else
            {
                PickTempStorage.getInstance().removeMediaData(mAdapter.getDatas().get(mCurrentIndex));
            }
        });
        mSelectContainer.setOnClickListener(v -> mCvSelect.setChecked(!mCvSelect.isChecked()));

        //临时存储的监听
        PickTempStorage.getInstance().getSelectedMediaLiveData().observe(this, mediaList -> {
            if (mediaList == null || mediaList.size() == 0)
            {
                mTvDone.setVisibility(View.GONE);
            } else
            {
                mTvDone.setVisibility(View.VISIBLE);
                mTvDone.setText(getString(R.string.done_placeholder, mediaList.size(), mOptions.getMaxPickNumber()));
            }
        });

        //“原图”checkbox状态同步
        PickTempStorage.getInstance().getOriginFileStateLiveData().observe(this, checked -> mCkOriginalFile.setChecked(checked));
        mCkOriginalFile.setChecked(PickTempStorage.getInstance().getOriginFileStateLiveData().getValue());
        mCkOriginalFile.setOnCheckedChangeListener((buttonView, isChecked) ->
                PickTempStorage.getInstance().getOriginFileStateLiveData().postValue(isChecked));

        //将已选择的数据临时保存一份用于交互
        mOriginSelectedList.addAll(PickTempStorage.getInstance().getSelectedMediaLiveData().getValue());
        mAdapter.refreshDatas(mOriginSelectedList);
        mViewPager.setCurrentItem(0);
    }

    /**
     * 更新当前图片选中状态
     */
    private void updateCurrentSelectedState()
    {
        MediaBean mediaBean = mAdapter.getDatas().get(mCurrentIndex);
        if (mediaBean != null)
        {
            mCvSelect.setChecked(PickTempStorage.getInstance().contains(mediaBean), false);
        }
    }

    /**
     * 完成选择
     */
    private void callSelectedDone()
    {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onViewTap(View view, float x, float y)
    {
        if (mActionBar.getVisibility() == View.VISIBLE)
        {
            mActionBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.image_picker_actionbar_dismiss));
            mBottomContainer.setAnimation(AnimationUtils.loadAnimation(this, R.anim.image_picker_bottom_dismiss));
            mActionBar.setVisibility(View.GONE);
            mBottomContainer.setVisibility(View.GONE);
            Utils.setStatusBarColor(this, Color.TRANSPARENT, false);
        } else
        {
            mActionBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.image_picker_actionbar_show));
            mBottomContainer.setAnimation(AnimationUtils.loadAnimation(this, R.anim.image_picker_bottom_show));
            mActionBar.setVisibility(View.VISIBLE);
            mBottomContainer.setVisibility(View.VISIBLE);
            Utils.setStatusBarColor(this, mOptions.getStyle().getStatusBarColor(), false);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        PickTempStorage.getInstance().getSelectedMediaLiveData().removeObservers(this);
        PickTempStorage.getInstance().getOriginFileStateLiveData().removeObservers(this);
    }


}
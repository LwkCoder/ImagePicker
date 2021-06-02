package com.lwkandroid.imagepicker.ui.pager.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.base.activity.ImagePickerBaseActivity;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImageContants;
import com.lwkandroid.imagepicker.data.ImageDataModel;
import com.lwkandroid.imagepicker.data.ImagePickerOptions;
import com.lwkandroid.imagepicker.ui.pager.adapter.ImagePagerAdapter;
import com.lwkandroid.imagepicker.utils.ImagePickerComUtils;
import com.lwkandroid.imagepicker.widget.ImagePickerActionBar;
import com.lwkandroid.imagepicker.widget.ImagePickerViewPager;

import java.util.ArrayList;

/**
 * 滑动查看图片的基类Activity
 */
public class ImagePagerActivity extends ImagePickerBaseActivity
{
    private ArrayList<ImageBean> mDataList = new ArrayList<>();
    private int mCurPosition;
    private boolean mIsPreview;
    private ImagePickerOptions mOptions;
    private ImagePickerViewPager mViewPager;
    private ImagePickerActionBar mActionBar;
    private View mViewBottom;
    private CheckBox mCkSelected;
    private Button mBtnOk;
    private ImagePagerAdapter mAdapter;

    /**
     * 跳转到该界面的公共方法
     *
     * @param activity      发起跳转的Activity
     * @param dataList      数据List
     * @param startPosition 初始展示的位置
     * @param options       核心参数
     * @param requestCode   请求码
     */
    public static void start(Activity activity, ArrayList<ImageBean> dataList, int startPosition, ImagePickerOptions options, int requestCode)
    {
        Intent intent = new Intent(activity, ImagePagerActivity.class);
        intent.putExtra(ImageContants.INTENT_KEY_START_POSITION, startPosition);
        intent.putExtra(ImageContants.INTENT_KEY_OPTIONS, options);
        intent.putExtra(ImageContants.INTENT_KEY_IS_PREVIEW, requestCode == ImageContants.REQUEST_CODE_PREVIEW);
        ImageDataModel.getInstance().setPagerDataList(dataList);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void beforeSetContentView(Bundle savedInstanceState)
    {
        super.beforeSetContentView(savedInstanceState);
        Intent intent = getIntent();
        mCurPosition = intent.getIntExtra(ImageContants.INTENT_KEY_START_POSITION, 0);
        mIsPreview = intent.getBooleanExtra(ImageContants.INTENT_KEY_IS_PREVIEW, false);
        mDataList.addAll(ImageDataModel.getInstance().getPagerDataList());
        mOptions = intent.getParcelableExtra(ImageContants.INTENT_KEY_OPTIONS);
    }

    @Override
    protected int getContentViewResId()
    {
        return R.layout.activity_image_pager;
    }

    @Override
    protected void initUI(View contentView)
    {
        mActionBar = findView(R.id.acb_image_pager);
        mViewPager = findView(R.id.vp_image_pager);
        mViewBottom = findView(R.id.fl_image_pager_bottom);
        mCkSelected = findView(R.id.ck_image_pager);
        mBtnOk = findView(R.id.btn_image_pager_ok);

        //本身是预览界面就需要关闭预览窗口
        if (mIsPreview)
        {
            mActionBar.hidePreview();
        } else
        {
            mActionBar.showPreview();
            mActionBar.setOnPreviewClickListener(this);
        }
        mBtnOk.setOnClickListener(this);
    }

    @Override
    protected void initData()
    {
        mAdapter = new ImagePagerAdapter(this, mDataList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mViewPager.setCurrentItem(mCurPosition, false);
        mAdapter.setPhotoViewClickListener(new ImagePagerAdapter.PhotoViewClickListener()
        {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1)
            {
                onImageSingleTap();
            }
        });

        updateCheckBoxStatus();
        updateActionbarTitle();
        onSelectNumChanged();
    }

    @Override
    protected void onClick(View v, int id)
    {
        if (id == R.id.tv_imagepicker_actionbar_preview)
        {
            //去预览界面
            ImagePagerActivity.start(this, (ArrayList<ImageBean>) ImageDataModel.getInstance().getResultList()
                    , 0, mOptions, ImageContants.REQUEST_CODE_PREVIEW);

        } else if (id == R.id.btn_image_pager_ok)
        {
            //返回上级界面选择完毕
            setResult(RESULT_OK);
            finish();
        }
    }

    private ViewPager.SimpleOnPageChangeListener mPageChangeListener = new ViewPager.SimpleOnPageChangeListener()
    {
        @Override
        public void onPageSelected(int position)
        {
            super.onPageSelected(position);
            mCurPosition = position;
            if (mDataList != null && position < mDataList.size())
            {
                updateActionbarTitle();
                updateCheckBoxStatus();
            }
        }
    };

    //更新Title
    private void updateActionbarTitle()
    {
        if (mActionBar != null)
        {
            mActionBar.setTitle(getString(R.string.imagepicker_pager_title_count
                    , String.valueOf(mCurPosition + 1), String.valueOf(mDataList.size())));
        }
    }

    //更新当前图片选中状态
    private void updateCheckBoxStatus()
    {
        if (mCkSelected != null)
        {
            mCkSelected.setOnCheckedChangeListener(null);//取消监听，以免冲突
            mCkSelected.setChecked(ImageDataModel.getInstance().hasDataInResult(mDataList.get(mCurPosition)));
            mCkSelected.setOnCheckedChangeListener(mCkChangeListener);
        }
    }

    //更新按钮的文案
    private void onSelectNumChanged()
    {
        int resultNum = ImageDataModel.getInstance().getResultNum();
        mBtnOk.setText(getString(R.string.btn_imagepicker_ok, String.valueOf(resultNum), String.valueOf(mOptions.getMaxNum())));
        if (resultNum == 0)
        {
            mBtnOk.setEnabled(false);
            mActionBar.enablePreview(false);
        } else
        {
            mBtnOk.setEnabled(true);
            mActionBar.enablePreview(true);
        }
    }

    private CompoundButton.OnCheckedChangeListener mCkChangeListener = new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if (isChecked)
            {
                if (ImageDataModel.getInstance().getResultNum() == mOptions.getMaxNum())
                {
                    showShortToast(getString(R.string.warning_imagepicker_max_num, String.valueOf(mOptions.getMaxNum())));
                    mCkSelected.setOnCheckedChangeListener(null);//取消监听，以免冲突
                    mCkSelected.setChecked(false);
                    mCkSelected.setOnCheckedChangeListener(mCkChangeListener);
                } else
                {
                    ImageDataModel.getInstance().addDataToResult(mDataList.get(mCurPosition));
                    onSelectNumChanged();
                }
            } else
            {
                ImageDataModel.getInstance().delDataFromResult(mDataList.get(mCurPosition));
                onSelectNumChanged();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageContants.REQUEST_CODE_PREVIEW)
        {
            if (resultCode == RESULT_OK)
            {
                setResult(RESULT_OK);
                finish();
            } else
            {
                //从预览界面回来需要刷新视图
                updateCheckBoxStatus();
                onSelectNumChanged();
            }
        }
    }

    //根据单击来隐藏/显示头部和底部的布局
    private void onImageSingleTap()
    {
        if (mActionBar == null || mViewBottom == null)
            return;
        if (mActionBar.getVisibility() == View.VISIBLE)
        {
            mActionBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.imagepicker_actionbar_dismiss));
            mViewBottom.setAnimation(AnimationUtils.loadAnimation(this, R.anim.imagepicker_bottom_dismiss));
            mActionBar.setVisibility(View.GONE);
            mViewBottom.setVisibility(View.GONE);
            //更改状态栏为透明
            ImagePickerComUtils.changeStatusBarColor(this, getResources().getColor(R.color.imagepicker_transparent));
            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
            if (Build.VERSION.SDK_INT >= 16)
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else
        {
            mActionBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.imagepicker_actionbar_show));
            mViewBottom.setAnimation(AnimationUtils.loadAnimation(this, R.anim.imagepicker_bottom_show));
            mActionBar.setVisibility(View.VISIBLE);
            mViewBottom.setVisibility(View.VISIBLE);
            //改回状态栏颜色
            ImagePickerComUtils.changeStatusBarColor(this, getResources().getColor(R.color.imagepicker_statusbar));
            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
            if (Build.VERSION.SDK_INT >= 16)
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mDataList.clear();
        ImageDataModel.getInstance().clearPagerDataList();
    }
}

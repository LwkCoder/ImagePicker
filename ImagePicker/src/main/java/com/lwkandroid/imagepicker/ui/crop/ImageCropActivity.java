package com.lwkandroid.imagepicker.ui.crop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.base.activity.ImagePickerBaseActivity;
import com.lwkandroid.imagepicker.data.ImageContants;
import com.lwkandroid.imagepicker.data.ImagePickerOptions;
import com.lwkandroid.imagepicker.widget.crop.CropView;

/**
 * 裁剪界面
 */
public class ImageCropActivity extends ImagePickerBaseActivity
{
    /**
     * 跳转到该界面的公共方法
     *
     * @param activity   发起跳转的Activity
     * @param originPath 待裁剪图片路径
     * @param options    参数
     */
    public static void start(Activity activity, String originPath, ImagePickerOptions options)
    {
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(ImageContants.INTENT_KEY_ORIGIN_PATH, originPath);
        intent.putExtra(ImageContants.INTENT_KEY_OPTIONS, options);
        activity.startActivityForResult(intent, ImageContants.REQUEST_CODE_CROP);
    }

    private ImagePickerOptions mOptions;
    private String mOriginPath;
    private CropView mCropView;
    private Handler mHandler;

    @Override
    protected void beforSetContentView(Bundle savedInstanceState)
    {
        super.beforSetContentView(savedInstanceState);
        Intent intent = getIntent();
        mOriginPath = intent.getStringExtra(ImageContants.INTENT_KEY_ORIGIN_PATH);
        mOptions = intent.getParcelableExtra(ImageContants.INTENT_KEY_OPTIONS);
    }

    @Override
    protected int getContentViewResId()
    {
        mHandler = new Handler(getMainLooper());
        return R.layout.activity_image_crop;
    }

    @Override
    protected void initUI(View contentView)
    {
        mCropView = findView(R.id.cv_crop);
        addClick(R.id.btn_crop_cancel);
        addClick(R.id.btn_crop_confirm);
    }

    @Override
    protected void initData()
    {
        if (mOriginPath == null || mOriginPath.length() == 0)
        {
            showShortToast(R.string.imagepicker_crop_decode_fail);
            finish();
            return;
        }


    }

    @Override
    protected void onClick(View v, int id)
    {
        if (id == R.id.btn_crop_cancel)
        {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.btn_crop_confirm)
        {
            //TODO 返回裁剪数据
        }
    }
}

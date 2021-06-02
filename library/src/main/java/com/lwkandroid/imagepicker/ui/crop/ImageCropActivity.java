package com.lwkandroid.imagepicker.ui.crop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.base.activity.ImagePickerBaseActivity;
import com.lwkandroid.imagepicker.data.ImageContants;
import com.lwkandroid.imagepicker.data.ImagePickerCropParams;
import com.lwkandroid.imagepicker.data.ImagePickerOptions;
import com.lwkandroid.imagepicker.widget.crop.CropUtil;
import com.lwkandroid.imagepicker.widget.crop.CropView;

import java.io.File;

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
    private ProgressDialog mDialog;
    private ImagePickerCropParams mCropParams;

    @Override
    protected void beforeSetContentView(Bundle savedInstanceState)
    {
        super.beforeSetContentView(savedInstanceState);
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
        if (mOptions == null)
        {
            showShortToast(R.string.error_imagepicker_lack_params);
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        if (mOriginPath == null || mOriginPath.length() == 0)
        {
            showShortToast(R.string.imagepicker_crop_decode_fail);
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        File file = new File(mOriginPath);
        if (!file.exists())
        {
            showShortToast(R.string.imagepicker_crop_decode_fail);
            finish();
            return;
        }

        mCropParams = mOptions.getCropParams();
        mCropView.load(mOriginPath)
                .setAspect(mCropParams.getAspectX(), mCropParams.getAspectY())
                .setOutputSize(mCropParams.getOutputX(), mCropParams.getOutputY())
                .start(this);
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
            returnCroppedImage();
        }
    }

    //保存并返回数据
    private void returnCroppedImage()
    {
        showDialog();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Bitmap bitmap = mCropView.getOutput();
                String cachePath = mOptions.getCachePath();
                String name = CropUtil.createCropName();
                String resultPath = CropUtil.saveBmp(bitmap, cachePath, name);
                closeDialog();
                if (TextUtils.isEmpty(resultPath))
                {
                    showShortToast(R.string.imagepicker_crop_save_fail);
                    setResult(RESULT_CANCELED);
                    finish();
                } else
                {
                    //保存成功后返回给上级界面
                    Intent intent = new Intent();
                    intent.putExtra(ImageContants.INTENT_KEY_CROP_PATH, resultPath);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }).start();
    }

    private void showDialog()
    {
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                mDialog = new ProgressDialog(ImageCropActivity.this);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setMessage(getString(R.string.imagepicker_crop_dialog));
                mDialog.show();
            }
        });
    }

    private void closeDialog()
    {
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (mDialog != null && mDialog.isShowing())
                {
                    mDialog.dismiss();
                }
                mDialog = null;
            }
        });
    }
}

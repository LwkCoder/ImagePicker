package com.lwkandroid.imagepicker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import com.lwkandroid.imagepicker.data.ImageContants;
import com.lwkandroid.imagepicker.data.ImageDataModel;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.data.ImagePickerCropParams;
import com.lwkandroid.imagepicker.data.ImagePickerOptions;
import com.lwkandroid.imagepicker.ui.grid.view.ImageDataActivity;
import com.lwkandroid.imagepicker.utils.IImagePickerDisplayer;

/**
 * Created by LWK
 * TODO 调用方法的入口
 */

public class ImagePicker
{
    /**
     * 默认的ResultCode
     */
    public static final int DEF_RESULT_CODE = 136;

    /**
     * 返回结果中包含图片数据的Intent的键值
     */
    public static final String INTENT_RESULT_DATA = "ImageBeans";

    private ImagePickerOptions mOptions;

    private ImagePicker()
    {
    }

    private ImagePicker(ImagePickerOptions options)
    {
        this.mOptions = options;
    }

    /**
     * 发起选择图片
     *
     * @param activity    发起的Activity
     * @param requestCode 请求码
     * @param resultCode  结果码
     */
    public void start(Activity activity, int requestCode, int resultCode)
    {
        Intent intent = new Intent(activity, ImageDataActivity.class);
        intent.putExtra(ImageContants.INTENT_KEY_OPTIONS, mOptions);
        intent.putExtra(ImageContants.INTENT_KEY_RESULTCODE, resultCode);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 发起选择图片
     *
     * @param fragment    发起的Fragment
     * @param requestCode 请求码
     * @param resultCode  结果码
     */
    public void start(Fragment fragment, int requestCode, int resultCode)
    {
        Intent intent = new Intent(fragment.getActivity(), ImageDataActivity.class);
        intent.putExtra(ImageContants.INTENT_KEY_OPTIONS, mOptions);
        intent.putExtra(ImageContants.INTENT_KEY_RESULTCODE, resultCode);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static final class Builder
    {
        private ImagePickerOptions mOptions;

        public Builder()
        {
            mOptions = new ImagePickerOptions();
        }

        public Builder pickType(ImagePickType mode)
        {
            mOptions.setType(mode);
            return this;
        }

        public Builder maxNum(int maxNum)
        {
            mOptions.setMaxNum(maxNum);
            return this;
        }

        public Builder needCamera(boolean b)
        {
            mOptions.setNeedCamera(b);
            return this;
        }

        public Builder cachePath(String path)
        {
            mOptions.setCachePath(path);
            return this;
        }

        public Builder doCrop(int aspectX, int aspectY, int outputX, int outputY)
        {
            mOptions.setNeedCrop(true);
            mOptions.setCropParams(new ImagePickerCropParams(aspectX, aspectY, outputX, outputY));
            return this;
        }

        public Builder displayer(IImagePickerDisplayer displayer)
        {
            ImageDataModel.getInstance().setDisplayer(displayer);
            return this;
        }

        public ImagePicker build()
        {
            return new ImagePicker(mOptions);
        }
    }
}

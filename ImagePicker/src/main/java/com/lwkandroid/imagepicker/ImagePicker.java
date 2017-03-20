package com.lwkandroid.imagepicker;

import android.app.Activity;
import android.content.Intent;

import com.lwkandroid.imagepicker.data.Contants;
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
        intent.putExtra(Contants.INTENT_OPTIONS_KEY, mOptions);
        intent.putExtra(Contants.INTENT_RESULTCODE_KEY, resultCode);
        activity.startActivityForResult(intent, requestCode);
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

        public Builder limitNum(int limitNum)
        {
                mOptions.setLimitNum(limitNum);
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

        public Builder doCrop(ImagePickerCropParams params)
        {
            mOptions.setNeedCrop(true);
            mOptions.setCropParams(params);
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

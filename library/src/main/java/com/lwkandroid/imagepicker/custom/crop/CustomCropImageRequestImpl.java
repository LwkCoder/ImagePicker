package com.lwkandroid.imagepicker.custom.crop;

import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.config.CustomCropImageOptions;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @description: 执行自定义裁剪的实现类
 * @author: LWK
 * @date: 2021/7/6 14:21
 */
public class CustomCropImageRequestImpl implements ICustomCropImageRequest
{
    private CustomCropImageOptions mOptions;

    public CustomCropImageRequestImpl(CustomCropImageOptions options)
    {
        this.mOptions = options;
    }

    @Override
    public void doCrop(AppCompatActivity activity, PickCallBack<File> callBack)
    {
        CustomCropImageFragment.create(activity, mOptions, callBack);
    }

    @Override
    public void doCrop(Fragment fragment, PickCallBack<File> callBack)
    {
        CustomCropImageFragment.create(fragment.getActivity(), mOptions, callBack);
    }
}

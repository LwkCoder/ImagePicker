package com.lwkandroid.imagepicker.system.crop;

import com.lwkandroid.imagepicker.config.SystemCropImageOptions;
import com.lwkandroid.imagepicker.callback.PickCallBack;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @description: 执行系统裁剪的实现类
 * @author:
 * @date: 2021/6/3 9:33
 */
public class SystemCropImageRequestImpl implements ISystemCropImageRequest
{
    private SystemCropImageOptions mOptions;

    public SystemCropImageRequestImpl(SystemCropImageOptions options)
    {
        this.mOptions = options;
    }

    @Override
    public void doCrop(AppCompatActivity activity, PickCallBack<File> callBack)
    {
        SystemCropImageFragment.create(activity, mOptions, callBack);
    }

    @Override
    public void doCrop(Fragment fragment, PickCallBack<File> callBack)
    {
        SystemCropImageFragment.create(fragment.getActivity(), mOptions, callBack);
    }
}

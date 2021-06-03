package com.lwkandroid.library.system.crop;

import com.lwkandroid.library.bean.SystemCropOptions;
import com.lwkandroid.library.callback.PickCallBack;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @description: 执行系统裁剪的实现类
 * @author:
 * @date: 2021/6/3 9:33
 */
public class SystemCropRequestImpl implements ISystemCropRequest
{
    private SystemCropOptions mOptions;

    public SystemCropRequestImpl(SystemCropOptions options)
    {
        this.mOptions = options;
    }

    @Override
    public void doCrop(AppCompatActivity activity, PickCallBack<File> callBack)
    {
        SystemCropFragment.create(activity, mOptions, callBack);
    }

    @Override
    public void doCrop(Fragment fragment, PickCallBack<File> callBack)
    {
        SystemCropFragment.create(fragment.getActivity(), mOptions, callBack);
    }
}

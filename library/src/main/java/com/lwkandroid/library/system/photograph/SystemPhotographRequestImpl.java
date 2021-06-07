package com.lwkandroid.library.system.photograph;

import com.lwkandroid.library.options.SystemPhotographOptions;
import com.lwkandroid.library.callback.PickCallBack;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @description: 系统拍照实现类
 * @author: LWK
 * @date: 2021/6/1 17:24
 */
public class SystemPhotographRequestImpl implements ISystemPhotographRequest
{
    private SystemPhotographOptions mOptions;

    public SystemPhotographRequestImpl(SystemPhotographOptions options)
    {
        this.mOptions = options;
    }

    @Override
    public void doPhotograph(AppCompatActivity activity, PickCallBack<File> callBack)
    {
        SystemPhotographFragment.create(activity, mOptions, callBack);
    }

    @Override
    public void doPhotograph(Fragment fragment, PickCallBack<File> callBack)
    {
        SystemPhotographFragment.create(fragment.getActivity(), mOptions, callBack);
    }
}

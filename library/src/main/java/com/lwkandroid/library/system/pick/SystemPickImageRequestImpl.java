package com.lwkandroid.library.system.pick;

import com.lwkandroid.library.options.SystemPickImageOptions;
import com.lwkandroid.library.callback.PickCallBack;

import java.io.File;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @description: 执行从系统相册选择图片的实现类
 * @author: LWK
 * @date: 2021/6/2 14:13
 */
public class SystemPickImageRequestImpl implements ISystemPickImageRequest
{
    private SystemPickImageOptions mOptions;

    public SystemPickImageRequestImpl(SystemPickImageOptions options)
    {
        this.mOptions = options;
    }

    @Override
    public void doPickImage(AppCompatActivity activity, PickCallBack<List<File>> callBack)
    {
        SystemPickImageFragment.create(activity, mOptions, callBack);
    }

    @Override
    public void doPickImage(Fragment fragment, PickCallBack<List<File>> callBack)
    {
        SystemPickImageFragment.create(fragment.getActivity(), mOptions, callBack);
    }
}

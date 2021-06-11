package com.lwkandroid.imagepicker.custom.pick;

import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.config.CustomPickImageOptions;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @description: 执行从自定义界面选择图片的实现类
 * @author: LWK
 * @date: 2021/6/7 15:17
 */
public class CustomPickImageRequestImpl implements ICustomPickImageRequest
{
    private CustomPickImageOptions mOptions;

    public CustomPickImageRequestImpl(CustomPickImageOptions options)
    {
        this.mOptions = options;
    }

    @Override
    public void doPickImage(AppCompatActivity activity, PickCallBack<List<MediaBean>> callBack)
    {
        CustomPickImageFragment.create(activity, mOptions, callBack);
    }

    @Override
    public void doPickImage(Fragment fragment, PickCallBack<List<MediaBean>> callBack)
    {
        CustomPickImageFragment.create(fragment.getActivity(), mOptions, callBack);
    }
}

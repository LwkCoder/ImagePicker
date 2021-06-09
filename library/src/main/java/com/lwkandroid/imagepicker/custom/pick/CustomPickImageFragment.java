package com.lwkandroid.imagepicker.custom.pick;

import android.os.Bundle;

import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.common.AbsMediatorFragment;
import com.lwkandroid.imagepicker.config.CustomPickImageOptions;

import java.util.List;

import androidx.fragment.app.FragmentActivity;

/**
 * @description: 执行从自定义界面选择图片的Fragment
 * @author: LWK
 * @date: 2021/6/9 13:39
 */
public class CustomPickImageFragment extends AbsMediatorFragment<CustomPickImageOptions, List<MediaBean>>
{
    public CustomPickImageFragment(CustomPickImageOptions options, PickCallBack<List<MediaBean>> callback)
    {
        super(options, callback);
    }

    public static void create(FragmentActivity activity, CustomPickImageOptions options, PickCallBack<List<MediaBean>> callBack)
    {
        CustomPickImageFragment fragment = new CustomPickImageFragment(options, callBack);
        // 设置保留实例，不会因为屏幕方向或配置变化而重新创建
        fragment.setRetainInstance(true);
        fragment.attachActivity(activity);

        Bundle bundle=new Bundle();
    }

    @Override
    protected void doRequest()
    {

    }
}

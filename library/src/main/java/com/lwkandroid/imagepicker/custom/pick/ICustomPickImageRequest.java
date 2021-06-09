package com.lwkandroid.imagepicker.custom.pick;

import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.callback.PickCallBack;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @description: 执行从自定义界面选择图片的接口
 * @author: LWK
 * @date: 2021/6/1 16:22
 */
public interface ICustomPickImageRequest
{
    void doPickImage(AppCompatActivity activity, PickCallBack<List<MediaBean>> callBack);

    void doPickImage(Fragment fragment, PickCallBack<List<MediaBean>> callBack);
}

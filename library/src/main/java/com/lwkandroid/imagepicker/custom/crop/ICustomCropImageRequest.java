package com.lwkandroid.imagepicker.custom.crop;

import com.lwkandroid.imagepicker.callback.PickCallBack;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @description: 执行自定义裁剪的接口
 * @author: LWK
 * @date: 2021/7/6 16:22
 */
public interface ICustomCropImageRequest
{
    void doCrop(AppCompatActivity activity, PickCallBack<File> callBack);

    void doCrop(Fragment fragment, PickCallBack<File> callBack);
}

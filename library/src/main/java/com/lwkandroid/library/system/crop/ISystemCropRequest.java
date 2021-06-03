package com.lwkandroid.library.system.crop;

import com.lwkandroid.library.callback.PickCallBack;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @description: 执行系统裁剪的接口
 * @author: LWK
 * @date: 2021/6/1 16:22
 */
public interface ISystemCropRequest
{
    void doCrop(AppCompatActivity activity, PickCallBack<File> callBack);

    void doCrop(Fragment fragment, PickCallBack<File> callBack);
}

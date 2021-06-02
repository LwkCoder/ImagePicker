package com.lwkandroid.library.system.photograph;

import com.lwkandroid.library.callback.PickCallBack;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @description: 执行系统拍照的接口
 * @author: LWK
 * @date: 2021/6/1 16:22
 */
public interface ISystemPhotographRequest
{
    void doPhotograph(AppCompatActivity activity, PickCallBack<File> callBack);

    void doPhotograph(Fragment fragment, PickCallBack<File> callBack);
}

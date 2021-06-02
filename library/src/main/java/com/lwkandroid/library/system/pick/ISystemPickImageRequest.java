package com.lwkandroid.library.system.pick;

import com.lwkandroid.library.callback.PickCallBack;

import java.io.File;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @description: 执行从系统相册选择图片的接口
 * @author: LWK
 * @date: 2021/6/1 16:22
 */
public interface ISystemPickImageRequest
{
    void doPickImage(AppCompatActivity activity, PickCallBack<List<File>> callBack);

    void doPickImage(Fragment fragment, PickCallBack<List<File>> callBack);
}

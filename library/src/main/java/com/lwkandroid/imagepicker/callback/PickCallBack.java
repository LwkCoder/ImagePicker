package com.lwkandroid.imagepicker.callback;

/**
 * @description: 通用回调
 * @author:
 * @date: 2021/6/1 15:28
 */
public interface PickCallBack<T>
{
    void onPickSuccess(T result);

    void onPickFailed(int errorCode, String message);
}

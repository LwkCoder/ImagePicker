package com.lwkandroid.imagepicker.config;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

/**
 * @description: 图片加载接口
 * @author: LWK
 * @date: 2021/6/9 16:11
 */
public interface ImagePickerDisplayer
{
    /**
     * 显示图片
     *
     * @param context   Context
     * @param url       Url
     * @param imageView ImageView
     */
    void displayImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView);

    /**
     * 显示GIF图片
     *
     * @param context   Context
     * @param url       Url
     * @param imageView ImageView
     */
    void displayGifImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView);
}

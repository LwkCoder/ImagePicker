package com.lwkandroid.imagepicker.config;

import android.content.Context;
import android.widget.ImageView;

import com.lwkandroid.imagepicker.bean.BucketBean;
import com.lwkandroid.imagepicker.bean.MediaBean;

import androidx.annotation.NonNull;

/**
 * @description: 图片加载接口
 * @author: LWK
 * @date: 2021/6/9 16:11
 */
public interface ImagePickerDisPlayer
{
    /**
     * 显示媒体文件数据图片
     *
     * @param context   Context
     * @param mediaBean 媒体文件
     * @param imageView ImageView
     */
    void displayImage(@NonNull Context context, @NonNull MediaBean mediaBean, @NonNull ImageView imageView);

    /**
     * 显示文件夹封面图片
     *
     * @param context    Context
     * @param bucketBean 文件夹
     * @param imageView  ImageView
     */
    void displayImage(@NonNull Context context, @NonNull BucketBean bucketBean, @NonNull ImageView imageView);
}

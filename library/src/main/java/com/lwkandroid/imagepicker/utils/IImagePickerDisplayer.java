package com.lwkandroid.imagepicker.utils;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by LWK
 * TODO 图片加载接口
 */

public interface IImagePickerDisplayer
{
    /**
     *
     * 加载图片
     *
     * @param context     Context
     * @param url         图片地址
     * @param imageView   待加载控件
     * @param maxWidth    最大宽度
     * @param maxHeight   最大高度
     */
    void display(Context context, String url, ImageView imageView, int maxWidth, int maxHeight);

    /**
     * 加载图片
     *
     * @param context     Context
     * @param url         图片地址
     * @param imageView   待加载控件
     * @param placeHolder 加载占位图
     * @param errorHolder 失败占位图
     * @param maxWidth    最大宽度
     * @param maxHeight   最大高度
     */
    void display(Context context, String url, ImageView imageView, int placeHolder, int errorHolder, int maxWidth, int maxHeight);
}

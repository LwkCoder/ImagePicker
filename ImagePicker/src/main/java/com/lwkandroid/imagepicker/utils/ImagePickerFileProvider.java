package com.lwkandroid.imagepicker.utils;

import android.content.Context;
import android.support.v4.content.FileProvider;

/**
 * TODO 自定义FileProvider，解决清单文件合并冲突
 * 参考自博客：http://www.cnblogs.com/liushilin/p/6602364.html
 */

public class ImagePickerFileProvider extends FileProvider
{
    public static String getAuthorities(Context context)
    {
        return new StringBuffer()
                .append(context.getPackageName())
                .append(".provider")
                .toString();
    }
}

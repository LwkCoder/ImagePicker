package com.lwkandroid.imagepicker.utils;

import android.content.Context;

import androidx.core.content.FileProvider;

/**
 * @description:
 * @date: 2020/8/13 16:14
 */
public class ImagePickerFileProvider extends FileProvider
{
    @Override
    public boolean onCreate()
    {
        return super.onCreate();
    }

    public static String getAuthorities(Context context)
    {
        return new StringBuffer()
                .append(context.getPackageName())
                .append(".imagepicker.cache.fileprovider")
                .toString();
    }
}

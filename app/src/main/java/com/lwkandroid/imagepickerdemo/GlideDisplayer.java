package com.lwkandroid.imagepickerdemo;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lwkandroid.imagepicker.config.ImagePickerDisplayer;

import androidx.annotation.NonNull;

/**
 * @description:
 * @author:
 * @date: 2021/6/10 13:47
 */
class GlideDisplayer implements ImagePickerDisplayer
{
    @Override
    public void displayImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView)
    {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(imageView);
    }

    @Override
    public void displayGifImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView)
    {
        Glide.with(context)
                .asGif()
                .load(url)
                .into(imageView);
    }
}

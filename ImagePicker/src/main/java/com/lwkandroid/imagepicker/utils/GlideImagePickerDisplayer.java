package com.lwkandroid.imagepicker.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Glide实现的图片加载
 */
public class GlideImagePickerDisplayer implements IImagePickerDisplayer
{

    @Override
    public void display(Context context, String url, ImageView imageView, int maxWidth, int maxHeight)
    {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .override(maxWidth, maxHeight)
                .into(imageView);
    }

    @Override
    public void display(Context context, String url, ImageView imageView, int placeHolder, int errorHolder, int maxWidth, int maxHeight)
    {

        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(placeHolder)
                .error(errorHolder)
                .override(maxWidth, maxHeight)
                .into(imageView);
    }
}

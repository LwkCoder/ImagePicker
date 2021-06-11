package com.lwkandroid.imagepickerdemo;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lwkandroid.imagepicker.bean.BucketBean;
import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.config.ImagePickerDisPlayer;

import androidx.annotation.NonNull;

/**
 * @description:
 * @author:
 * @date: 2021/6/10 13:47
 */
class GlideDisPlayer implements ImagePickerDisPlayer
{
    @Override
    public void displayImage(@NonNull Context context, @NonNull MediaBean mediaBean, @NonNull ImageView imageView)
    {
        Glide.with(context)
                .load(mediaBean.getPath())
                .into(imageView);
    }

    @Override
    public void displayImage(@NonNull Context context, @NonNull BucketBean bucketBean, @NonNull ImageView imageView)
    {
        Glide.with(context)
                .load(bucketBean.getFirstImagePath())
                .into(imageView);
    }
}

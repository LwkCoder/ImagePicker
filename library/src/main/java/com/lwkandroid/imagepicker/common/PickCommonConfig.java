package com.lwkandroid.imagepicker.common;

import com.lwkandroid.imagepicker.config.ImagePickerDisPlayer;

/**
 * @description: 通用配置
 * @author: LWK
 * @date: 2021/6/9 16:03
 */
public class PickCommonConfig
{
    private ImagePickerDisPlayer mImagePickerDisPlayer;

    public static PickCommonConfig getInstance()
    {
        return Holder.INSTANCE;
    }

    private PickCommonConfig()
    {
    }

    private static final class Holder
    {
        private static final PickCommonConfig INSTANCE = new PickCommonConfig();
    }

    public ImagePickerDisPlayer getImagePickerDisplayer()
    {
        return mImagePickerDisPlayer;
    }

    public void setImagePickerDisPlayer(ImagePickerDisPlayer disPlayer)
    {
        this.mImagePickerDisPlayer = disPlayer;
    }
}
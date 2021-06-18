package com.lwkandroid.imagepicker.config;

/**
 * @description: 通用配置
 * @author: LWK
 * @date: 2021/6/9 16:03
 */
public class PickCommonConfig
{
    private ImagePickerDisplayer mImagePickerDisplayer;

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

    public ImagePickerDisplayer getImagePickerDisplayer()
    {
        return mImagePickerDisplayer;
    }

    public void setImagePickerDisPlayer(ImagePickerDisplayer disPlayer)
    {
        this.mImagePickerDisplayer = disPlayer;
    }
}
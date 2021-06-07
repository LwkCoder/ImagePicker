package com.lwkandroid.library;

import com.lwkandroid.library.options.SystemCropOptions;
import com.lwkandroid.library.options.SystemPhotographOptions;
import com.lwkandroid.library.options.SystemPickImageOptions;

import java.io.File;

/**
 * @description: 入口
 * @author: LWK
 * @date: 2021/6/1 14:32
 */
public final class ImagePicker
{
    private ImagePicker()
    {
        throw new UnsupportedOperationException("Can't instantiate this class !");
    }

    /**
     * 发起系统拍照
     */
    public static SystemPhotographOptions.Builder photographBySystem()
    {
        return new SystemPhotographOptions.Builder();
    }

    /**
     * 发起系统选择图片
     */
    public static SystemPickImageOptions.Builder pickImageBySystem()
    {
        return new SystemPickImageOptions.Builder();
    }

    /**
     * 发起系统裁剪
     *
     * @param imageFile 本地图片File对象
     */
    public static SystemCropOptions.Builder cropImageBySystem(File imageFile)
    {
        return new SystemCropOptions.Builder().setImageFile(imageFile);
    }
}

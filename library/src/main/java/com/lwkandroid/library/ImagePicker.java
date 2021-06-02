package com.lwkandroid.library;

import com.lwkandroid.library.bean.SystemPhotographOptions;
import com.lwkandroid.library.bean.SystemPickImageOptions;

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

    public static SystemPhotographOptions.Builder takePhotoBySystem()
    {
        return new SystemPhotographOptions.Builder();
    }

    public static SystemPickImageOptions.Builder pickImageBySystem()
    {
        return new SystemPickImageOptions.Builder();
    }
}

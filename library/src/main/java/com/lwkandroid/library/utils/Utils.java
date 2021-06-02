package com.lwkandroid.library.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @description: 通用工具
 * @author: LWK
 * @date: 2021/6/2 9:28
 */
public final class Utils
{
    private Utils()
    {
        throw new UnsupportedOperationException("Can't instantiate this class !");
    }

    /**
     * 判断指定缓存路径是否可用，不可用时返回默认缓存地址
     *
     * @param context
     * @param dirPath
     * @return
     */
    public static String getAvailableCacheDirPath(Context context, String dirPath)
    {
        if (dirPath != null)
        {
            File dirFile = new File(dirPath);
            if (dirFile.exists() || dirFile.mkdirs())
            {
                return dirFile.getAbsolutePath();
            }
        }

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            return context.getExternalCacheDir().getAbsolutePath();
        } else
        {
            return context.getCacheDir().getAbsolutePath();
        }
    }
}
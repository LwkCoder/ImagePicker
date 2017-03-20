package com.lwkandroid.imagepicker.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.lwkandroid.imagepicker.R;

import java.io.File;

/**
 * Created by LWK
 * TODO 拍照帮助类
 */

public class TakePhotoCompatUtils
{
    private static final String NAME_PREFIX = "IMG_";
    private static final String NAME_POSTFIX = ".jpg";

    /**
     * 拍照
     *
     * @param activity    发起拍照的帮助类
     * @param requestCode 请求码
     * @param cachePath   缓存地址
     */
    public static String takePhoto(Activity activity, int requestCode, String cachePath)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //自定义缓存路径
        File tempFile = getPhotoTempFile(cachePath);
        //7.0以上需要适配StickMode
        if (Build.VERSION.SDK_INT >= 24)
        {
            Uri imageUri = FileProvider.getUriForFile(activity, activity.getResources().getString(R.string.fileprovider_authorities), tempFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
            activity.startActivityForResult(intent, requestCode);
        } else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));//将拍取的照片保存到指定URI
            activity.startActivityForResult(intent, requestCode);
        }
        return tempFile.getAbsolutePath();
    }

    /**
     * 获取临时存储文件的File
     *
     * @param cachePath 缓存文件夹
     * @return 临时文件File
     */
    private static File getPhotoTempFile(String cachePath)
    {
        String name = new StringBuilder().append(NAME_PREFIX)
                .append(String.valueOf(System.currentTimeMillis()))
                .append(NAME_POSTFIX).toString();
        return new File(cachePath, name);
    }
}

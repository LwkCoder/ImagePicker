package com.lwkandroid.imagepicker.utils;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.data.ImageContants;
import com.lwkandroid.library.utils.ImagePickerFileProvider;

import java.io.File;

import androidx.core.content.FileProvider;

/**
 * Created by LWK
 * TODO 拍照帮助类
 */

public class TakePhotoCompatUtils
{
    static final int CAMERA_FACING_BACK = 0;
    static final int CAMERA_FACING_FRONT = 1;

    /**
     * 检查设备是否有摄像头
     *
     * @return
     */
    public static boolean hasCamera()
    {
        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++)
        {
            Camera.getCameraInfo(i, info);
            if (info.facing == CAMERA_FACING_BACK)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 拍照
     *
     * @param activity    发起拍照的帮助类
     * @param requestCode 请求码
     * @param cachePath   缓存地址
     * @return 拍照后图片地址
     */
    public static String takePhoto(Activity activity, int requestCode, String cachePath)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        try
        {
            File cacheFile = new File(cachePath);
            if (!cacheFile.exists())
            {
                cacheFile.mkdirs();
            }
            File tempFile = getPhotoTempFile(cachePath);
            Log.d("ImagePicker", "TakePhoto temp file path:" + tempFile.getAbsolutePath());
            //7.0以上需要适配StickMode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                Uri imageUri = FileProvider.getUriForFile(activity, ImagePickerFileProvider.getAuthorities(activity), tempFile);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //将拍取的照片保存到指定URI
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                activity.startActivityForResult(intent, requestCode);
            } else
            {
                //将拍取的照片保存到指定URI
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                activity.startActivityForResult(intent, requestCode);
            }
            return tempFile.getAbsolutePath();
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(activity, R.string.error_can_not_takephoto, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 获取临时存储文件的File
     *
     * @param cachePath 缓存文件夹
     * @return 临时文件File
     */
    private static File getPhotoTempFile(String cachePath)
    {
        String name = new StringBuilder().append(ImageContants.PHOTO_NAME_PREFIX)
                .append(System.currentTimeMillis())
                .append(ImageContants.IMG_NAME_POSTFIX).toString();
        return new File(cachePath, name);
    }
}

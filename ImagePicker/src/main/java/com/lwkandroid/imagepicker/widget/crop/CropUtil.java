package com.lwkandroid.imagepicker.widget.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.lwkandroid.imagepicker.data.ImageContants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CropUtil
{
    /**
     * 获取图片旋转角度
     *
     * @param path 图片路径
     * @return 旋转角度
     */
    public static int getExifRotation(String path)
    {
        if (TextUtils.isEmpty(path))
            return 0;

        try
        {
            ExifInterface exif = new ExifInterface(path);
            // We only recognize a subset of orientation tag values
            switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED))
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return ExifInterface.ORIENTATION_UNDEFINED;
            }
        } catch (IOException e)
        {
            return 0;
        }
    }

    /**
     * 解析待裁剪图片时计算SampleSize
     *
     * @param context context
     * @param path    图片路径
     * @return SampleSize
     */
    public static int calculateBitmapSampleSize(Context context, String path)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int maxSize = getMaxImageSize(context);
        int sampleSize = 1;
        while (options.outHeight / sampleSize > maxSize || options.outWidth / sampleSize > maxSize)
        {
            sampleSize = sampleSize << 1;
        }

        return sampleSize;
    }

    //比较屏幕，获取图片解析后的最大尺寸
    private static int getMaxImageSize(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        int width, height;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else
        {
            width = display.getWidth();
            height = display.getHeight();
        }
        return (int) Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
    }

    /**
     * 获取裁剪框内Bitmap
     */
    public static Bitmap decodeRegionCrop(Context context, String path, Rect rect, int outWidth, int outHeight, int exifRotation)
    {
        InputStream is = null;
        Bitmap croppedImage = null;
        try
        {
            is = new FileInputStream(path);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);

            final int width = decoder.getWidth();
            final int height = decoder.getHeight();

            if (exifRotation != 0)
            {
                // Adjust crop area to account for image rotation
                Matrix matrix = new Matrix();
                matrix.setRotate(-exifRotation);

                RectF adjusted = new RectF();
                matrix.mapRect(adjusted, new RectF(rect));

                // Adjust to account for origin at 0,0
                adjusted.offset(adjusted.left < 0 ? width : 0, adjusted.top < 0 ? height : 0);
                rect = new Rect((int) adjusted.left, (int) adjusted.top, (int) adjusted.right, (int) adjusted.bottom);
            }

            int maxSize = getMaxImageSize(context);
            int sampleSize = 1;
            while (rect.width() / sampleSize > maxSize || rect.height() / sampleSize > maxSize)
            {
                sampleSize = sampleSize << 1;
            }

            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inSampleSize = sampleSize;
            croppedImage = decoder.decodeRegion(rect, option);

            boolean isRequired = false;
            Matrix matrix = new Matrix();
            if (exifRotation != 0)
            {
                matrix.postRotate(exifRotation);
                isRequired = true;
            }

            if (outWidth > 0 && outHeight > 0)
            {
                RotateBitmap rotateBitmap = new RotateBitmap(croppedImage, exifRotation);
                matrix.postScale((float) outWidth / rotateBitmap.getWidth(), (float) outHeight / rotateBitmap.getHeight());

                isRequired = true;
            }

            if (isRequired)
            {
                croppedImage = Bitmap.createBitmap(croppedImage, 0, 0, croppedImage.getWidth(), croppedImage.getHeight(), matrix, true);
            }
        } catch (Exception e)
        {
            croppedImage = null;
        } finally
        {
            try
            {
                if (is != null)
                    is.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return croppedImage;
    }

    /**
     * 创建裁剪图片的名字
     */
    public static String createCropName()
    {
        return new StringBuilder().append(ImageContants.CROP_NAME_PREFIX)
                .append(String.valueOf(System.currentTimeMillis()))
                .append(ImageContants.IMG_NAME_POSTFIX).toString();
    }

    /**
     * 保存图片
     *
     * @param bitmap   需要保存的图片
     * @param saveName 图片保存的名称
     * @return 返回保存后的图片地址
     */
    public static String saveBmp(Bitmap bitmap, String savePath, String saveName)
    {
        String resultPath = null;
        if (bitmap == null)
            return resultPath;
        try
        {
            //保存位置
            File cacheFile = new File(savePath);
            if (!cacheFile.exists())
                cacheFile.mkdirs();
            File file = new File(savePath, saveName);
            if (file.exists())
                file.delete();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            bitmap.recycle();
            bitmap = null;
            System.gc();
            resultPath = file.getAbsolutePath();
        } catch (Exception e)
        {
            Log.e("CropUtils", "saveBmp(): savePath = " + savePath + "\nsaveName = " + saveName + "\n保存图片失败：" + e.toString());
        }
        return resultPath;
    }
}
package com.lwkandroid.imagepicker.widget.crop;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.content.FileProvider;
import android.view.MotionEvent;
import android.view.View;

import com.lwkandroid.imagepicker.R;

import java.io.File;

public class CropCompat
{
    private static final int SIXTY_FPS_INTERVAL = 1000 / 60;

    public static void postOnAnimation(View view, Runnable runnable)
    {
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN)
        {
            postOnAnimationJellyBean(view, runnable);
        } else
        {
            view.postDelayed(runnable, SIXTY_FPS_INTERVAL);
        }
    }

    @TargetApi(16)
    private static void postOnAnimationJellyBean(View view, Runnable runnable)
    {
        view.postOnAnimation(runnable);
    }

    public static int getPointerIndex(int action)
    {
        if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB)
        {
            return getPointerIndexHoneyComb(action);
        } else
        {
            return getPointerIndexEclair(action);
        }
    }

    @SuppressWarnings("deprecation")
    @TargetApi(VERSION_CODES.ECLAIR)
    private static int getPointerIndexEclair(int action)
    {
        return (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    private static int getPointerIndexHoneyComb(int action)
    {
        return (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    }

    /**
     * 获取某个File对应的Uri
     */
    public static Uri getUriFromFile(Context context, File file)
    {
        if (Build.VERSION.SDK_INT >= 24)
        {
            return FileProvider.getUriForFile(context, context.getResources().getString(R.string.fileprovider_authorities), file);
        } else
        {
            return Uri.fromFile(file);
        }
    }
}
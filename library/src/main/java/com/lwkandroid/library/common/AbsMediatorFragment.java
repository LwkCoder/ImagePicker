package com.lwkandroid.library.common;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * @description: 通用中介Fragment
 * @author:
 * @date: 2021/6/2 13:37
 */
public abstract class AbsMediatorFragment extends Fragment
{
    private int mScreenOrientation;
    private boolean mHasStartRequest;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        FragmentActivity activity = getActivity();
        if (activity == null)
        {
            return;
        }
        // 如果当前没有锁定屏幕方向就获取当前屏幕方向并进行锁定
        mScreenOrientation = activity.getRequestedOrientation();
        if (mScreenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        {
            return;
        }
        int activityOrientation = activity.getResources().getConfiguration().orientation;
        try
        {
            // 兼容问题：在 Android 8.0 的手机上可以固定 Activity 的方向，但是这个 Activity 不能是透明的，否则就会抛出异常
            // 复现场景：只需要给 Activity 主题设置 <item name="android:windowIsTranslucent">true</item> 属性即可
            if (activityOrientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else if (activityOrientation == Configuration.ORIENTATION_PORTRAIT)
            {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } catch (IllegalStateException e)
        {
            // java.lang.IllegalStateException: Only fullscreen activities can request orientation
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        FragmentActivity activity = getActivity();
        if (activity == null || mScreenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        {
            return;
        }
        // 为什么这里不用跟上面一样 try catch ？因为这里是把 Activity 方向取消固定，只有设置横屏或竖屏的时候才可能触发 crash
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mHasStartRequest)
        {
            return;
        }

        mHasStartRequest = true;

        doRequest();
    }

    public void attachActivity(FragmentActivity activity)
    {
        if (activity != null)
        {
            activity.getSupportFragmentManager().beginTransaction()
                    .add(this, this.toString()).commitAllowingStateLoss();
        }
    }

    public void detachActivity(FragmentActivity activity)
    {
        if (activity != null)
        {
            activity.getSupportFragmentManager().beginTransaction()
                    .remove(this).commitAllowingStateLoss();
        }
    }


    protected abstract void doRequest();
}

package com.lwkandroid.imagepicker.widget;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 兼容图片缩放控件的ViewPager
 */
public class ImagePickerViewPager extends ViewPager
{
    public ImagePickerViewPager(Context context)
    {
        super(context);
    }

    public ImagePickerViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        try
        {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        try
        {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
}

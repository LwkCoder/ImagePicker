package com.lwkandroid.imagepicker.config;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.lwkandroid.imagepicker.R;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.res.ResourcesCompat;

/**
 * @description: 自定义界面风格
 * @author: LWK
 * @date: 2021/6/10 14:05
 */
public class CustomCropImageStyle implements Parcelable
{
    @ColorInt
    private int navigationBarColor;

    @ColorInt
    private int rootBackgroundColor;

    @ColorInt
    private int doneTextColor;

    @ColorInt
    private int operationTintColor;

    public int getNavigationBarColor()
    {
        return navigationBarColor;
    }

    public void setNavigationBarColor(int navigationBarColor)
    {
        this.navigationBarColor = navigationBarColor;
    }

    public int getRootBackgroundColor()
    {
        return rootBackgroundColor;
    }

    public void setRootBackgroundColor(int rootBackgroundColor)
    {
        this.rootBackgroundColor = rootBackgroundColor;
    }

    public int getDoneTextColor()
    {
        return doneTextColor;
    }

    public void setDoneTextColor(int doneTextColor)
    {
        this.doneTextColor = doneTextColor;
    }

    public int getOperationTintColor()
    {
        return operationTintColor;
    }

    public void setOperationTintColor(int operationTintColor)
    {
        this.operationTintColor = operationTintColor;
    }

    public static class Builder
    {
        private CustomCropImageStyle mStyle;

        public Builder()
        {
            mStyle = new CustomCropImageStyle();
        }

        public Builder setNavigationBarColor(@ColorInt int color)
        {
            mStyle.setNavigationBarColor(color);
            return this;
        }

        public Builder setNavigationBarColorResId(Context context, @ColorRes int resId)
        {
            mStyle.setNavigationBarColor(ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()));
            return this;
        }

        public Builder setRootBackgroundColor(@ColorInt int color)
        {
            mStyle.setRootBackgroundColor(color);
            return this;
        }

        public Builder setRootBackgroundColorResId(Context context, @ColorRes int resId)
        {
            mStyle.setRootBackgroundColor(ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()));
            return this;
        }

        public Builder setOperationTintColor(@ColorInt int color)
        {
            mStyle.setOperationTintColor(color);
            return this;
        }

        public Builder setOperationTintColorResId(Context context, @ColorRes int resId)
        {
            mStyle.setOperationTintColor(ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()));
            return this;
        }

        public Builder setDoneTextColor(@ColorInt int color)
        {
            mStyle.setDoneTextColor(color);
            return this;
        }

        public Builder setDoneTextColorResId(Context context, @ColorRes int resId)
        {
            mStyle.setDoneTextColor(ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()));
            return this;
        }


        public CustomCropImageStyle build()
        {
            return mStyle;
        }
    }

    public static CustomCropImageStyle dark(Context context)
    {
        return new Builder()
                .setNavigationBarColorResId(context, R.color.image_crop_style_dark_navigation_bar_background)
                .setRootBackgroundColorResId(context, R.color.image_crop_style_dark_root_background)
                .setDoneTextColorResId(context, R.color.image_crop_style_dark_done_text)
                .setOperationTintColorResId(context, R.color.image_crop_style_dark_operation_tint)
                .build();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.navigationBarColor);
        dest.writeInt(this.rootBackgroundColor);
        dest.writeInt(this.doneTextColor);
        dest.writeInt(this.operationTintColor);
    }

    public CustomCropImageStyle()
    {
    }

    protected CustomCropImageStyle(Parcel in)
    {
        this.navigationBarColor = in.readInt();
        this.rootBackgroundColor = in.readInt();
        this.doneTextColor = in.readInt();
        this.operationTintColor = in.readInt();
    }

    public static final Creator<CustomCropImageStyle> CREATOR = new Creator<CustomCropImageStyle>()
    {
        @Override
        public CustomCropImageStyle createFromParcel(Parcel source)
        {
            return new CustomCropImageStyle(source);
        }

        @Override
        public CustomCropImageStyle[] newArray(int size)
        {
            return new CustomCropImageStyle[size];
        }
    };
}

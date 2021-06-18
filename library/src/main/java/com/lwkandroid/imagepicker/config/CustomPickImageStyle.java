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
public class CustomPickImageStyle implements Parcelable
{
    @ColorInt
    private int statusBarColor;

    @ColorInt
    private int actionBarColor;

    @ColorInt
    private int navigationBarColor;

    @ColorInt
    private int rootBackgroundColor;

    @ColorInt
    private int actionBarTextColor;

    @ColorInt
    private int loadingColor;

    @ColorInt
    private int bucketNameTextColor;

    @ColorInt
    private int bucketListBackgroundColor;

    @ColorInt
    private int doneTextColor;

    @ColorInt
    private int checkWidgetNormalColor;

    @ColorInt
    private int checkWidgetCheckedColor;

    public int getStatusBarColor()
    {
        return statusBarColor;
    }

    public void setStatusBarColor(int statusBarColor)
    {
        this.statusBarColor = statusBarColor;
    }

    public int getActionBarColor()
    {
        return actionBarColor;
    }

    public void setActionBarColor(int actionBarColor)
    {
        this.actionBarColor = actionBarColor;
    }

    public int getActionBarTextColor()
    {
        return actionBarTextColor;
    }

    public void setActionBarTextColor(int actionBarTextColor)
    {
        this.actionBarTextColor = actionBarTextColor;
    }

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

    public int getLoadingColor()
    {
        return loadingColor;
    }

    public void setLoadingColor(int loadingColor)
    {
        this.loadingColor = loadingColor;
    }

    public int getBucketNameTextColor()
    {
        return bucketNameTextColor;
    }

    public void setBucketNameTextColor(int bucketNameTextColor)
    {
        this.bucketNameTextColor = bucketNameTextColor;
    }

    public int getBucketListBackgroundColor()
    {
        return bucketListBackgroundColor;
    }

    public void setBucketListBackgroundColor(int bucketListBackgroundColor)
    {
        this.bucketListBackgroundColor = bucketListBackgroundColor;
    }

    public int getDoneTextColor()
    {
        return doneTextColor;
    }

    public void setDoneTextColor(int doneTextColor)
    {
        this.doneTextColor = doneTextColor;
    }

    public int getCheckWidgetNormalColor()
    {
        return checkWidgetNormalColor;
    }

    public void setCheckWidgetNormalColor(int checkWidgetNormalColor)
    {
        this.checkWidgetNormalColor = checkWidgetNormalColor;
    }

    public int getCheckWidgetCheckedColor()
    {
        return checkWidgetCheckedColor;
    }

    public void setCheckWidgetCheckedColor(int checkWidgetCheckedColor)
    {
        this.checkWidgetCheckedColor = checkWidgetCheckedColor;
    }

    public static CustomPickImageStyle dark(Context context)
    {
        return new Builder()
                .setStatusBarColorResId(context, R.color.image_pick_style_dark_status_bar)
                .setActionBarColorResId(context, R.color.image_pick_style_dark_action_bar_background)
                .setNavigationBarColorResId(context, R.color.image_pick_style_dark_navigation_bar_background)
                .setRootBackgroundColorResId(context, R.color.image_pick_style_dark_root_background)
                .setActionBarTextColorResId(context, R.color.image_pick_style_dark_action_bar_text)
                .setLoadingColorResId(context, R.color.image_pick_style_dark_loading)
                .setBucketNameTextColorResId(context, R.color.image_pick_style_dark_bucket_name)
                .setBucketListBackgroundColorResId(context, R.color.image_pick_style_dark_bucket_list_background)
                .setDoneTextColorResId(context, R.color.image_pick_style_dark_done_text)
                .setCheckWidgetNormalColorResId(context, R.color.image_pick_style_dark_check_widget_normal)
                .setCheckWidgetCheckedColorResId(context, R.color.image_pick_style_dark_check_widget_checked)
                .build();
    }

    public static class Builder
    {
        private CustomPickImageStyle mStyle;

        public Builder()
        {
            mStyle = new CustomPickImageStyle();
        }

        public Builder setStatusBarColor(@ColorInt int color)
        {
            mStyle.setStatusBarColor(color);
            return this;
        }

        public Builder setStatusBarColorResId(Context context, @ColorRes int resId)
        {
            mStyle.setStatusBarColor(ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()));
            return this;
        }

        public Builder setActionBarColor(@ColorInt int color)
        {
            mStyle.setActionBarColor(color);
            return this;
        }

        public Builder setActionBarColorResId(Context context, @ColorRes int resId)
        {
            mStyle.setActionBarColor(ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()));
            return this;
        }

        public Builder setActionBarTextColor(@ColorInt int color)
        {
            mStyle.setActionBarTextColor(color);
            return this;
        }

        public Builder setActionBarTextColorResId(Context context, @ColorRes int resId)
        {
            mStyle.setActionBarTextColor(ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()));
            return this;
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

        public Builder setLoadingColor(@ColorInt int color)
        {
            mStyle.setLoadingColor(color);
            return this;
        }

        public Builder setLoadingColorResId(Context context, @ColorRes int resId)
        {
            mStyle.setLoadingColor(ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()));
            return this;
        }

        public Builder setBucketNameTextColor(@ColorInt int color)
        {
            mStyle.setBucketNameTextColor(color);
            return this;
        }

        public Builder setBucketNameTextColorResId(Context context, @ColorRes int resId)
        {
            mStyle.setBucketNameTextColor(ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()));
            return this;
        }

        public Builder setBucketListBackgroundColor(@ColorInt int color)
        {
            mStyle.setBucketListBackgroundColor(color);
            return this;
        }

        public Builder setBucketListBackgroundColorResId(Context context, @ColorRes int resId)
        {
            mStyle.setBucketListBackgroundColor(ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()));
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

        public Builder setCheckWidgetNormalColor(@ColorInt int color)
        {
            mStyle.setCheckWidgetNormalColor(color);
            return this;
        }

        public Builder setCheckWidgetNormalColorResId(Context context, @ColorRes int resId)
        {
            mStyle.setCheckWidgetNormalColor(ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()));
            return this;
        }

        public Builder setCheckWidgetCheckedColor(@ColorInt int color)
        {
            mStyle.setCheckWidgetCheckedColor(color);
            return this;
        }

        public Builder setCheckWidgetCheckedColorResId(Context context, @ColorRes int resId)
        {
            mStyle.setCheckWidgetCheckedColor(ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()));
            return this;
        }

        public CustomPickImageStyle build()
        {
            return mStyle;
        }
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.statusBarColor);
        dest.writeInt(this.actionBarColor);
        dest.writeInt(this.navigationBarColor);
        dest.writeInt(this.rootBackgroundColor);
        dest.writeInt(this.actionBarTextColor);
        dest.writeInt(this.loadingColor);
        dest.writeInt(this.bucketNameTextColor);
        dest.writeInt(this.bucketListBackgroundColor);
        dest.writeInt(this.doneTextColor);
        dest.writeInt(this.checkWidgetNormalColor);
        dest.writeInt(this.checkWidgetCheckedColor);
    }

    public CustomPickImageStyle()
    {
    }

    protected CustomPickImageStyle(Parcel in)
    {
        this.statusBarColor = in.readInt();
        this.actionBarColor = in.readInt();
        this.navigationBarColor = in.readInt();
        this.rootBackgroundColor = in.readInt();
        this.actionBarTextColor = in.readInt();
        this.loadingColor = in.readInt();
        this.bucketNameTextColor = in.readInt();
        this.bucketListBackgroundColor = in.readInt();
        this.doneTextColor = in.readInt();
        this.checkWidgetNormalColor = in.readInt();
        this.checkWidgetCheckedColor = in.readInt();
    }

    public static final Creator<CustomPickImageStyle> CREATOR = new Creator<CustomPickImageStyle>()
    {
        @Override
        public CustomPickImageStyle createFromParcel(Parcel source)
        {
            return new CustomPickImageStyle(source);
        }

        @Override
        public CustomPickImageStyle[] newArray(int size)
        {
            return new CustomPickImageStyle[size];
        }
    };
}

package com.lwkandroid.imagepicker.config;

import android.os.Parcel;
import android.os.Parcelable;

import com.lwkandroid.imagepicker.custom.crop.CustomCropImageRequestImpl;
import com.lwkandroid.imagepicker.custom.crop.ICustomCropImageRequest;

import java.io.File;

/**
 * @description: 系统裁剪的配置
 * @author: LWK
 * @date: 2021/6/3 9:21
 */
public class CustomCropImageOptions implements Parcelable
{
    private File imageFile;
    private String cacheDirPath;
    /**
     * 裁剪框宽度比例
     */
    private int aspectX = 1;
    /**
     * 裁剪框高度比例
     */
    private int aspectY = 1;
    /**
     * 输出图片宽度尺寸
     */
    private int outputX = 100;
    /**
     * 输出图片高度尺寸
     */
    private int outputY = 100;

    public File getImageFile()
    {
        return imageFile;
    }

    public void setImageFile(File imageFile)
    {
        this.imageFile = imageFile;
    }

    public int getAspectX()
    {
        return aspectX;
    }

    public void setAspectX(int aspectX)
    {
        this.aspectX = aspectX;
    }

    public int getAspectY()
    {
        return aspectY;
    }

    public void setAspectY(int aspectY)
    {
        this.aspectY = aspectY;
    }

    public int getOutputX()
    {
        return outputX;
    }

    public void setOutputX(int outputX)
    {
        this.outputX = outputX;
    }

    public int getOutputY()
    {
        return outputY;
    }

    public void setOutputY(int outputY)
    {
        this.outputY = outputY;
    }

    public String getCacheDirPath()
    {
        return cacheDirPath;
    }

    public void setCacheDirPath(String cacheDirPath)
    {
        this.cacheDirPath = cacheDirPath;
    }

    public static class Builder
    {
        private CustomCropImageOptions mOptions;

        public Builder()
        {
            this.mOptions = new CustomCropImageOptions();
        }

        public Builder setImageFile(File imageFile)
        {
            mOptions.setImageFile(imageFile);
            return this;
        }

        public Builder setAspectX(int aspectX)
        {
            mOptions.setAspectX(aspectX);
            return this;
        }

        public Builder setAspectY(int aspectY)
        {
            mOptions.setAspectY(aspectY);
            return this;
        }

        public Builder setOutputX(int outputX)
        {
            mOptions.setOutputX(outputX);
            return this;
        }

        public Builder setOutputY(int outputY)
        {
            mOptions.setOutputY(outputY);
            return this;
        }

        public Builder setCacheDirPath(String cacheDirPath)
        {
            mOptions.setCacheDirPath(cacheDirPath);
            return this;
        }

        public ICustomCropImageRequest build()
        {
            return new CustomCropImageRequestImpl(mOptions);
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
        dest.writeSerializable(this.imageFile);
        dest.writeString(this.cacheDirPath);
        dest.writeInt(this.aspectX);
        dest.writeInt(this.aspectY);
        dest.writeInt(this.outputX);
        dest.writeInt(this.outputY);
    }

    public CustomCropImageOptions()
    {
    }

    protected CustomCropImageOptions(Parcel in)
    {
        this.imageFile = (File) in.readSerializable();
        this.cacheDirPath = in.readString();
        this.aspectX = in.readInt();
        this.aspectY = in.readInt();
        this.outputX = in.readInt();
        this.outputY = in.readInt();
    }

    public static final Parcelable.Creator<CustomCropImageOptions> CREATOR = new Parcelable.Creator<CustomCropImageOptions>()
    {
        @Override
        public CustomCropImageOptions createFromParcel(Parcel source)
        {
            return new CustomCropImageOptions(source);
        }

        @Override
        public CustomCropImageOptions[] newArray(int size)
        {
            return new CustomCropImageOptions[size];
        }
    };
}

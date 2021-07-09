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
    private CustomCropImageStyle style;
    /**
     * 裁剪框宽度比例
     */
    private int aspectX = 1;
    /**
     * 裁剪框高度比例
     */
    private int aspectY = 1;
    /**
     * 是否圆形裁剪
     */
    private boolean circleShape = false;
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

    public String getCacheDirPath()
    {
        return cacheDirPath;
    }

    public void setCacheDirPath(String cacheDirPath)
    {
        this.cacheDirPath = cacheDirPath;
    }

    public boolean isCircleShape()
    {
        return circleShape;
    }

    public void setCircleShape(boolean circleShape)
    {
        this.circleShape = circleShape;
    }

    public CustomCropImageStyle getStyle()
    {
        return style;
    }

    public void setStyle(CustomCropImageStyle style)
    {
        this.style = style;
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

        public Builder setAspectSize(int aspectX, int aspectY)
        {
            mOptions.setAspectX(aspectX);
            mOptions.setAspectY(aspectY);
            return this;
        }

        public Builder setCacheDirPath(String cacheDirPath)
        {
            mOptions.setCacheDirPath(cacheDirPath);
            return this;
        }

        public Builder setCircleShape(boolean circleShape)
        {
            mOptions.setCircleShape(circleShape);
            return this;
        }

        public Builder setStyle(CustomCropImageStyle style)
        {
            mOptions.setStyle(style);
            return this;
        }

        public Builder setOutputSize(int x, int y)
        {
            mOptions.setOutputX(x);
            mOptions.setOutputY(y);
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
        dest.writeParcelable(this.style, flags);
        dest.writeInt(this.aspectX);
        dest.writeInt(this.aspectY);
        dest.writeByte(this.circleShape ? (byte) 1 : (byte) 0);
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
        this.style = in.readParcelable(CustomCropImageStyle.class.getClassLoader());
        this.aspectX = in.readInt();
        this.aspectY = in.readInt();
        this.circleShape = in.readByte() != 0;
        this.outputX = in.readInt();
        this.outputY = in.readInt();
    }

    public static final Creator<CustomCropImageOptions> CREATOR = new Creator<CustomCropImageOptions>()
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

package com.lwkandroid.imagepicker.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LWK
 * TODO 图片选择各参数
 */

public class ImagePickerOptions implements Parcelable
{
    private ImagePickType type = ImagePickType.SINGLE;
    private int maxNum = 1;
    private boolean needCamera = true;
    private boolean needCrop;
    private ImagePickerCropParams cropParams;
    private String cachePath;

    public ImagePickType getType()
    {
        return type;
    }

    public void setType(ImagePickType type)
    {
        this.type = type;
    }

    public int getMaxNum()
    {
        return maxNum;
    }

    public void setMaxNum(int maxNum)
    {
        if (maxNum > 0)
            this.maxNum = maxNum;
    }

    public boolean isNeedCamera()
    {
        return needCamera;
    }

    public void setNeedCamera(boolean needCamera)
    {
        this.needCamera = needCamera;
    }

    public boolean isNeedCrop()
    {
        return needCrop;
    }

    public void setNeedCrop(boolean needCrop)
    {
        this.needCrop = needCrop;
    }

    public String getCachePath()
    {
        return cachePath;
    }

    public void setCachePath(String cachePath)
    {
        this.cachePath = cachePath;
    }

    public ImagePickerCropParams getCropParams()
    {
        return cropParams;
    }

    public void setCropParams(ImagePickerCropParams cropParams)
    {
        this.cropParams = cropParams;
    }

    @Override
    public String toString()
    {
        return "ImagePickerOptions{" +
                "type=" + type +
                ", maxNum=" + maxNum +
                ", needCamera=" + needCamera +
                ", needCrop=" + needCrop +
                ", cropParams=" + cropParams +
                ", cachePath='" + cachePath + '\'' +
                '}';
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeInt(this.maxNum);
        dest.writeByte(this.needCamera ? (byte) 1 : (byte) 0);
        dest.writeByte(this.needCrop ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.cropParams, flags);
        dest.writeString(this.cachePath);
    }

    public ImagePickerOptions()
    {
    }

    protected ImagePickerOptions(Parcel in)
    {
        int tmpMode = in.readInt();
        this.type = tmpMode == -1 ? null : ImagePickType.values()[tmpMode];
        this.maxNum = in.readInt();
        this.needCamera = in.readByte() != 0;
        this.needCrop = in.readByte() != 0;
        this.cropParams = in.readParcelable(ImagePickerCropParams.class.getClassLoader());
        this.cachePath = in.readString();
    }

    public static final Parcelable.Creator<ImagePickerOptions> CREATOR = new Parcelable.Creator<ImagePickerOptions>()
    {
        @Override
        public ImagePickerOptions createFromParcel(Parcel source)
        {
            return new ImagePickerOptions(source);
        }

        @Override
        public ImagePickerOptions[] newArray(int size)
        {
            return new ImagePickerOptions[size];
        }
    };
}

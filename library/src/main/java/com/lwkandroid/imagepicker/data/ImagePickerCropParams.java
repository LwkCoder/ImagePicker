package com.lwkandroid.imagepicker.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LWK
 * TODO 裁剪参数
 */
public class ImagePickerCropParams implements Parcelable
{
    private int aspectX = 1;

    private int aspectY = 1;

    private int outputX;

    private int outputY;

    public ImagePickerCropParams(int aspectX, int aspectY, int outputX, int outputY)
    {
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        this.outputX = outputX;
        this.outputY = outputY;
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

    @Override
    public String toString()
    {
        return "ImagePickerCropParams{" +
                "aspectX=" + aspectX +
                ", aspectY=" + aspectY +
                ", outputX=" + outputX +
                ", outputY=" + outputY +
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
        dest.writeInt(this.aspectX);
        dest.writeInt(this.aspectY);
        dest.writeInt(this.outputX);
        dest.writeInt(this.outputY);
    }

    public ImagePickerCropParams()
    {
    }

    protected ImagePickerCropParams(Parcel in)
    {
        this.aspectX = in.readInt();
        this.aspectY = in.readInt();
        this.outputX = in.readInt();
        this.outputY = in.readInt();
    }

    public static final Creator<ImagePickerCropParams> CREATOR = new Creator<ImagePickerCropParams>()
    {
        @Override
        public ImagePickerCropParams createFromParcel(Parcel source)
        {
            return new ImagePickerCropParams(source);
        }

        @Override
        public ImagePickerCropParams[] newArray(int size)
        {
            return new ImagePickerCropParams[size];
        }
    };
}

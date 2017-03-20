package com.lwkandroid.imagepicker.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LWK
 * TODO 裁剪参数
 */
public class ImagePickerCropParams implements Parcelable
{
    /**
     * 裁剪模式
     */
    private ImageCropType type = ImageCropType.RECT;

    /**
     * 裁剪的宽高比
     */
    private float ratio = Contants.DEF_CROP_RATIO;

    /**
     * 裁剪后最大宽度
      */
    private int cropMaxWidth = Contants.DEF_CROP_MAX_WIDTH;

    public ImageCropType getType()
    {
        return type;
    }

    public void setType(ImageCropType type)
    {
        this.type = type;
    }

    public float getRatio()
    {
        return ratio;
    }

    public void setRatio(float ratio)
    {
        this.ratio = ratio;
    }

    public int getCropMaxWidth()
    {
        return cropMaxWidth;
    }

    public void setCropMaxWidth(int cropMaxWidth)
    {
        this.cropMaxWidth = cropMaxWidth;
    }

    @Override
    public String toString()
    {
        return "ImagePickerCropParams{" +
                "type=" + type +
                ", ratio=" + ratio +
                ", cropMaxWidth=" + cropMaxWidth +
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
        dest.writeFloat(this.ratio);
        dest.writeInt(this.cropMaxWidth);
    }

    public ImagePickerCropParams()
    {
    }

    protected ImagePickerCropParams(Parcel in)
    {
        int tmpMode = in.readInt();
        this.type = tmpMode == -1 ? null : ImageCropType.values()[tmpMode];
        this.ratio = in.readFloat();
        this.cropMaxWidth = in.readInt();
    }

    public static final Parcelable.Creator<ImagePickerCropParams> CREATOR = new Parcelable.Creator<ImagePickerCropParams>()
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

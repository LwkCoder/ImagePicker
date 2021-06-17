package com.lwkandroid.imagepicker.config;

import android.os.Parcel;
import android.os.Parcelable;

import com.lwkandroid.imagepicker.constants.PickMimeType;
import com.lwkandroid.imagepicker.custom.pick.CustomPickImageRequestImpl;
import com.lwkandroid.imagepicker.custom.pick.ICustomPickImageRequest;

/**
 * @description: 自定义选择图片的配置
 * @author: LWK
 * @date: 2021/6/7 14:10
 */
public class CustomPickImageOptions implements Parcelable
{
    private int maxPickNumber = 1;
    private long fileMinSize = 0;
    private long fileMaxSize = Long.MAX_VALUE;
    private String[] mimeTypeArray = PickMimeType.ARRAY_NO_LIMIT;
    private CustomPickImageStyle style;
    private boolean showOriginalFileCheckBox;

    public int getMaxPickNumber()
    {
        return maxPickNumber;
    }

    public void setMaxPickNumber(int maxPickNumber)
    {
        this.maxPickNumber = maxPickNumber;
    }

    public long getFileMinSize()
    {
        return fileMinSize;
    }

    public void setFileMinSize(long fileMinSize)
    {
        this.fileMinSize = fileMinSize;
    }

    public long getFileMaxSize()
    {
        return fileMaxSize;
    }

    public void setFileMaxSize(long fileMaxSize)
    {
        this.fileMaxSize = fileMaxSize;
    }

    public String[] getMimeTypeArray()
    {
        return mimeTypeArray;
    }

    public void setMimeTypeArray(String[] mimeTypeArray)
    {
        this.mimeTypeArray = mimeTypeArray;
    }

    public CustomPickImageStyle getStyle()
    {
        return style;
    }

    public void setStyle(CustomPickImageStyle style)
    {
        this.style = style;
    }

    public boolean isShowOriginalFileCheckBox()
    {
        return showOriginalFileCheckBox;
    }

    public void setShowOriginalFileCheckBox(boolean b)
    {
        this.showOriginalFileCheckBox = b;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.maxPickNumber);
        dest.writeLong(this.fileMinSize);
        dest.writeLong(this.fileMaxSize);
        dest.writeStringArray(this.mimeTypeArray);
        dest.writeParcelable(this.style, flags);
        dest.writeByte(this.showOriginalFileCheckBox ? (byte) 1 : (byte) 0);
    }

    public CustomPickImageOptions()
    {
    }

    protected CustomPickImageOptions(Parcel in)
    {
        this.maxPickNumber = in.readInt();
        this.fileMinSize = in.readLong();
        this.fileMaxSize = in.readLong();
        this.mimeTypeArray = in.createStringArray();
        this.style = in.readParcelable(CustomPickImageStyle.class.getClassLoader());
        this.showOriginalFileCheckBox = in.readByte() != 0;
    }

    public static final Creator<CustomPickImageOptions> CREATOR = new Creator<CustomPickImageOptions>()
    {
        @Override
        public CustomPickImageOptions createFromParcel(Parcel source)
        {
            return new CustomPickImageOptions(source);
        }

        @Override
        public CustomPickImageOptions[] newArray(int size)
        {
            return new CustomPickImageOptions[size];
        }
    };

    public static class Builder
    {
        private CustomPickImageOptions mOptions;

        public Builder()
        {
            mOptions = new CustomPickImageOptions();
        }

        public Builder setMaxPickNumber(int maxPickNumber)
        {
            mOptions.setMaxPickNumber(maxPickNumber);
            return this;
        }

        public Builder setFileMinSize(int fileMinSize)
        {
            mOptions.setFileMinSize(fileMinSize);
            return this;
        }

        public Builder setFileMaxSize(int fileMaxSize)
        {
            mOptions.setFileMaxSize(fileMaxSize);
            return this;
        }

        public Builder setPickMimeType(@PickMimeType.Type int type)
        {
            mOptions.setMimeTypeArray(PickMimeType.getMimeTypeArrayByType(type));
            return this;
        }

        public Builder setStyle(CustomPickImageStyle style)
        {
            mOptions.setStyle(style);
            return this;
        }

        public Builder setShowOriginalFileCheckBox(boolean show)
        {
            mOptions.setShowOriginalFileCheckBox(show);
            return this;
        }

        public ICustomPickImageRequest build()
        {
            return new CustomPickImageRequestImpl(mOptions);
        }
    }
}

package com.lwkandroid.imagepicker.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:图片文件夹实体类
 */
public class ImageFloderBean implements Parcelable
{
    private String floderId;
    private String floderName;
    private String firstImgPath;
    private int num;

    public ImageFloderBean(String floderId)
    {
        this.floderId = floderId;
    }

    public ImageFloderBean(String floderId, String floderName)
    {
        this.floderId = floderId;
        this.floderName = floderName;
    }

    public String getFloderId()
    {
        return floderId;
    }

    public void setFloderId(String floderId)
    {
        this.floderId = floderId;
    }

    public String getFloderName()
    {
        return floderName;
    }

    public void setFloderName(String floderName)
    {
        this.floderName = floderName;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public String getFirstImgPath()
    {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath)
    {
        this.firstImgPath = firstImgPath;
    }

    public void gainNum()
    {
        this.num++;
    }

    @Override
    public String toString()
    {
        return "ImageFloderBean{" +
                "floderId='" + floderId + '\'' +
                ", floderName='" + floderName + '\'' +
                ", firstImgPath='" + firstImgPath + '\'' +
                ", num=" + num +
                '}';
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public boolean equals(Object obj)
    {
        return (obj instanceof ImageFloderBean && ((ImageFloderBean) obj).getFloderId().equals(floderId));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.floderId);
        dest.writeString(this.floderName);
        dest.writeString(this.firstImgPath);
        dest.writeInt(this.num);
    }

    public ImageFloderBean()
    {
    }

    protected ImageFloderBean(Parcel in)
    {
        this.floderId = in.readString();
        this.floderName = in.readString();
        this.firstImgPath = in.readString();
        this.num = in.readInt();
    }

    public static final Creator<ImageFloderBean> CREATOR = new Creator<ImageFloderBean>()
    {
        @Override
        public ImageFloderBean createFromParcel(Parcel source)
        {
            return new ImageFloderBean(source);
        }

        @Override
        public ImageFloderBean[] newArray(int size)
        {
            return new ImageFloderBean[size];
        }
    };
}

package com.lwkandroid.imagepicker.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:图片文件夹实体类
 */
public class ImageFolderBean implements Parcelable
{
    private String floderId;
    private String floderName;
    private String firstImgPath;
    private int num;

    public ImageFolderBean(String floderId)
    {
        this.floderId = floderId;
    }

    public ImageFolderBean(String floderId, String floderName)
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
        return "ImageFolderBean{" +
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
        return (obj instanceof ImageFolderBean && ((ImageFolderBean) obj).getFloderId().equals(floderId));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.floderId);
        dest.writeString(this.floderName);
        dest.writeString(this.firstImgPath);
        dest.writeInt(this.num);
    }

    public ImageFolderBean()
    {
    }

    protected ImageFolderBean(Parcel in)
    {
        this.floderId = in.readString();
        this.floderName = in.readString();
        this.firstImgPath = in.readString();
        this.num = in.readInt();
    }

    public static final Creator<ImageFolderBean> CREATOR = new Creator<ImageFolderBean>()
    {
        @Override
        public ImageFolderBean createFromParcel(Parcel source)
        {
            return new ImageFolderBean(source);
        }

        @Override
        public ImageFolderBean[] newArray(int size)
        {
            return new ImageFolderBean[size];
        }
    };
}

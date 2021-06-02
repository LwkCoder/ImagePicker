package com.lwkandroid.imagepicker.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:图片文件夹实体类
 */
public class ImageFolderBean implements Parcelable
{
    private String folderId;
    private String folderName;
    private String firstImgPath;
    private int num;

    public ImageFolderBean(String folderId)
    {
        this.folderId = folderId;
    }

    public ImageFolderBean(String folderId, String folderName)
    {
        this.folderId = folderId;
        this.folderName = folderName;
    }

    public String getFolderId()
    {
        return folderId;
    }

    public void setFolderId(String folderId)
    {
        this.folderId = folderId;
    }

    public String getFolderName()
    {
        return folderName;
    }

    public void setFolderName(String folderName)
    {
        this.folderName = folderName;
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
                "folderId='" + folderId + '\'' +
                ", folderName='" + folderName + '\'' +
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
        return (obj instanceof ImageFolderBean && ((ImageFolderBean) obj).getFolderId().equals(folderId));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.folderId);
        dest.writeString(this.folderName);
        dest.writeString(this.firstImgPath);
        dest.writeInt(this.num);
    }

    public ImageFolderBean()
    {
    }

    protected ImageFolderBean(Parcel in)
    {
        this.folderId = in.readString();
        this.folderName = in.readString();
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

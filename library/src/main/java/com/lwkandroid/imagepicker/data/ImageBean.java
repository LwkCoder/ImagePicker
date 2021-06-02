package com.lwkandroid.imagepicker.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:图片实体类
 */
public class ImageBean implements Parcelable
{
    /**
     * 图片id【扫描sd卡后才有的】
     */
    private String imageId;
    /**
     * 原图地址
     */
    private String imagePath;
    /**
     * 最后修改时间
     */
    private Long lastModified;
    /**
     * 图片宽
     */
    private int width;
    /**
     * 图片高
     */
    private int height;
    /**
     * 所在文件夹的id【扫描sd卡后才有的】
     */
    private String folderId;

    public ImageBean()
    {
    }

    public String getImageId()
    {
        return imageId;
    }

    public void setImageId(String imageId)
    {
        this.imageId = imageId;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public Long getLastModified()
    {
        return lastModified;
    }

    public void setLastModified(Long lastModified)
    {
        this.lastModified = lastModified;
    }

    public String getFolderId()
    {
        return folderId;
    }

    public void setFolderId(String folderId)
    {
        this.folderId = folderId;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    @Override
    public String toString()
    {
        return "ImageBean{" +
                "imageId='" + imageId + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", lastModified=" + lastModified +
                ", width=" + width +
                ", height=" + height +
                ", folderId='" + folderId + '\'' +
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
        return (obj instanceof ImageBean && ((ImageBean) obj).getImageId().equals(imageId));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.imageId);
        dest.writeString(this.imagePath);
        dest.writeValue(this.lastModified);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.folderId);
    }

    protected ImageBean(Parcel in)
    {
        this.imageId = in.readString();
        this.imagePath = in.readString();
        this.lastModified = (Long) in.readValue(Long.class.getClassLoader());
        this.width = in.readInt();
        this.height = in.readInt();
        this.folderId = in.readString();
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>()
    {
        @Override
        public ImageBean createFromParcel(Parcel source)
        {
            return new ImageBean(source);
        }

        @Override
        public ImageBean[] newArray(int size)
        {
            return new ImageBean[size];
        }
    };
}

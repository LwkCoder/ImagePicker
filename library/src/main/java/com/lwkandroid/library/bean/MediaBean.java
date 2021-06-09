package com.lwkandroid.library.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lwkandroid.library.constants.ImageMimeType;

import java.util.Objects;

/**
 * @description: 媒体文件实体类
 * @author: LWK
 * @date: 2021/6/7 9:26
 */
public class MediaBean implements Parcelable
{
    private String id;
    private String name;
    private String path;
    private String mimeType;
    private int width;
    private int height;
    private String modifyDate;
    private long size;
    private String bucketId;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
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

    public String getModifyDate()
    {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate)
    {
        this.modifyDate = modifyDate;
    }

    public String getBucketId()
    {
        return bucketId;
    }

    public void setBucketId(String bucketId)
    {
        this.bucketId = bucketId;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        MediaBean mediaBean = (MediaBean) o;
        return Objects.equals(id, mediaBean.id);
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    public boolean isGif()
    {
        return ImageMimeType.MIME_TYPE_GIF.equalsIgnoreCase(mimeType);
    }

    @Override
    public String toString()
    {
        return "MediaBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", modifyTime=" + modifyDate +
                ", size=" + size +
                ", bucketId='" + bucketId + '\'' +
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
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.mimeType);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.modifyDate);
        dest.writeLong(this.size);
        dest.writeString(this.bucketId);
    }

    public MediaBean()
    {
    }

    protected MediaBean(Parcel in)
    {
        this.id = in.readString();
        this.name = in.readString();
        this.path = in.readString();
        this.mimeType = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.modifyDate = in.readString();
        this.size = in.readLong();
        this.bucketId = in.readString();
    }

    public static final Creator<MediaBean> CREATOR = new Creator<MediaBean>()
    {
        @Override
        public MediaBean createFromParcel(Parcel source)
        {
            return new MediaBean(source);
        }

        @Override
        public MediaBean[] newArray(int size)
        {
            return new MediaBean[size];
        }
    };
}

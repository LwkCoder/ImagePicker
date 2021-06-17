package com.lwkandroid.imagepicker.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * @description: 文件夹实体类
 * @author: LWK
 * @date: 2021/6/7 10:21
 */
public class BucketBean implements Parcelable
{
    private long bucketId = -1;
    private String name;
    private String firstImagePath;
    private long fileNumber;

    public long getBucketId()
    {
        return bucketId;
    }

    public void setBucketId(long bucketId)
    {
        this.bucketId = bucketId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getFirstImagePath()
    {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath)
    {
        this.firstImagePath = firstImagePath;
    }

    public long getFileNumber()
    {
        return fileNumber;
    }

    public void setFileNumber(long fileNumber)
    {
        this.fileNumber = fileNumber;
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
        BucketBean that = (BucketBean) o;
        return bucketId == that.bucketId;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(bucketId);
    }

    @Override
    public String toString()
    {
        return "BucketBean{" +
                "bucketId=" + bucketId +
                ", name='" + name + '\'' +
                ", firstImagePath='" + firstImagePath + '\'' +
                ", fileNumber=" + fileNumber +
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
        dest.writeLong(this.bucketId);
        dest.writeString(this.name);
        dest.writeString(this.firstImagePath);
        dest.writeLong(this.fileNumber);
    }

    public BucketBean()
    {
    }

    protected BucketBean(Parcel in)
    {
        this.bucketId = in.readLong();
        this.name = in.readString();
        this.firstImagePath = in.readString();
        this.fileNumber = in.readLong();
    }

    public static final Parcelable.Creator<BucketBean> CREATOR = new Parcelable.Creator<BucketBean>()
    {
        @Override
        public BucketBean createFromParcel(Parcel source)
        {
            return new BucketBean(source);
        }

        @Override
        public BucketBean[] newArray(int size)
        {
            return new BucketBean[size];
        }
    };
}

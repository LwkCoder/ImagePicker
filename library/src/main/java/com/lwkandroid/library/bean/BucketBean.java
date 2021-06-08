package com.lwkandroid.library.bean;

import java.util.List;
import java.util.Objects;

/**
 * @description: 文件夹实体类
 * @author: LWK
 * @date: 2021/6/7 10:21
 */
public class BucketBean
{
    private long bucketId = -1;
    private String name;
    private String firstImagePath;
    private String firstMimeType;
    private long fileNumber;
    private List<MediaBean> mediaList;

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

    public String getFirstMimeType()
    {
        return firstMimeType;
    }

    public void setFirstMimeType(String firstMimeType)
    {
        this.firstMimeType = firstMimeType;
    }

    public long getFileNumber()
    {
        return fileNumber;
    }

    public void setFileNumber(long fileNumber)
    {
        this.fileNumber = fileNumber;
    }

    public List<MediaBean> getMediaList()
    {
        return mediaList;
    }

    public void setMediaList(List<MediaBean> mediaList)
    {
        this.mediaList = mediaList;
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
                ", firstMimeType='" + firstMimeType + '\'' +
                ", fileNumber=" + fileNumber +
                ", mediaList=" + mediaList +
                '}';
    }
}

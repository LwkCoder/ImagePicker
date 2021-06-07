package com.lwkandroid.library.bean;

import com.lwkandroid.library.constants.ImageMimeType;

/**
 * @description: 媒体文件实体类
 * @author: LWK
 * @date: 2021/6/7 9:26
 */
public class MediaBean
{
    private String id;
    private String name;
    private String mimeType;
    private int width;
    private int height;
    private long lastModifyTime;
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

    public long getLastModifyTime()
    {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime)
    {
        this.lastModifyTime = lastModifyTime;
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

    public boolean isGif()
    {
        return ImageMimeType.MIME_TYPE_GIF.equalsIgnoreCase(mimeType);
    }
}

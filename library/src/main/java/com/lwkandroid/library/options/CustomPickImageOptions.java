package com.lwkandroid.library.options;

import com.lwkandroid.library.constants.PickMimeType;
import com.lwkandroid.library.custom.CustomPickImageRequestImpl;
import com.lwkandroid.library.custom.ICustomPickImageRequest;

/**
 * @description: 自定义选择图片的配置
 * @author: LWK
 * @date: 2021/6/7 14:10
 */
public class CustomPickImageOptions
{
    private int maxPickNumber = 1;
    private long fileMinSize = 0;
    private long fileMaxSize = Long.MAX_VALUE;
    private String[] mimeTypeArray = PickMimeType.ARRAY_NO_LIMIT;

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

        public ICustomPickImageRequest build()
        {
            return new CustomPickImageRequestImpl(mOptions);
        }
    }
}

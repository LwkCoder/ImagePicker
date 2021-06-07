package com.lwkandroid.library.options;

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
    private String[] selectionArgs;

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

    public String[] getSelectionArgs()
    {
        return selectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs)
    {
        this.selectionArgs = selectionArgs;
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
    }
}

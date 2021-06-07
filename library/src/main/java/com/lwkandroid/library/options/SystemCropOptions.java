package com.lwkandroid.library.options;

import com.lwkandroid.library.system.crop.ISystemCropRequest;
import com.lwkandroid.library.system.crop.SystemCropRequestImpl;

import java.io.File;

/**
 * @description: 系统裁剪的配置
 * @author: LWK
 * @date: 2021/6/3 9:21
 */
public class SystemCropOptions
{
    private File imageFile;
    private String cacheDirPath;
    /**
     * 裁剪框宽度比例
     */
    private int aspectX = 1;
    /**
     * 裁剪框高度比例
     */
    private int aspectY = 1;
    /**
     * 输出图片宽度尺寸
     */
    private int outputX = 100;
    /**
     * 输出图片高度尺寸
     */
    private int outputY = 100;

    public File getImageFile()
    {
        return imageFile;
    }

    public void setImageFile(File imageFile)
    {
        this.imageFile = imageFile;
    }

    public int getAspectX()
    {
        return aspectX;
    }

    public void setAspectX(int aspectX)
    {
        this.aspectX = aspectX;
    }

    public int getAspectY()
    {
        return aspectY;
    }

    public void setAspectY(int aspectY)
    {
        this.aspectY = aspectY;
    }

    public int getOutputX()
    {
        return outputX;
    }

    public void setOutputX(int outputX)
    {
        this.outputX = outputX;
    }

    public int getOutputY()
    {
        return outputY;
    }

    public void setOutputY(int outputY)
    {
        this.outputY = outputY;
    }

    public String getCacheDirPath()
    {
        return cacheDirPath;
    }

    public void setCacheDirPath(String cacheDirPath)
    {
        this.cacheDirPath = cacheDirPath;
    }

    public static class Builder
    {
        private SystemCropOptions mOptions;

        public Builder()
        {
            this.mOptions = new SystemCropOptions();
        }

        public Builder setImageFile(File imageFile)
        {
            mOptions.setImageFile(imageFile);
            return this;
        }

        public Builder setAspectX(int aspectX)
        {
            mOptions.setAspectX(aspectX);
            return this;
        }

        public Builder setAspectY(int aspectY)
        {
            mOptions.setAspectY(aspectY);
            return this;
        }

        public Builder setOutputX(int outputX)
        {
            mOptions.setOutputX(outputX);
            return this;
        }

        public Builder setOutputY(int outputY)
        {
            mOptions.setOutputY(outputY);
            return this;
        }

        public Builder setCacheDirPath(String cacheDirPath)
        {
            mOptions.setCacheDirPath(cacheDirPath);
            return this;
        }

        public ISystemCropRequest build()
        {
            return new SystemCropRequestImpl(mOptions);
        }
    }
}

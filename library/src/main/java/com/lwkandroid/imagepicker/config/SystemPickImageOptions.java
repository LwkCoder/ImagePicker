package com.lwkandroid.imagepicker.config;

import com.lwkandroid.imagepicker.system.pick.ISystemPickImageRequest;
import com.lwkandroid.imagepicker.system.pick.SystemPickImageRequestImpl;

/**
 * @description: 选择系统相册图片的相关配置
 * @author:
 * @date: 2021/6/2 13:52
 */
public class SystemPickImageOptions
{
    private int maxPickNumber = 1;

    public int getMaxPickNumber()
    {
        return maxPickNumber;
    }

    public void setMaxPickNumber(int maxPickNumber)
    {
        this.maxPickNumber = maxPickNumber;
    }

    public static class Builder
    {
        private SystemPickImageOptions mOptions;

        public Builder()
        {
            mOptions = new SystemPickImageOptions();
        }

        public Builder setMaxPickNumber(int maxNumber)
        {
            mOptions.setMaxPickNumber(maxNumber);
            return this;
        }

        public ISystemPickImageRequest build()
        {
            return new SystemPickImageRequestImpl(mOptions);
        }
    }
}

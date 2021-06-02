package com.lwkandroid.library.bean;

import com.lwkandroid.library.system.pick.ISystemPickImageRequest;
import com.lwkandroid.library.system.pick.SystemPickImageRequestImpl;

/**
 * @description: 选择系统相册图片的相关配置
 * @author:
 * @date: 2021/6/2 13:52
 */
public class SystemPickImageOptions
{
    private int maxNumber = 1;

    public int getMaxNumber()
    {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber)
    {
        this.maxNumber = maxNumber;
    }

    public static class Builder
    {
        private SystemPickImageOptions mOptions;

        public Builder()
        {
            mOptions = new SystemPickImageOptions();
        }

        public Builder setMaxNumber(int maxNumber)
        {
            mOptions.setMaxNumber(maxNumber);
            return this;
        }

        public ISystemPickImageRequest build()
        {
            return new SystemPickImageRequestImpl(mOptions);
        }
    }
}

package com.lwkandroid.imagepicker.options;

import com.lwkandroid.imagepicker.system.photograph.ISystemPhotographRequest;
import com.lwkandroid.imagepicker.system.photograph.SystemPhotographRequestImpl;

/**
 * @description: 系统拍照相关配置
 * @author: LWK
 * @date: 2021/6/1 15:20
 */
public class SystemPhotographOptions
{
    private String cacheDirPath;

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
        private SystemPhotographOptions options;

        public Builder()
        {
            this.options = new SystemPhotographOptions();
        }

        public Builder setCacheDirPath(String path)
        {
            options.setCacheDirPath(path);
            return this;
        }

        public ISystemPhotographRequest build()
        {
            return new SystemPhotographRequestImpl(options);
        }
    }
}
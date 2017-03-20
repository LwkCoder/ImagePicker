package com.lwkandroid.imagepicker.data;

import com.lwkandroid.imagepicker.utils.ImagePickerComUtils;

/**
 * 常量
 */
public class Contants
{
    /**
     * 界面跳转options的键值
     */
    public static final String INTENT_OPTIONS_KEY = "options";

    /**
     * 界面跳转resultCode的键值
     */
    public static final String INTENT_RESULTCODE_KEY = "resultCode";

    /**
     * “所有图片”文件夹的id
     */
    public static final String ID_ALL_IMAGE_FLODER = "-100";

    /**
     * 默认缓存路径
     */
    public static final String DEF_CACHE_PATH = ImagePickerComUtils.getSdPath() + "/";

    /**
     * 裁剪的默认宽高比
     */
    public static final float DEF_CROP_RATIO = 1.0f;

    /**
     * 裁剪的默认最大宽度
     */
    public static final int DEF_CROP_MAX_WIDTH = 100;

    /**
     * 展示小图时最大分辨率
     */
    public static final int DISPLAY_THUMB_SIZE = 300;
}

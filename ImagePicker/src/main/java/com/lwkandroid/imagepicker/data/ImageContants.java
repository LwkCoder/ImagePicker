package com.lwkandroid.imagepicker.data;

import com.lwkandroid.imagepicker.utils.ImagePickerComUtils;

/**
 * 常量
 */
public class ImageContants
{
    /**
     * 界面跳转options的键值
     */
    public static final String INTENT_KEY_OPTIONS = "options";

    /**
     * 界面跳转resultCode的键值
     */
    public static final String INTENT_KEY_RESULTCODE = "resultCode";

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

    /**
     * sdk23获取sd卡读写权限的requestCode
     */
    public static final int REQUEST_CODE_PERMISSION_SDCARD = 110;

    /**
     * sdk23获取sd卡拍照权限的requestCode
     */
    public static final int REQUEST_CODE_PERMISSION_CAMERA = 111;

    /**
     * 拍照请求码
     */
    public static final int REQUEST_CODE_TAKE_PHOTO = 112;

    /**
     * 裁剪请求码
     */
    public static final int REQUEST_CODE_CROP = 113;

    /**
     * 预览请求码
     */
    public static final int REQUEST_CODE_PREVIEW = 114;

    /**
     * 看大图请求码
     */
    public static final int REQUEST_CODE_DETAIL = 115;

    /**
     * 传递数据的key
     */
    public static final String INTENT_KEY_DATA = "dataList";

    /**
     * 传递起始位置的key
     */
    public static final String INTENT_KEY_START_POSITION = "startP";

    /**
     * 传递Pager页面中是否为预览模式的key
     */
    public static final String INTENT_KEY_IS_PREVIEW = "isPreview";

    /**
     * 点击完成/确定后的ResultCode
     */
    public static final int RESULT_CODE_OK = 123;
}

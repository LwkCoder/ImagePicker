package com.lwkandroid.imagepicker.data;

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
     * 传递起始位置的key
     */
    public static final String INTENT_KEY_START_POSITION = "startP";

    /**
     * 传递Pager页面中是否为预览模式的key
     */
    public static final String INTENT_KEY_IS_PREVIEW = "isPreview";

    /**
     * 传递待裁剪图片路径的key
     */
    public static final String INTENT_KEY_ORIGIN_PATH = "originPath";

    /**
     * 裁剪后图片路径的key
     */
    public static final String INTENT_KEY_CROP_PATH = "cropPath";

    /**
     * “所有图片”文件夹的id
     */
    public static final String ID_ALL_IMAGE_FOLDER = "-100";

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
     * 拍照后图片名字前缀
     */
    public static final String PHOTO_NAME_PREFIX = "IMG_";

    /**
     * 裁剪后图片名字前缀
     */
    public static final String CROP_NAME_PREFIX = "CROP_";

    /**
     * 图片文件名后缀
     */
    public static final String IMG_NAME_POSTFIX = ".jpg";
}

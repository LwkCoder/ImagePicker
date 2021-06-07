package com.lwkandroid.library.constants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * @description: 图片MimeType选择模式
 * @author: LWK
 * @date: 2021/6/7 15:01
 */
public class PickMimeType
{
    public static final int ALL_IMAGE = 0;
    public static final int EXCEPT_GIF = 1;

    public static final String[] PICK_TYPE_ALL_IMAGE = new String[]{ImageMimeType.MIME_TYPE_PNG,
            ImageMimeType.MIME_TYPE_JPG, ImageMimeType.MIME_TYPE_JPEG, ImageMimeType.MIME_TYPE_GIF};

    public static final String[] PICK_TYPE_EXCEPT_GIF = new String[]{ImageMimeType.MIME_TYPE_PNG,
            ImageMimeType.MIME_TYPE_JPG, ImageMimeType.MIME_TYPE_JPEG};

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ALL_IMAGE, EXCEPT_GIF})
    public @interface Type
    {

    }

    public static String[] getSelectionArgByType(@Type int type)
    {
        if (ALL_IMAGE == type)
        {
            return PICK_TYPE_ALL_IMAGE;
        } else if (EXCEPT_GIF == type)
        {
            return PICK_TYPE_EXCEPT_GIF;
        }
        return PICK_TYPE_ALL_IMAGE;
    }
}

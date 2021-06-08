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
    public static final int NO_LIMIT = 0;
    public static final int REGULAR = 1;
    public static final int EXCEPT_GIF = 2;

    public static final String[] ARRAY_NO_LIMIT = new String[]{ImageMimeType.MIME_TYPE_PNG, ImageMimeType.MIME_TYPE_JPG
            , ImageMimeType.MIME_TYPE_JPEG, ImageMimeType.MIME_TYPE_BMP, ImageMimeType.MIME_TYPE_WEBP, ImageMimeType.MIME_TYPE_GIF};

    public static final String[] ARRAY_REGULAR = new String[]{ImageMimeType.MIME_TYPE_PNG, ImageMimeType.MIME_TYPE_JPG, ImageMimeType.MIME_TYPE_JPEG};

    public static final String[] ARRAY_EXCEPT_GIF = new String[]{ImageMimeType.MIME_TYPE_PNG, ImageMimeType.MIME_TYPE_JPG
            , ImageMimeType.MIME_TYPE_JPEG, ImageMimeType.MIME_TYPE_BMP, ImageMimeType.MIME_TYPE_WEBP};

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NO_LIMIT, REGULAR, EXCEPT_GIF})
    public @interface Type
    {

    }

    public static String[] getMimeTypeArrayByType(@Type int type)
    {
        if (NO_LIMIT == type)
        {
            return ARRAY_NO_LIMIT;
        } else if (REGULAR == type)
        {
            return ARRAY_REGULAR;
        } else if (EXCEPT_GIF == type)
        {
            return ARRAY_EXCEPT_GIF;
        }
        return ARRAY_NO_LIMIT;
    }
}

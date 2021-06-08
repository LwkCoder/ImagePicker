package com.lwkandroid.library.custom;

import android.content.Context;
import android.provider.MediaStore;
import android.util.Log;

import com.lwkandroid.library.bean.BucketBean;
import com.lwkandroid.library.bean.MediaBean;
import com.lwkandroid.library.callback.PickCallBack;
import com.lwkandroid.library.constants.ImageConstants;
import com.lwkandroid.library.options.CustomPickImageOptions;
import com.lwkandroid.library.utils.ThreadUtils;
import com.lwkandroid.library.utils.Utils;

import java.util.List;

/**
 * @description: 媒体数据加载接口
 * @author: LWK
 * @date: 2021/6/7 9:23
 */
public class MediaLoaderEngine
{
    private final String ORDER_BY = MediaStore.Images.Media.DATE_MODIFIED + " desc";

    public void loadAllBucket(Context context, CustomPickImageOptions options, PickCallBack<List<BucketBean>> callBack)
    {
        StringBuilder selectionBuilder = new StringBuilder();
        //拼接MimeType的限制
        appendMimeTypeSelection(selectionBuilder, options.getMimeTypeArray());
        //拼接FileSize的限制
        appendFileSizeSelection(selectionBuilder, options.getFileMinSize(), options.getFileMaxSize());

        Log.e("AAA", "--->" + selectionBuilder.toString());

        ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<List<BucketBean>>()
        {
            @Override
            public List<BucketBean> doInBackground() throws Throwable
            {
                IMediaLoaderEngine loaderEngine = Utils.checkAndroidQ() ? new AndroidQLoaderImpl() : new LegacyLoaderImpl();
                return loaderEngine.loadMediaData(context, selectionBuilder.toString(), null, ORDER_BY);
            }

            @Override
            public void onSuccess(List<BucketBean> result)
            {
                if (callBack != null)
                {
                    callBack.onPickSuccess(result);
                }
            }
        });
    }

    public void loadPageImage(Context context, CustomPickImageOptions options, long bucketId,
                              int pageIndex, int pageSize, PickCallBack<List<MediaBean>> callBack)
    {
        StringBuilder selectionBuilder = new StringBuilder();
        //拼接MimeType的限制
        appendMimeTypeSelection(selectionBuilder, options.getMimeTypeArray());
        //拼接FileSize的限制
        appendFileSizeSelection(selectionBuilder, options.getFileMinSize(), options.getFileMaxSize());
        //凭借BucketId的限制
        appendBucketIdSelection(selectionBuilder, bucketId);

        Log.e("BBB", "--->" + selectionBuilder.toString());

        ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<List<MediaBean>>()
        {
            @Override
            public List<MediaBean> doInBackground() throws Throwable
            {
                IMediaLoaderEngine loaderEngine = Utils.checkAndroidQ() ? new AndroidQLoaderImpl() : new LegacyLoaderImpl();
                return null;
            }

            @Override
            public void onSuccess(List<MediaBean> result)
            {
                if (callBack != null)
                {
                    callBack.onPickSuccess(result);
                }
            }
        });
    }

    private void appendMimeTypeSelection(StringBuilder stringBuilder, String[] mimeTypeArray)
    {
        stringBuilder.append(ImageConstants.SPACE)
                .append(ImageConstants.LEFT_BRACKET);
        for (int i = 0; i < mimeTypeArray.length; i++)
        {
            stringBuilder.append(MediaStore.Images.Media.MIME_TYPE)
                    .append(ImageConstants.EQUALS)
                    .append(ImageConstants.SINGLE_QUOTES)
                    .append(mimeTypeArray[i])
                    .append(ImageConstants.SINGLE_QUOTES);
            if (i < mimeTypeArray.length - 1)
            {
                stringBuilder.append(ImageConstants.SPACE)
                        .append(ImageConstants.OR)
                        .append(ImageConstants.SPACE);
            }
        }
        stringBuilder.append(ImageConstants.RIGHT_BRACKET)
                .append(ImageConstants.SPACE);
    }

    private void appendFileSizeSelection(StringBuilder stringBuilder, long minFileSize, long maxFileSize)
    {
        stringBuilder.append(ImageConstants.SPACE)
                .append(ImageConstants.AND)
                .append(ImageConstants.SPACE)
                .append(ImageConstants.LEFT_BRACKET)
                .append(Math.max(0, minFileSize))
                .append(ImageConstants.SPACE)
                .append(ImageConstants.LESS_THAN)
                .append(ImageConstants.EQUALS)
                .append(ImageConstants.SPACE)
                .append(MediaStore.MediaColumns.SIZE)
                .append(ImageConstants.SPACE)
                .append(ImageConstants.AND)
                .append(ImageConstants.SPACE)
                .append(MediaStore.MediaColumns.SIZE)
                .append(ImageConstants.SPACE)
                .append(ImageConstants.LESS_THAN)
                .append(ImageConstants.EQUALS)
                .append(ImageConstants.SPACE)
                .append(Math.max(0, maxFileSize))
                .append(ImageConstants.RIGHT_BRACKET)
                .append(ImageConstants.SPACE);
    }

    private void appendBucketIdSelection(StringBuilder stringBuilder, long bucketId)
    {
        if (ImageConstants.BUCKET_ID_ALL_IMAGE == bucketId)
        {
            return;
        }

        stringBuilder.append(ImageConstants.SPACE)
                .append(ImageConstants.AND)
                .append(ImageConstants.SPACE)
                .append(ImageConstants.LEFT_BRACKET)
                .append(MediaStore.Images.Media.BUCKET_ID)
                .append(ImageConstants.EQUALS)
                .append(bucketId)
                .append(ImageConstants.RIGHT_BRACKET)
                .append(ImageConstants.SPACE);
    }
}

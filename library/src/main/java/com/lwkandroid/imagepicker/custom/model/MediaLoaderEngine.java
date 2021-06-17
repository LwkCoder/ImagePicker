package com.lwkandroid.imagepicker.custom.model;

import android.content.Context;
import android.provider.MediaStore;

import com.lwkandroid.imagepicker.bean.BucketBean;
import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.config.CustomPickImageOptions;
import com.lwkandroid.imagepicker.constants.ImageConstants;
import com.lwkandroid.imagepicker.utils.ThreadUtils;
import com.lwkandroid.imagepicker.utils.Utils;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * @description: 数据加载
 * @author: LWK
 * @date: 2021/6/7 9:23
 */
public class MediaLoaderEngine
{
    private final String ORDER_BY = MediaStore.Images.Media.DATE_MODIFIED + ImageConstants.SPACE + ImageConstants.DESC;
    private static final String COLUMN_BUCKET_ID = "bucket_id";

    public void loadAllBucket(@NonNull Context context, @NonNull LifecycleOwner lifecycleOwner,
                              @NonNull CustomPickImageOptions options, PickCallBack<List<BucketBean>> callBack)
    {
        StringBuilder selectionBuilder = new StringBuilder();
        //拼接MimeType的限制
        appendMimeTypeSelection(selectionBuilder, options.getMimeTypeArray());
        //拼接FileSize的限制
        appendFileSizeSelection(selectionBuilder, options.getFileMinSize(), options.getFileMaxSize());

        ThreadUtils.SimpleTask<List<BucketBean>> task = new ThreadUtils.SimpleTask<List<BucketBean>>()
        {
            @Override
            public List<BucketBean> doInBackground() throws Throwable
            {
                IMediaLoaderEngine loaderEngine = Utils.checkAndroidQ() ? new AndroidQLoaderImpl() : new LegacyLoaderImpl();

                return loaderEngine.loadAllBuckets(context, selectionBuilder.toString(), null, ORDER_BY);
            }

            @Override
            public void onSuccess(List<BucketBean> result)
            {
                if (callBack != null)
                {
                    callBack.onPickSuccess(result);
                }
            }
        };

        lifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver()
        {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event)
            {
                if (Lifecycle.Event.ON_DESTROY == event)
                {
                    ThreadUtils.cancel(task);
                    source.getLifecycle().removeObserver(this);
                }
            }
        });

        ThreadUtils.executeBySingle(task);
    }

    public void loadPageImage(@NonNull Context context, @NonNull LifecycleOwner lifecycleOwner,
                              @NonNull CustomPickImageOptions options, long bucketId,
                              @IntRange(from = 1, to = Integer.MAX_VALUE) int pageIndex,
                              @IntRange(from = 1, to = Integer.MAX_VALUE) int pageSize,
                              PickCallBack<List<MediaBean>> callBack)
    {
        StringBuilder selectionBuilder = new StringBuilder();
        //拼接MimeType的限制
        appendMimeTypeSelection(selectionBuilder, options.getMimeTypeArray());
        //拼接FileSize的限制
        appendFileSizeSelection(selectionBuilder, options.getFileMinSize(), options.getFileMaxSize());
        //凭借BucketId的限制
        appendBucketIdSelection(selectionBuilder, bucketId);

        ThreadUtils.SimpleTask<List<MediaBean>> task = new ThreadUtils.SimpleTask<List<MediaBean>>()
        {
            @Override
            public List<MediaBean> doInBackground() throws Throwable
            {
                IMediaLoaderEngine loaderEngine = Utils.checkAndroidQ() ? new AndroidQLoaderImpl() : new LegacyLoaderImpl();
                return loaderEngine.loadPageMediaData(context, selectionBuilder.toString(), null, ORDER_BY, pageIndex, pageSize);
            }

            @Override
            public void onSuccess(List<MediaBean> result)
            {
                if (callBack != null)
                {
                    callBack.onPickSuccess(result);
                }
            }
        };

        lifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver()
        {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event)
            {
                if (Lifecycle.Event.ON_DESTROY == event)
                {
                    ThreadUtils.cancel(task);
                    source.getLifecycle().removeObserver(this);
                }
            }
        });

        ThreadUtils.executeBySingle(task);
    }

    private void appendMimeTypeSelection(StringBuilder stringBuilder, String[] mimeTypeArray)
    {
        stringBuilder.append(ImageConstants.SPACE)
                .append(ImageConstants.LEFT_BRACKET);
        for (int i = 0; i < mimeTypeArray.length; i++)
        {
            stringBuilder.append(MediaStore.Images.Media.MIME_TYPE)
                    .append(ImageConstants.EQUAL)
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
                .append(ImageConstants.EQUAL)
                .append(ImageConstants.SPACE)
                .append(MediaStore.MediaColumns.SIZE)
                .append(ImageConstants.SPACE)
                .append(ImageConstants.AND)
                .append(ImageConstants.SPACE)
                .append(MediaStore.MediaColumns.SIZE)
                .append(ImageConstants.SPACE)
                .append(ImageConstants.LESS_THAN)
                .append(ImageConstants.EQUAL)
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
                .append(COLUMN_BUCKET_ID)
                .append(ImageConstants.EQUAL)
                .append(bucketId)
                .append(ImageConstants.RIGHT_BRACKET)
                .append(ImageConstants.SPACE);
    }
}
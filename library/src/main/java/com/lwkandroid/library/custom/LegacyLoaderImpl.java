package com.lwkandroid.library.custom;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.lwkandroid.imagepicker.BuildConfig;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.library.bean.BucketBean;
import com.lwkandroid.library.constants.ImageConstants;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @description: AndroidQ以前的扫描实现
 * @author: LWK
 * @date: 2021/6/7 15:51
 */
final class LegacyLoaderImpl implements IMediaLoaderEngine
{
    private final Uri QUERY_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private final String[] PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
    };

    @Override
    public List<BucketBean> loadMediaData(Context context, String selection, String[] selectionArg, String sortOrder)
    {
        Cursor cursor = context.getContentResolver().query(QUERY_URI, PROJECTION, selection, selectionArg, sortOrder);

        List<BucketBean> resultList = new LinkedList<>();

        Map<Long, BucketBean> buckMap = new HashMap<>();
        long totalFileNumber = 0;
        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                long bucketId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));

                if (buckMap.containsKey(bucketId))
                {
                    BucketBean bucketBean = buckMap.get(bucketId);
                    bucketBean.setFileNumber(bucketBean.getFileNumber() + 1);
                } else
                {
                    BucketBean bucketBean = new BucketBean();
                    bucketBean.setBucketId(bucketId);
                    bucketBean.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
                    bucketBean.setFileNumber(1);
                    bucketBean.setFirstImagePath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    bucketBean.setFirstMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)));
                    buckMap.put(bucketId, bucketBean);
                }
                totalFileNumber++;
            } while (cursor.moveToNext());
        }

        //创建“全部图片”的文件夹
        BucketBean allImageBucket = new BucketBean();
        allImageBucket.setBucketId(ImageConstants.BUCKET_ID_ALL_IMAGE);
        allImageBucket.setName(context.getResources().getString(R.string.bucket_all_picture));
        allImageBucket.setFileNumber(totalFileNumber);
        if (cursor != null && cursor.moveToFirst())
        {
            allImageBucket.setFirstImagePath(getFirstImagePath(cursor));
            allImageBucket.setFirstMimeType(getFirstImageMimeType(cursor));
        }

        resultList.add(allImageBucket);
        resultList.addAll(buckMap.values());

        if (BuildConfig.DEBUG)
        {
            for (BucketBean bucketBean : resultList)
            {
                Log.i("LegacyLoaderImpl", "bucketBean->" + bucketBean.toString());
            }
        }

        return resultList;
    }

    private String getFirstImagePath(Cursor cursor)
    {
        return cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
    }

    private String getFirstImageMimeType(Cursor cursor)
    {
        return cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE));
    }

}

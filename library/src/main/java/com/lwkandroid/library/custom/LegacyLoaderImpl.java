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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @description: AndroidQ以前的扫描实现
 * @author: LWK
 * @date: 2021/6/7 15:51
 */
final class LegacyLoaderImpl implements IMediaLoaderEngine
{
    private static final Uri QUERY_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    private static final String GROUP_BY = ImageConstants.RIGHT_BRACKET + ImageConstants.SPACE + ImageConstants.GROUP_BY
            + ImageConstants.SPACE + ImageConstants.LEFT_BRACKET + MediaStore.Images.Media.BUCKET_ID;

    private static final String COLUMN_COUNT = "count";

    private static final String[] PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            "COUNT(*) AS " + COLUMN_COUNT
    };

    @Override
    public List<BucketBean> loadMediaData(Context context, String selection, String[] selectionArg, String sortOrder)
    {
        selection += GROUP_BY;
        Cursor cursor = context.getContentResolver().query(QUERY_URI, PROJECTION, selection, selectionArg, sortOrder);

        List<BucketBean> resultList = new LinkedList<>();
        Set<Long> idSet = new HashSet<>();

        long totalFileNumber = 0;
        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                long bucketId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                if (idSet.contains(bucketId))
                {
                    continue;
                }

                int fileNumber = cursor.getInt(cursor.getColumnIndex(COLUMN_COUNT));
                BucketBean bucketBean = new BucketBean();
                bucketBean.setBucketId(bucketId);
                bucketBean.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
                bucketBean.setFileNumber(fileNumber);
                bucketBean.setFirstImagePath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                bucketBean.setFirstMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)));

                idSet.add(bucketId);
                resultList.add(bucketBean);
                totalFileNumber += fileNumber;
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

        resultList.add(0, allImageBucket);

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

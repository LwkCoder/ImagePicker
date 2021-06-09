package com.lwkandroid.library.custom.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.lwkandroid.imagepicker.BuildConfig;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.library.bean.BucketBean;
import com.lwkandroid.library.bean.MediaBean;
import com.lwkandroid.library.constants.ImageConstants;

import java.io.File;
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
    private static final String COLUMN_COUNT = "count";
    private static final String COLUMN_BUCKET_ID = "bucket_id";
    private static final String COLUMN_BUCKET_DISPLAY_NAME = "bucket_display_name";

    private static final Uri QUERY_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    private static final String GROUP_BY = ImageConstants.RIGHT_BRACKET + ImageConstants.SPACE + ImageConstants.GROUP_BY
            + ImageConstants.SPACE + ImageConstants.LEFT_BRACKET + COLUMN_BUCKET_ID;

    private static final String[] PROJECTION_BUCKET = {
            MediaStore.Images.Media._ID,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            "COUNT(*) AS " + COLUMN_COUNT
    };

    private static final String[] PROJECTION_MEDIA = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.SIZE,
            COLUMN_BUCKET_ID,
    };

    @Override
    public List<BucketBean> loadAllBuckets(Context context, String selection, String[] selectionArg, String sortOrder)
    {
        selection += GROUP_BY;
        Cursor cursor = context.getContentResolver().query(QUERY_URI, PROJECTION_BUCKET, selection, selectionArg, sortOrder);

        List<BucketBean> resultList = new LinkedList<>();
        Set<Long> idSet = new HashSet<>();

        long totalFileNumber = 0;
        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                long bucketId = cursor.getLong(cursor.getColumnIndex(PROJECTION_BUCKET[1]));
                if (idSet.contains(bucketId))
                {
                    continue;
                }

                BucketBean bucketBean = new BucketBean();
                bucketBean.setBucketId(bucketId);
                bucketBean.setName(cursor.getString(cursor.getColumnIndex(PROJECTION_BUCKET[2])));
                bucketBean.setFirstImagePath(cursor.getString(cursor.getColumnIndex(PROJECTION_BUCKET[3])));
                int fileNumber = cursor.getInt(cursor.getColumnIndex(COLUMN_COUNT));
                bucketBean.setFileNumber(fileNumber);

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
            allImageBucket.setFirstImagePath(cursor.getString(cursor.getColumnIndex(PROJECTION_BUCKET[3])));
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

    @Override
    public List<MediaBean> loadPageMediaData(Context context, String selection, String[] selectionArg, String sortOrder, int pageIndex, int pageSize)
    {
        sortOrder = sortOrder + ImageConstants.SPACE + ImageConstants.LIMIT + ImageConstants.SPACE + pageSize + ImageConstants.SPACE
                + ImageConstants.OFFSET + ImageConstants.SPACE + (pageIndex - 1) * pageSize;
        Cursor cursor = context.getContentResolver().query(QUERY_URI, PROJECTION_MEDIA, selection, selectionArg, sortOrder);

        List<MediaBean> resultList = new LinkedList<>();
        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                String path = cursor.getString(cursor.getColumnIndex(PROJECTION_MEDIA[2]));
                if (TextUtils.isEmpty(path) || !new File(path).exists())
                {
                    continue;
                }
                MediaBean mediaBean = new MediaBean();
                mediaBean.setId(cursor.getString(cursor.getColumnIndex(PROJECTION_MEDIA[0])));
                mediaBean.setName(cursor.getString(cursor.getColumnIndex(PROJECTION_MEDIA[1])));
                mediaBean.setPath(path);
                mediaBean.setWidth(cursor.getInt(cursor.getColumnIndex(PROJECTION_MEDIA[3])));
                mediaBean.setHeight(cursor.getInt(cursor.getColumnIndex(PROJECTION_MEDIA[4])));
                mediaBean.setMimeType(cursor.getString(cursor.getColumnIndex(PROJECTION_MEDIA[5])));
                mediaBean.setModifyDate(cursor.getString(cursor.getColumnIndex(PROJECTION_MEDIA[6])));
                mediaBean.setSize(cursor.getLong(cursor.getColumnIndex(PROJECTION_MEDIA[7])));
                mediaBean.setBucketId(cursor.getString(cursor.getColumnIndex(PROJECTION_MEDIA[8])));

                resultList.add(mediaBean);
            } while (cursor.moveToNext());
        }

        if (BuildConfig.DEBUG)
        {
            for (MediaBean mediaBean : resultList)
            {
                Log.i("LegacyLoaderImpl", "mediaBean->" + mediaBean.toString());
            }
        }

        return resultList;
    }

}

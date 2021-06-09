package com.lwkandroid.library.custom.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.lwkandroid.imagepicker.BuildConfig;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.library.bean.BucketBean;
import com.lwkandroid.library.bean.MediaBean;
import com.lwkandroid.library.constants.ImageConstants;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;

/**
 * @description: AndroidQ及以后的扫描实现
 * @author: LWK
 * @date: 2021/6/7 15:51
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
final class AndroidQLoaderImpl implements IMediaLoaderEngine
{
    private static final Uri QUERY_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    private static final String[] PROJECTION_BUCKET = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
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
            MediaStore.Images.Media.BUCKET_ID,
    };


    @Override
    public List<BucketBean> loadAllBuckets(Context context, String selection, String[] selectionArg, String sortOrder)
    {
        Cursor cursor = context.getContentResolver().query(QUERY_URI, PROJECTION_BUCKET, selection, selectionArg, sortOrder);

        List<BucketBean> resultList = new LinkedList<>();

        Map<Long, BucketBean> buckMap = new HashMap<>();
        long totalFileNumber = 0;

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                long bucketId = cursor.getLong(cursor.getColumnIndex(PROJECTION_BUCKET[1]));

                if (buckMap.containsKey(bucketId))
                {
                    BucketBean bucketBean = buckMap.get(bucketId);
                    bucketBean.setFileNumber(bucketBean.getFileNumber() + 1);
                } else
                {
                    BucketBean bucketBean = new BucketBean();
                    bucketBean.setBucketId(bucketId);
                    bucketBean.setName(cursor.getString(cursor.getColumnIndex(PROJECTION_BUCKET[2])));
                    bucketBean.setFileNumber(1);
                    //这里要做兼容
                    String firstImagePath = cursor.getString(cursor.getColumnIndex(PROJECTION_BUCKET[3]));
                    if (TextUtils.isEmpty(firstImagePath) || !(new File(firstImagePath)).exists())
                    {
                        firstImagePath = getRealPathAndroid_Q(bucketId);
                    }
                    bucketBean.setFirstImagePath(firstImagePath);
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
            //这里要做兼容
            String firstImagePath = cursor.getString(cursor.getColumnIndex(PROJECTION_BUCKET[3]));
            if (TextUtils.isEmpty(firstImagePath) || !(new File(firstImagePath)).exists())
            {
                firstImagePath = getRealPathAndroid_Q(cursor.getLong(cursor.getColumnIndex(PROJECTION_BUCKET[0])));
            }
            allImageBucket.setFirstImagePath(firstImagePath);
        }

        resultList.add(allImageBucket);
        resultList.addAll(buckMap.values());

        if (BuildConfig.DEBUG)
        {
            for (BucketBean bucketBean : resultList)
            {
                Log.i("AndroidQLoaderImpl", "bucketBean->" + bucketBean.toString());
            }
        }

        return resultList;
    }

    @Override
    public List<MediaBean> loadPageMediaData(Context context, String selection, String[] selectionArg, String sortOrder, int pageIndex, int pageSize)
    {
        return null;
    }

    private String getRealPathAndroid_Q(long id)
    {
        return QUERY_URI.buildUpon().appendPath(String.valueOf(id)).build().toString();
    }
}

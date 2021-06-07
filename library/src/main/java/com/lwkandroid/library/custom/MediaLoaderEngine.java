package com.lwkandroid.library.custom;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.library.bean.BucketBean;
import com.lwkandroid.library.callback.PickCallBack;
import com.lwkandroid.library.constants.ImageConstants;
import com.lwkandroid.library.options.CustomPickImageOptions;
import com.lwkandroid.library.utils.ThreadUtils;
import com.lwkandroid.library.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @description: 媒体数据加载接口
 * @author: LWK
 * @date: 2021/6/7 9:23
 */
public class MediaLoaderEngine
{
    private static final long FILE_SIZE_UNIT = 1024 * 1024L;
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC";
    private static final String NOT_GIF_UNKNOWN = "!='image/*'";
    private static final String NOT_GIF = " AND (" + MediaStore.MediaColumns.MIME_TYPE + "!='image/gif' AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF_UNKNOWN + ")";
    private static final String GROUP_BY_BUCKET_Id = " GROUP BY (bucket_id";
    private static final String COLUMN_COUNT = "count";
    private static final String COLUMN_BUCKET_ID = "bucket_id";
    private static final String COLUMN_BUCKET_DISPLAY_NAME = "bucket_display_name";

    private static final String[] PROJECTION_29 = {
            MediaStore.Files.FileColumns._ID,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE};

    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            "COUNT(*) AS " + COLUMN_COUNT};


    public void loadAllBucket(Context context, CustomPickImageOptions options, PickCallBack<List<BucketBean>> callBack)
    {

        StringBuilder selectionBuilder = new StringBuilder();

        //拼接MimeType的限制
        selectionBuilder.append(ImageConstants.LEFT_BRACKET);
        String[] mimeTypeArray = options.getMimeTypeArray();
        for (int i = 0; i < mimeTypeArray.length; i++)
        {
            selectionBuilder.append(MediaStore.Images.Media.MIME_TYPE)
                    .append(ImageConstants.EQUALS)
                    .append(ImageConstants.SINGLE_QUOTES)
                    .append(mimeTypeArray[i])
                    .append(ImageConstants.SINGLE_QUOTES);
            if (i < mimeTypeArray.length - 1)
            {
                selectionBuilder.append(ImageConstants.SPACE)
                        .append(ImageConstants.OR)
                        .append(ImageConstants.SPACE);
            }
        }
        selectionBuilder.append(ImageConstants.RIGHT_BRACKET);

        //拼接FileSize的限制
        selectionBuilder.append(ImageConstants.SPACE)
                .append(ImageConstants.AND)
                .append(ImageConstants.SPACE)
                .append(ImageConstants.LEFT_BRACKET)
                .append(Math.max(0, options.getFileMinSize()))
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
                .append(Math.max(0, options.getFileMaxSize()))
                .append(ImageConstants.RIGHT_BRACKET)
        ;

        Log.e("AAA", "--->" + selectionBuilder.toString());

        String selection = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?";
        //只筛选png、jpg、jpeg、PNG、JPG、JPEG
        //        String[] selectionArgs = {"image/png", "image/jpg", "image/jpeg", "image/PNG", "image/JPG", "image/JPEG"};
        String[] selectionArgs = {String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)};

        ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<List<BucketBean>>()
        {
            @Override
            public List<BucketBean> doInBackground() throws Throwable
            {
                Cursor cursor = context.getContentResolver().query(QUERY_URI,
                        Utils.checkAndroidQ() ? PROJECTION_29 : PROJECTION,
                        selectionBuilder.toString(), null, ORDER_BY);
                try
                {
                    if (cursor != null)
                    {
                        int count = cursor.getCount();
                        int totalCount = 0;
                        List<BucketBean> allBucketList = new ArrayList<>();
                        if (count > 0)
                        {
                            if (Utils.checkAndroidQ())
                            {
                                Map<Long, Long> countMap = new HashMap<>();
                                while (cursor.moveToNext())
                                {
                                    long bucketId = cursor.getLong(cursor.getColumnIndex(COLUMN_BUCKET_ID));
                                    Long newCount = countMap.get(bucketId);
                                    if (newCount == null)
                                    {
                                        newCount = 1L;
                                    } else
                                    {
                                        newCount++;
                                    }
                                    countMap.put(bucketId, newCount);
                                }

                                if (cursor.moveToFirst())
                                {
                                    Set<Long> idSet = new HashSet<>();
                                    do
                                    {
                                        long bucketId = cursor.getLong(cursor.getColumnIndex(COLUMN_BUCKET_ID));
                                        if (idSet.contains(bucketId))
                                        {
                                            continue;
                                        }
                                        BucketBean bucketBean = new BucketBean();
                                        bucketBean.setBucketId(bucketId);
                                        String bucketDisplayName = cursor.getString(cursor.getColumnIndex(COLUMN_BUCKET_DISPLAY_NAME));
                                        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
                                        long size = countMap.get(bucketId);
                                        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                                        bucketBean.setName(bucketDisplayName);
                                        bucketBean.setFileNumber(size);
                                        bucketBean.setFirstImagePath(getRealPathAndroid_Q(id));
                                        bucketBean.setFirstMimeType(mimeType);
                                        allBucketList.add(bucketBean);
                                        idSet.add(bucketId);
                                        totalCount += size;
                                    } while (cursor.moveToNext());
                                }

                            } else
                            {
                                cursor.moveToFirst();
                                do
                                {
                                    BucketBean bucketBean = new BucketBean();
                                    long bucketId = cursor.getLong(cursor.getColumnIndex(COLUMN_BUCKET_ID));
                                    String bucketDisplayName = cursor.getString(cursor.getColumnIndex(COLUMN_BUCKET_DISPLAY_NAME));
                                    String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
                                    int size = cursor.getInt(cursor.getColumnIndex(COLUMN_COUNT));
                                    bucketBean.setBucketId(bucketId);
                                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                                    bucketBean.setFirstImagePath(url);
                                    bucketBean.setName(bucketDisplayName);
                                    bucketBean.setFirstMimeType(mimeType);
                                    bucketBean.setFileNumber(size);
                                    allBucketList.add(bucketBean);
                                    totalCount += size;
                                } while (cursor.moveToNext());
                            }

                            sortFolder(allBucketList);

                            //创建“全部图片”的文件夹
                            BucketBean allImageBucket = new BucketBean();
                            allImageBucket.setFileNumber(totalCount);
                            allImageBucket.setBucketId(-1);
                            if (cursor.moveToFirst())
                            {
                                allImageBucket.setFirstImagePath(Utils.checkAndroidQ() ? getFirstUri(cursor) : getFirstUrl(cursor));
                                allImageBucket.setFirstMimeType(getFirstCoverMimeType(cursor));
                            }
                            allImageBucket.setName(context.getResources().getString(R.string.bucket_all_picture));

                            allBucketList.add(0, allImageBucket);
                            return allBucketList;
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    return new ArrayList<>();
                } finally
                {
                    if (cursor != null && !cursor.isClosed())
                    {
                        cursor.close();
                    }
                }
                return new ArrayList<>();
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

    private void sortFolder(List<BucketBean> imageFolders)
    {
        Collections.sort(imageFolders, (lhs, rhs) -> {
            if (lhs.getMediaList() == null || rhs.getMediaList() == null)
            {
                return 0;
            }
            long lSize = lhs.getFileNumber();
            long rSize = rhs.getFileNumber();
            return Long.compare(rSize, lSize);
        });
    }

    private String getFileSizeCondition()
    {
        long maxS = Long.MAX_VALUE;
        return String.format(Locale.CHINA, "%d <%s " + MediaStore.MediaColumns.SIZE + " and " + MediaStore.MediaColumns.SIZE + " <= %d",
                Math.max(0, 1024),
                Math.max(0, 1024) == 0 ? "" : "=",
                maxS);
    }

    private static String getFirstUri(Cursor cursor)
    {
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
        return getRealPathAndroid_Q(id);
    }

    private static String getFirstUrl(Cursor cursor)
    {
        return cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
    }

    private static String getFirstCoverMimeType(Cursor cursor)
    {
        return cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE));
    }

    private static String getRealPathAndroid_Q(long id)
    {
        return QUERY_URI.buildUpon().appendPath(String.valueOf(id)).build().toString();
    }
}

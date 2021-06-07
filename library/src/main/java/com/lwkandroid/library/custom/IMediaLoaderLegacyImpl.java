package com.lwkandroid.library.custom;

import android.content.Context;
import android.provider.MediaStore;

import com.lwkandroid.library.bean.BucketBean;
import com.lwkandroid.library.callback.PickCallBack;

import java.util.List;

/**
 * @description: AndroidQ以前的扫描实现
 * @author: 20180004
 * @date: 2021/6/7 15:51
 */
class IMediaLoaderLegacyImpl implements IMediaLoaderEngine
{
    private final String[] PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE
    };

    @Override
    public void loadMediaData(Context context, String selection, String[] selectionArg, String sortOrder, PickCallBack<List<BucketBean>> callBack)
    {

    }
}

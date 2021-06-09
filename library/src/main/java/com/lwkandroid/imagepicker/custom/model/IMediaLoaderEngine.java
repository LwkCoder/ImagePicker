package com.lwkandroid.imagepicker.custom.model;

import android.content.Context;

import com.lwkandroid.imagepicker.bean.BucketBean;
import com.lwkandroid.imagepicker.bean.MediaBean;

import java.util.List;

import androidx.annotation.IntRange;

/**
 * @description: 媒体数据加载接口
 * @author: LWK
 * @date: 2021/6/7 15:43
 */
interface IMediaLoaderEngine
{
    List<BucketBean> loadAllBuckets(Context context, String selection, String[] selectionArg, String sortOrder);

    List<MediaBean> loadPageMediaData(Context context, String selection, String[] selectionArg, String sortOrder,
                                      @IntRange(from = 1, to = Integer.MAX_VALUE) int pageIndex,
                                      @IntRange(from = 1, to = Integer.MAX_VALUE) int pageSize);
}

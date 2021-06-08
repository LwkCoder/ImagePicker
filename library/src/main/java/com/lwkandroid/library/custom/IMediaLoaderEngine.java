package com.lwkandroid.library.custom;

import android.content.Context;

import com.lwkandroid.library.bean.BucketBean;

import java.util.List;

/**
 * @description: 媒体数据加载接口
 * @author: LWK
 * @date: 2021/6/7 15:43
 */
interface IMediaLoaderEngine
{
    List<BucketBean> loadMediaData(Context context, String selection, String[] selectionArg, String sortOrder);
}

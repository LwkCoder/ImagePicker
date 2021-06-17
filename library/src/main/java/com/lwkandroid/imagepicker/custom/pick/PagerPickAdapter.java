package com.lwkandroid.imagepicker.custom.pick;

import android.content.Context;

import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.ArrayList;

/**
 * @description:
 * @author: LWK
 * @date: 2021/6/17 10:35
 */
class PagerPickAdapter extends RcvSingleAdapter<MediaBean>
{
    public PagerPickAdapter(Context context, int layoutId, int number)
    {
        super(context, layoutId, new ArrayList<>(number));
    }

    @Override
    public void onBindView(RcvHolder holder, MediaBean itemData, int position)
    {

    }
}

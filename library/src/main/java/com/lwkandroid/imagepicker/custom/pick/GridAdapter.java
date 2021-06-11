package com.lwkandroid.imagepicker.custom.pick;

import android.content.Context;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * @description: 网格列表适配器
 * @author: LWK
 * @date: 2021/6/11 15:30
 */
class GridAdapter extends RcvSingleAdapter<MediaBean>
{
    public GridAdapter(Context context, List<MediaBean> datas)
    {
        super(context, R.layout.adapter_image_picker_grid, datas);
    }

    @Override
    public void onBindView(RcvHolder holder, MediaBean itemData, int position)
    {

    }
}

package com.lwkandroid.imagepicker.custom.pick;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.common.PickCommonConfig;
import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * @description: 网格列表适配器
 * @author: LWK
 * @date: 2021/6/11 15:30
 */
final class GridAdapter extends RcvSingleAdapter<MediaBean>
{
    private final int mChildSize;

    public GridAdapter(Context context, List<MediaBean> datas, int childSize)
    {
        super(context, R.layout.adapter_image_picker_grid, datas);
        this.mChildSize = childSize;
    }

    @Override
    protected void onCreateDataViewHolder(RcvHolder holder, ViewGroup parent, int viewType)
    {
        super.onCreateDataViewHolder(holder, parent, viewType);
        ViewGroup.LayoutParams layoutParams = holder.getConvertView().getLayoutParams();
        layoutParams.width = mChildSize;
        layoutParams.height = mChildSize;
        holder.getConvertView().setLayoutParams(layoutParams);
    }

    @Override
    public void onBindView(RcvHolder holder, MediaBean itemData, int position)
    {
        ImageView imageView = holder.findView(R.id.imageView);

        if (PickCommonConfig.getInstance().getImagePickerDisplayer() != null)
        {
            if (itemData.isGif())
            {
                PickCommonConfig.getInstance().getImagePickerDisplayer().displayGifImage(
                        getContext(), itemData.getPath(), imageView);
            } else
            {
                PickCommonConfig.getInstance().getImagePickerDisplayer().displayImage(
                        getContext(), itemData.getPath(), imageView);
            }
        }
    }
}

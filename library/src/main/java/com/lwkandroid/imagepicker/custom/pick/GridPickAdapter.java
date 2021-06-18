package com.lwkandroid.imagepicker.custom.pick;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.config.PickCommonConfig;
import com.lwkandroid.imagepicker.widget.CheckView;
import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.util.Pair;

/**
 * @description: 网格列表适配器
 * @author: LWK
 * @date: 2021/6/11 15:30
 */
final class GridPickAdapter extends RcvSingleAdapter<MediaBean>
{
    private final int mCheckedColor;
    private int mChildSize;
    private boolean mShowCheckView;
    private final Map<String, Pair<Integer, Integer>> mCheckedPositionMap = new HashMap<>();

    public GridPickAdapter(Context context, List<MediaBean> datas, int childSize, int checkedColor, boolean showCheckView)
    {
        super(context, R.layout.adapter_image_picker_grid, datas);
        this.mChildSize = childSize;
        this.mCheckedColor = checkedColor;
        this.mShowCheckView = showCheckView;
    }

    @Override
    public void refreshDatas(List<MediaBean> data)
    {
        super.refreshDatas(data);
        mCheckedPositionMap.clear();
    }

    public void updateChildSize(int size)
    {
        this.mChildSize = size;
        notifyDataSetChanged();
    }

    @Override
    public void onBindView(RcvHolder holder, MediaBean itemData, int position)
    {
        ViewGroup.LayoutParams layoutParams = holder.getConvertView().getLayoutParams();
        layoutParams.width = mChildSize;
        layoutParams.height = mChildSize;
        holder.getConvertView().setLayoutParams(layoutParams);

        ImageView imageView = holder.findView(R.id.imgContent);
        CheckView checkView = holder.findView(R.id.checkView);
        ViewGroup.LayoutParams layoutParams1 = checkView.getLayoutParams();
        layoutParams1.width = mShowCheckView ? mChildSize / 4 : 0;
        layoutParams1.height = mShowCheckView ? mChildSize / 4 : 0;
        checkView.setLayoutParams(layoutParams1);
        checkView.setCheckedColor(mCheckedColor);

        int index = PickTempStorage.getInstance().indexMediaData(itemData);
        boolean checked = index >= 0;
        if (checked)
        {
            checkView.setNumber(index + 1);
            imageView.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            mCheckedPositionMap.put(itemData.getId(), new Pair<>(index, position));
        } else
        {
            imageView.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
        }

        checkView.setChecked(checked, false);
        checkView.setOnCheckedChangeListener((checkView1, isChecked) -> {
            if (isChecked)
            {
                if (PickTempStorage.getInstance().addMediaData(itemData))
                {
                    int newIndex = PickTempStorage.getInstance().indexMediaData(itemData);
                    checkView.setNumber(newIndex + 1);
                    mCheckedPositionMap.put(itemData.getId(), new Pair<>(newIndex, position));
                    notifyItemChanged(position);
                } else
                {
                    checkView1.setChecked(false, false);
                }
            } else
            {
                int oldIndex = PickTempStorage.getInstance().indexMediaData(itemData);
                if (PickTempStorage.getInstance().removeMediaData(itemData))
                {
                    notifyItemChanged(position);
                    mCheckedPositionMap.remove(itemData.getId());
                    for (Pair<Integer, Integer> pair : mCheckedPositionMap.values())
                    {
                        if (pair.first >= oldIndex)
                        {
                            notifyItemChanged(pair.second);
                        }
                    }
                }
            }
        });

        //显示是否为GIF
        if (itemData.isGif())
        {
            holder.setVisibility(R.id.imgGif, View.VISIBLE);
            if (PickCommonConfig.getInstance().getImagePickerDisplayer() != null)
            {
                PickCommonConfig.getInstance().getImagePickerDisplayer().displayGifImage(
                        getContext(), itemData.getPath(), imageView);
            }
        } else
        {
            holder.setVisibility(R.id.imgGif, View.GONE);
            if (PickCommonConfig.getInstance().getImagePickerDisplayer() != null)
            {
                PickCommonConfig.getInstance().getImagePickerDisplayer().displayImage(
                        getContext(), itemData.getPath(), imageView);
            }
        }
    }
}

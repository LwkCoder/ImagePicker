package com.lwkandroid.imagepicker.custom.pick;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.config.PickCommonConfig;
import com.lwkandroid.imagepicker.utils.Utils;
import com.lwkandroid.imagepicker.widget.photoview.OnViewTapListener;
import com.lwkandroid.imagepicker.widget.photoview.PhotoView;
import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import androidx.core.content.res.ResourcesCompat;

/**
 * @description: ViewPager预览图片适配器
 * @author: LWK
 * @date: 2021/6/17 10:35
 */
final class PagerPreviewAdapter extends RcvSingleAdapter<MediaBean>
{
    private final int mFileSizeBackGroundColor;
    private final int mFileSizeTextColor;
    private final OnViewTapListener mViewTapListener;

    public PagerPreviewAdapter(Context context, int fileSizeBackGroundColor,
                               int fileSizeTextColor, OnViewTapListener tapListener)
    {
        super(context, R.layout.adapter_pager_image_content, null);
        this.mViewTapListener = tapListener;
        this.mFileSizeBackGroundColor = fileSizeBackGroundColor;
        this.mFileSizeTextColor = fileSizeTextColor;
    }

    @Override
    public void onBindView(RcvHolder holder, MediaBean itemData, int position)
    {
        PhotoView photoView = holder.findView(R.id.photoView);
        TextView tvSize = holder.findView(R.id.tvSize);

        photoView.setOnViewTapListener(mViewTapListener);

        Drawable bgDrawable = ResourcesCompat.getDrawable(getContext().getResources(),
                R.drawable.shape_file_size_background, getContext().getTheme());
        bgDrawable.setTint(mFileSizeBackGroundColor);
        tvSize.setBackground(bgDrawable);
        tvSize.setTextColor(mFileSizeTextColor);
        tvSize.setText(getContext().getString(R.string.file_size_placeholder, Utils.byte2FitMemorySize(itemData.getSize(), 1)));

        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //显示是否为GIF
        if (itemData.isGif())
        {
            holder.setVisibility(R.id.imgGif, View.VISIBLE);
            if (PickCommonConfig.getInstance().getImagePickerDisplayer() != null)
            {
                PickCommonConfig.getInstance().getImagePickerDisplayer().displayGifImage(
                        getContext(), itemData.getPath(), photoView);
            }
        } else
        {
            holder.setVisibility(R.id.imgGif, View.GONE);
            if (PickCommonConfig.getInstance().getImagePickerDisplayer() != null)
            {
                PickCommonConfig.getInstance().getImagePickerDisplayer().displayImage(
                        getContext(), itemData.getPath(), photoView);
            }
        }
    }
}

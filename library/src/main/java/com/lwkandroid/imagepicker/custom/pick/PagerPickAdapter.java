package com.lwkandroid.imagepicker.custom.pick;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.config.PickCommonConfig;
import com.lwkandroid.imagepicker.widget.photoview.PhotoView;
import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

/**
 * @description:
 * @author: LWK
 * @date: 2021/6/17 10:35
 */
class PagerPickAdapter extends RcvSingleAdapter<MediaBean>
{
    private IMediaDataSupplier mMediaDataSupplier;

    public PagerPickAdapter(Context context, IMediaDataSupplier supplier)
    {
        super(context, R.layout.adapter_pager_image_content, null);
        this.mMediaDataSupplier = supplier;
    }

    @Override
    public void onBindView(RcvHolder holder, MediaBean itemData, int position)
    {
        if (itemData == null)
        {
            if (mMediaDataSupplier != null)
            {
                mMediaDataSupplier.onMediaDataRequest(position, new PickCallBack<MediaBean>()
                {
                    @Override
                    public void onPickSuccess(MediaBean result)
                    {
                        getDatas().set(position, result);
                        notifyItemChanged(position);
                    }

                    @Override
                    public void onPickFailed(int errorCode, String message)
                    {
                        Log.e("ImagePicker", "position = " + position + " 加载失败");
                    }
                });
            }
        } else
        {
            PhotoView photoView = holder.findView(R.id.photoView);
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

    public interface IMediaDataSupplier
    {
        void onMediaDataRequest(int position, PickCallBack<MediaBean> callBack);
    }
}

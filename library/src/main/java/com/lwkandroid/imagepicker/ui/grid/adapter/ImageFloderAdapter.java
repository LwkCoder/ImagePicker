package com.lwkandroid.imagepicker.ui.grid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.base.adapter.IImagePickerItemView;
import com.lwkandroid.imagepicker.base.adapter.ImagePickerBaseAdapter;
import com.lwkandroid.imagepicker.base.adapter.ImagePickerViewHolder;
import com.lwkandroid.imagepicker.data.ImageDataModel;
import com.lwkandroid.imagepicker.data.ImageFolderBean;

/**
 * Created by LWK
 * TODO 文件夹适配器
 */
public class ImageFloderAdapter extends ImagePickerBaseAdapter<ImageFolderBean>
{
    private int mCurfloderPosition;

    public ImageFloderAdapter(Context context, int position)
    {
        super(context, ImageDataModel.getInstance().getAllFolderList());
        this.mCurfloderPosition = position;
        addItemView(new ImageFloerItemView());
    }

    private class ImageFloerItemView implements IImagePickerItemView<ImageFolderBean>
    {

        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.layout_image_floder_listitem;
        }

        @Override
        public boolean isForViewType(ImageFolderBean item, int position)
        {
            return true;
        }

        @Override
        public void setData(ImagePickerViewHolder holder, ImageFolderBean imageFolderBean, int position, ViewGroup parent)
        {
            ImageView imgFirst = holder.findView(R.id.img_floder_listitem_firstImg);

            if (imageFolderBean != null)
            {
                ImageDataModel.getInstance().getDisPlayer().display(holder.getContext()
                        , imageFolderBean.getFirstImgPath(), imgFirst,
                        R.drawable.glide_default_picture, R.drawable.glide_default_picture,
                        300, 300);
                holder.setTvText(R.id.tv_floder_pop_listitem_name, imageFolderBean.getFolderName());
                holder.setTvText(R.id.tv_floder_pop_listitem_num,
                        holder.getContext().getResources().getString(R.string.imagepicker_folder_image_num
                                , String.valueOf(imageFolderBean.getNum())));
                if (position == mCurfloderPosition)
                    holder.setVisibility(R.id.img_floder_pop_listitem_selected, View.VISIBLE);
                else
                    holder.setVisibility(R.id.img_floder_pop_listitem_selected, View.GONE);
            }
        }
    }
}

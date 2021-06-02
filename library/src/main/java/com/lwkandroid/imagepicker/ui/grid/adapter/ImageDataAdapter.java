package com.lwkandroid.imagepicker.ui.grid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.lwkandroid.imagepicker.base.adapter.ImagePickerBaseAdapter;
import com.lwkandroid.imagepicker.base.adapter.ImagePickerViewHolder;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickerOptions;
import com.lwkandroid.imagepicker.ui.grid.view.IImageDataView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LWK
 * TODO GridView适配器
 */
public class ImageDataAdapter extends ImagePickerBaseAdapter<ImageBean>
{
    private ImagePickerOptions mOptions;
    //每个元素的宽高
    private int mImageLayoutSize;

    public ImageDataAdapter(Context context, int layoutSize, IImageDataView viewImpl)
    {
        super(context, null);
        mOptions = viewImpl.getOptions();
        this.mImageLayoutSize = layoutSize;

        //创建子布局
        if (mOptions.isNeedCamera())
            addItemView(new ImageCameraItemView(viewImpl));
        addItemView(new ImageContentItemView(viewImpl, this));
    }

    @Override
    protected void onCreateConvertView(int position, View convertView, ImagePickerViewHolder holder, ViewGroup parent)
    {
        super.onCreateConvertView(position, convertView, holder, parent);
        //设置每个item为正方形
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageLayoutSize));
    }

    @Override
    protected void onReuseConvertView(int position, View convertView, ImagePickerViewHolder holder, ViewGroup parent)
    {
        super.onReuseConvertView(position, convertView, holder, parent);
        //设置每个item为正方形
        AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) convertView.getLayoutParams();
        layoutParams.height = mImageLayoutSize;
        convertView.setLayoutParams(layoutParams);
    }

    @Override
    public void refreshDatas(List<ImageBean> datas)
    {
        List<ImageBean> datalist = new ArrayList<>();
        datalist.addAll(datas);
        //如果有相机入口要在数据集合0号位插入一条空数据
        if (mOptions.isNeedCamera())
            datalist.add(0, null);
        super.refreshDatas(datalist);
    }

    public void adjustLayoutSize(int layoutSize)
    {
        mImageLayoutSize = layoutSize;
    }

}

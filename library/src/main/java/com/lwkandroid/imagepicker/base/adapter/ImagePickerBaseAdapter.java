package com.lwkandroid.imagepicker.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:通用适配器
 */
public abstract class ImagePickerBaseAdapter<T> extends BaseAdapter
{
    protected Context mContext;
    protected List<T> mDataList = new ArrayList<>();

    private ImagePickerItemViewManager<T> mItemViewManager;

    public ImagePickerBaseAdapter(Context context, List<T> datas)
    {
        this.mContext = context;
        if (datas != null && datas.size() > 0)
            this.mDataList.addAll(datas);
        mItemViewManager = new ImagePickerItemViewManager<>();
    }

    /**
     * 获取当前所有数据
     */
    public List<T> getDatas()
    {
        return mDataList;
    }

    /**
     * 添加子布局类型
     */
    public ImagePickerBaseAdapter addItemView(IImagePickerItemView<T> itemView)
    {
        mItemViewManager.addItemView(itemView);
        return this;
    }

    /**
     * 添加子布局类型
     */
    public ImagePickerBaseAdapter addItemView(int viewType, IImagePickerItemView<T> itemView)
    {
        mItemViewManager.addItemView(viewType, itemView);
        return this;
    }

    /**
     * 是否启用子布局管理器
     * 【会根据其内子布局个数自动判断】
     */
    private boolean useItemViewManager()
    {
        return mItemViewManager.getItemViewCount() > 0;
    }

    @Override
    public int getViewTypeCount()
    {
        if (useItemViewManager())
            return mItemViewManager.getItemViewCount();
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (useItemViewManager())
        {
            int viewType = mItemViewManager.getItemViewType(mDataList.get(position), position);
            return viewType;
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        int layoutId = mItemViewManager.getItemViewLayoutId(mDataList.get(position), position);

        ImagePickerViewHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            holder = new ImagePickerViewHolder(mContext, convertView, position, layoutId);
            onCreateConvertView(position, convertView, holder, parent);
            convertView.setTag(holder);
        } else
        {
            holder = (ImagePickerViewHolder) convertView.getTag();
            holder.updatePosition(position);
            onReuseConvertView(position, convertView, holder, parent);
        }

        setData(holder, getItem(position), position, parent);
        return holder.getConvertView();
    }

    protected void onCreateConvertView(int position, View convertView, ImagePickerViewHolder holder, ViewGroup parent)
    {

    }

    protected void onReuseConvertView(int position, View convertView, ImagePickerViewHolder holder, ViewGroup parent)
    {

    }

    protected void setData(ImagePickerViewHolder viewHolder, T item, int position, ViewGroup parent)
    {
        mItemViewManager.setData(viewHolder, item, position, parent);
    }

    @Override
    public int getCount()
    {
        return mDataList.size();
    }

    @Override
    public T getItem(int position)
    {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    /**
     * 刷新数据的方法
     *
     * @param datas 新的数据list
     */
    public void refreshDatas(List<T> datas)
    {
        mDataList.clear();
        if (datas != null && datas.size() > 0)
            mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 增加一条数据
     */
    public void addData(T t)
    {
        if (t != null)
            mDataList.add(t);
        notifyDataSetChanged();
    }

    /**
     * 增加若干条数据
     */
    public void addDatas(List<T> data)
    {
        if (data != null && data.size() > 0)
            mDataList.addAll(data);
        notifyDataSetChanged();
    }
}

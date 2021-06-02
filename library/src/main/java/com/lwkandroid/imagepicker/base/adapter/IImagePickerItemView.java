package com.lwkandroid.imagepicker.base.adapter;

import android.view.ViewGroup;

/**
 * Function:子布局实现接口
 */
public interface IImagePickerItemView<T>
{
    /**
     * 子类实现此方法返回对应的子布局id
     *
     * @return 子布局id
     */
    int getItemViewLayoutId();

    /**
     * 子类实现此方法决定引用该子布局的时机
     *
     * @param item     该position对应的数据
     * @param position position
     * @return 是否属于子布局
     */
    boolean isForViewType(T item, int position);

    /**
     * 设置数据的方法
     *
     * @param holder   通用ViewHolder
     * @param t        数据
     * @param position 位置
     * @param parent   父布局
     */
    void setData(ImagePickerViewHolder holder, T t, int position, ViewGroup parent);
}

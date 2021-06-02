package com.lwkandroid.imagepicker.base.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Function:子布局管理器
 */
public class ImagePickerItemViewManager<T>
{
    private SparseArray<IImagePickerItemView<T>> mAllItemViews = new SparseArray();

    public int getItemViewCount()
    {
        return mAllItemViews.size();
    }

    public ImagePickerItemViewManager<T> addItemView(IImagePickerItemView<T> itemView)
    {
        int viewType = mAllItemViews.size();
        if (itemView != null)
        {
            mAllItemViews.put(viewType, itemView);
            viewType++;
        }
        return this;
    }

    public ImagePickerItemViewManager<T> addItemView(int viewType, IImagePickerItemView<T> itemView)
    {
        if (mAllItemViews.get(viewType) != null)
        {
            throw new IllegalArgumentException(
                    "An ItemView is already registered for the viewType = "
                            + viewType
                            + ". Already registered ItemView is "
                            + mAllItemViews.get(viewType));
        }
        mAllItemViews.put(viewType, itemView);
        return this;
    }

    public ImagePickerItemViewManager<T> removeItemView(IImagePickerItemView<T> itemView)
    {
        if (itemView == null)
        {
            throw new NullPointerException("ItemViewis null");
        }

        int indexToRemove = mAllItemViews.indexOfValue(itemView);
        if (indexToRemove >= 0)
            mAllItemViews.removeAt(indexToRemove);
        return this;
    }

    public ImagePickerItemViewManager<T> removeItemView(int itemType)
    {
        int indexToRemove = mAllItemViews.indexOfKey(itemType);

        if (indexToRemove >= 0)
            mAllItemViews.removeAt(indexToRemove);
        return this;
    }

    public int getItemViewType(T item, int position)
    {
        int itemViewCounts = mAllItemViews.size();
        for (int i = itemViewCounts - 1; i >= 0; i--)
        {
            IImagePickerItemView<T> itemView = mAllItemViews.valueAt(i);
            if (itemView.isForViewType(item, position))
                return mAllItemViews.keyAt(i);
        }
        throw new IllegalArgumentException("No ItemView added that matches position=" + position + " in data source");
    }

    public void setData(ImagePickerViewHolder holder, T item, int position, ViewGroup parent)
    {
        int itemViewCounts = mAllItemViews.size();
        for (int i = 0; i < itemViewCounts; i++)
        {
            IImagePickerItemView<T> itemView = mAllItemViews.valueAt(i);

            if (itemView.isForViewType(item, position))
            {
                itemView.setData(holder, item, position, parent);
                return;
            }
        }
        throw new IllegalArgumentException(
                "No ImagePickerItemViewManager added that matches position=" + position + " in data source");
    }


    public int getItemViewLayoutId(int viewType)
    {
        return mAllItemViews.get(viewType).getItemViewLayoutId();
    }

    public int getItemViewType(IImagePickerItemView itemView)
    {
        return mAllItemViews.indexOfValue(itemView);
    }

    public int getItemViewLayoutId(T item, int position)
    {
        int itemViewCounts = mAllItemViews.size();
        for (int i = itemViewCounts - 1; i >= 0; i--)
        {
            IImagePickerItemView<T> itemView = mAllItemViews.valueAt(i);
            if (itemView.isForViewType(item, position))
                return itemView.getItemViewLayoutId();
        }
        throw new IllegalArgumentException("No ItemView added that matches position=" + position + " in data source");
    }
}

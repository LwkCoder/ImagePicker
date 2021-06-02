package com.lwkandroid.imagepicker.base.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Function:ViewHolder通用模版
 */
public class ImagePickerViewHolder
{
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;
    private int mLayoutId;
    private int mPosition;

    public ImagePickerViewHolder(Context context, View itemView, int position, int layoutId)
    {
        this.mViews = new SparseArray<>();
        mContext = context;
        mConvertView = itemView;
        mPosition = position;
        mLayoutId = layoutId;
        mViews = new SparseArray<>();
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context     上下文
     * @param convertView 复用布局
     * @param parent      父布局
     * @param layoutId    根布局id
     * @param position    getView的position
     */
    //    public static ImagePickerViewHolder get(Context context, View convertView
    //            , int layoutId, int position, ViewGroup parent)
    //    {
    //        if (convertView == null)
    //        {
    //            View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
    //            ImagePickerViewHolder holder = new ImagePickerViewHolder(context, itemView, position, parent);
    //            holder.mLayoutId = layoutId;
    //            return holder;
    //        } else
    //        {
    //            ImagePickerViewHolder holder = (ImagePickerViewHolder) convertView.getTag();
    //            holder.mPosition = position;
    //            return holder;
    //        }
    //    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T findView(int viewId)
    {
        View view = mViews.get(viewId);
        if (view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public Context getContext()
    {
        return mContext;
    }

    public View getConvertView()
    {
        return mConvertView;
    }

    public int getPosition()
    {
        return mPosition;
    }

    public void updatePosition(int position)
    {
        mPosition = position;
    }

    /**
     * 设置View是否可见
     */
    public ImagePickerViewHolder setVisibility(int viewId, int visibility)
    {
        View view = findView(viewId);
        if (view != null)
            view.setVisibility(visibility);
        return this;
    }

    /**
     * 为TextView设置字符串
     */
    public ImagePickerViewHolder setTvText(int viewId, String text)
    {
        TextView tv = findView(viewId);
        if (tv != null)
            tv.setText(text);
        return this;
    }

    /**
     * 为TextView设置字符串
     */
    public ImagePickerViewHolder setTvText(int viewId, int resId)
    {
        TextView tv = findView(viewId);
        if (tv != null)
            tv.setText(resId);
        return this;
    }

    /**
     * 设置textview颜色
     */
    public ImagePickerViewHolder setTvColor(int viewId, int textColor)
    {
        TextView tv = findView(viewId);
        if (tv != null)
            tv.setTextColor(textColor);
        return this;
    }

    /**
     * 设置textview颜色【资源id】
     */
    public ImagePickerViewHolder setTvColorRes(int viewId, int textColorRes)
    {
        TextView tv = findView(viewId);
        if (tv != null)
            tv.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    /**
     * 设置textview支持超链接
     */
    public ImagePickerViewHolder setTvLinkify(int viewId)
    {
        TextView tv = findView(viewId);
        if (tv != null)
            Linkify.addLinks(tv, Linkify.ALL);
        return this;
    }

    /**
     * 设置textview的Typeface
     */
    public ImagePickerViewHolder setTvTypeface(Typeface typeface, int... viewIds)
    {
        for (int viewId : viewIds)
        {
            TextView tv = findView(viewId);
            if (tv != null)
            {
                tv.setTypeface(typeface);
                tv.setPaintFlags(tv.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            }
        }
        return this;
    }

    /**
     * 设置button的文本
     */
    public ImagePickerViewHolder setBtnText(int viewId, String text)
    {
        Button button = findView(viewId);
        if (button != null)
            button.setText(text);
        return this;
    }

    /**
     * 设置button的文本【资源id】
     */
    public ImagePickerViewHolder setBtnText(int viewId, int resId)
    {
        Button button = findView(viewId);
        if (button != null)
            button.setText(resId);
        return this;
    }

    /**
     * 为ImageView设置本地资源图片
     */
    public ImagePickerViewHolder setImgResource(int viewId, int resId)
    {
        ImageView img = findView(viewId);
        if (img != null)
            img.setImageResource(resId);
        return this;
    }

    /**
     * 为View设置点击事件
     */
    public ImagePickerViewHolder setClickListener(int viewId, View.OnClickListener l)
    {
        View view = findView(viewId);
        if (view != null)
            view.setOnClickListener(l);
        return this;
    }

    /**
     * 设置View背景色
     */
    public ImagePickerViewHolder setBackgroundColor(int viewId, int color)
    {
        View view = findView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置View的背景【资源id】
     */
    public ImagePickerViewHolder setBackgroundRes(int viewId, int bgResId)
    {
        View view = findView(viewId);
        view.setBackgroundResource(bgResId);
        return this;
    }

    /**
     * 设置View是否可选
     */
    public ImagePickerViewHolder setChecked(int viewId, boolean checked)
    {
        Checkable view = (Checkable) findView(viewId);
        view.setChecked(checked);
        return this;
    }
}

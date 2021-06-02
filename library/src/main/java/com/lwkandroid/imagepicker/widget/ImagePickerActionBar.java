package com.lwkandroid.imagepicker.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lwkandroid.imagepicker.R;

/**
 * 自定义ActionBar
 */
public class ImagePickerActionBar extends FrameLayout
{
    private TextView mTvTitle;
    private TextView mTvPreview;

    public ImagePickerActionBar(Context context)
    {
        super(context);
        init(context, null);
    }

    public ImagePickerActionBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs)
    {
        inflate(context, R.layout.layout_image_picker_actionbar, this);
        setWillNotDraw(false);

        mTvTitle = (TextView) findViewById(R.id.tv_imagepicker_actionbar_title);
        mTvPreview = (TextView) findViewById(R.id.tv_imagepicker_actionbar_preview);
        TextView tvBack = (TextView) findViewById(R.id.tv_imagepicker_actionbar_back);

        if (context instanceof Activity)
        {
            tvBack.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ((Activity) context).finish();
                }
            });


        }
    }

    /**
     * 设置标题
     */
    public void setTitle(String s)
    {
        if (mTvTitle != null)
            mTvTitle.setText(s);
    }

    /**
     * 设置标题
     */
    public void setTitle(int resId)
    {
        if (mTvTitle != null)
            mTvTitle.setText(resId);
    }

    /**
     * 隐藏预览入口
     */
    public void hidePreview()
    {
        if (mTvPreview != null)
            mTvPreview.setVisibility(View.GONE);
    }

    /**
     * 显示预览入口
     */
    public void showPreview()
    {
        if (mTvPreview != null)
            mTvPreview.setVisibility(View.VISIBLE);
    }

    /**
     * 设置预览入口是否可点击
     */
    public void enablePreview(boolean b)
    {
        if (mTvPreview != null)
            mTvPreview.setEnabled(b);
    }

    /**
     * 预览入口点击监听
     */
    public void setOnPreviewClickListener(OnClickListener listener)
    {
        if (mTvPreview != null)
            mTvPreview.setOnClickListener(listener);
    }
}

package com.lwkandroid.imagepicker.widget;

import android.content.Context;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.rcvadapter.base.RcvBaseLoadMoreView;
import com.lwkandroid.rcvadapter.ui.RcvLoadingView;

import androidx.annotation.ColorInt;

/**
 * @description: LoadMore
 * @author: LWK
 * @date: 2021/6/28 13:39
 */
public final class IpRcvLoadMoreView extends RcvBaseLoadMoreView
{
    private RcvLoadingView mLoadingView;
    private int mLoadingColor = android.R.color.darker_gray;

    public IpRcvLoadMoreView(Context context)
    {
        super(context);
    }

    @Override
    protected int setContentViewId()
    {
        return R.layout.layout_loading_more;
    }

    @Override
    protected void initUI()
    {
        mLoadingView = findViewById(R.id.loadingView);
        mLoadingView.setColor(mLoadingColor);
    }

    @Override
    protected void setBeforeLoadingUI()
    {
        mLoadingView.setVisibility(INVISIBLE);
    }

    @Override
    protected void setLoadingUI()
    {
        mLoadingView.setVisibility(VISIBLE);
    }

    @Override
    protected void setLoadSuccessUI()
    {
        mLoadingView.setVisibility(VISIBLE);
    }

    @Override
    protected void setLoadFailUI()
    {
        mLoadingView.setVisibility(INVISIBLE);
    }

    @Override
    protected void setNoMoreDataUI()
    {
        mLoadingView.setVisibility(INVISIBLE);
    }

    public void setLoadingColor(@ColorInt int color)
    {
        this.mLoadingColor = color;
        if (mLoadingView != null)
        {
            mLoadingView.setColor(mLoadingColor);
        }
    }
}

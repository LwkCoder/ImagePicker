package com.lwkandroid.imagepicker.ui.grid.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.data.ImageDataModel;
import com.lwkandroid.imagepicker.data.ImageFolderBean;
import com.lwkandroid.imagepicker.ui.grid.adapter.ImageFloderAdapter;

import java.lang.ref.WeakReference;

/**
 * Created by LWK
 * TODO 展示文件夹的PopupWindow
 */

public class ImageFloderPop implements PopupWindow.OnDismissListener, AdapterView.OnItemClickListener
{
    private WeakReference<Activity> mActReference;
    private PopupWindow mPopupWindow;
    private ListView mListView;
    private ImageFloderAdapter mAdapter;
    private onFloderItemClickListener mListener;
    private ValueAnimator mAnimator;
    private float mBgAlpha = 1f;
    private boolean mIsBright;

    //获取Activity引用实例
    private Activity getActivity()
    {
        return mActReference != null ? mActReference.get() : null;
    }

    /**
     * 从Activity底部弹出来
     */
    public void showAtBottom(Activity activity, View parent, ImageFolderBean curFloder, onFloderItemClickListener listener)
    {
        this.mActReference = new WeakReference<>(activity);
        this.mListener = listener;

        View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_image_floder_pop, null);
        int height = activity.getResources().getDimensionPixelSize(R.dimen.imagepicker_floder_pop_height);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, height, true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());// 响应返回键必须的语句。
        mPopupWindow.setFocusable(true);//设置pop可获取焦点
        mPopupWindow.setAnimationStyle(R.style.FloderPopAnimStyle);//设置显示、消失动画
        mPopupWindow.setOutsideTouchable(true);//设置点击外部可关闭pop
        mPopupWindow.setOnDismissListener(this);

        mListView = (ListView) contentView.findViewById(R.id.lv_image_floder_pop);
        final int position = ImageDataModel.getInstance().getAllFolderList().indexOf(curFloder);
        mAdapter = new ImageFloderAdapter(activity, position);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        toggleWindowAlpha();

        // 增加绘制监听
        ViewTreeObserver vto = mListView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                // 移除监听
                mListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mListView.setSelection(position);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (mListener != null)
            mListener.onFloderItemClicked(mAdapter.getItem(position));
        if (mPopupWindow != null)
            mPopupWindow.dismiss();
    }

    @Override
    public void onDismiss()
    {
        toggleWindowAlpha();
    }

    /**
     * 切换窗口透明度
     */
    private void toggleWindowAlpha()
    {
        if (mAnimator != null)
        {
            mAnimator.cancel();
            mAnimator = null;
        }

        mAnimator = ValueAnimator.ofFloat(0.5f, 1.0f);
        mAnimator.setDuration(200);//动画时间要和PopupWindow弹出动画的时间一致
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (float) animation.getAnimatedValue();
                mBgAlpha = mIsBright ? value : (1.5f - value);
                updateBgAlpha(mBgAlpha);
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                mIsBright = !mIsBright;
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });
        mAnimator.start();
    }

    /***
     * 此方法用于改变背景的透明度，从而达到“变暗”的效果
     */
    private void updateBgAlpha(float bgAlpha)
    {
        Activity activity = getActivity();
        if (activity != null)
        {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = bgAlpha; //0.0-1.0
            window.setAttributes(lp);
        }
    }

    public interface onFloderItemClickListener
    {
        void onFloderItemClicked(ImageFolderBean floderBean);
    }
}

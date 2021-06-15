package com.lwkandroid.imagepicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.lwkandroid.imagepicker.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

/**
 * @description:
 * @author: LWK
 * @date: 2021/6/15 9:52
 */
public class CheckView extends View
{
    public static final int CHECK_MODE_DRAWABLE = 0;
    public static final int CHECK_MODE_NUMBER = 1;

    //默认尺寸，48dp
    private static final int DEFAULT_SIZE = 48;

    private boolean checked;
    private int textSize;
    private int backgroundColor;
    private int borderColor;
    private int borderWidth;
    private int numberCenter;
    private Drawable drawableCenter;
    @CheckMode
    private int checkMode;

    @IntDef({CHECK_MODE_DRAWABLE, CHECK_MODE_NUMBER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CheckMode
    {

    }

    public CheckView(Context context)
    {
        this(context, null);
    }

    public CheckView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (MeasureSpec.EXACTLY != widthMode || MeasureSpec.EXACTLY != heightMode)
        {
            int density = (int) getContext().getResources().getDisplayMetrics().density;
            widthSize = heightSize = DEFAULT_SIZE * density;
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    private void init(Context context, AttributeSet attrs)
    {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ComActionBar);
        checked = ta.getBoolean(R.styleable.CheckView_android_checked, false);
        checkMode = ta.getInt(R.styleable.CheckView_checkMode, CHECK_MODE_DRAWABLE);
        textSize = ta.getDimensionPixelOffset(R.styleable.CheckView_android_textSize,
                context.getResources().getDimensionPixelOffset(R.dimen.check_view_text_size_default));
        backgroundColor = ta.getColor(R.styleable.CheckView_android_background, Color.TRANSPARENT);
        borderColor = ta.getColor(R.styleable.CheckView_border_color, Color.WHITE);
        borderWidth = ta.getDimensionPixelOffset(R.styleable.CheckView_border_width,
                context.getResources().getDimensionPixelOffset(R.dimen.check_view_border_width_default));
        numberCenter = ta.getInt(R.styleable.CheckView_numberCenter, 0);
        drawableCenter = ta.getDrawable(R.styleable.CheckView_drawableCenter);
    }
}

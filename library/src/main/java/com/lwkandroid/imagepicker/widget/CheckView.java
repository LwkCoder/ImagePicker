package com.lwkandroid.imagepicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
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

    private boolean mChecked;
    private int mTextSize;
    private int mBackgroundColor;
    private int mBorderColor;
    private int mBorderWidth;
    private int mNumberCenter;
    private Drawable mDrawableCenter;
    @CheckMode
    private int mCheckMode;

    private Paint mBorderPaint;
    private Paint mBackgroundPaint;
    private TextPaint mTextPaint;

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

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

    }

    private void init(Context context, AttributeSet attrs)
    {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CheckView);
        mChecked = ta.getBoolean(R.styleable.CheckView_android_checked, false);
        mCheckMode = ta.getInt(R.styleable.CheckView_checkMode, CHECK_MODE_DRAWABLE);
        mTextSize = ta.getDimensionPixelOffset(R.styleable.CheckView_android_textSize,
                context.getResources().getDimensionPixelOffset(R.dimen.check_view_text_size_default));
        mBackgroundColor = ta.getColor(R.styleable.CheckView_android_background, Color.TRANSPARENT);
        mBorderColor = ta.getColor(R.styleable.CheckView_border_color, Color.WHITE);
        mBorderWidth = ta.getDimensionPixelOffset(R.styleable.CheckView_border_width,
                context.getResources().getDimensionPixelOffset(R.dimen.check_view_border_width_default));
        mNumberCenter = ta.getInt(R.styleable.CheckView_numberCenter, 0);
        mDrawableCenter = ta.getDrawable(R.styleable.CheckView_drawableCenter);

        initTextPaint();
        initBorderPaint();
        initBackgroundPaint();
    }

    private void initBorderPaint()
    {
        if (mBackgroundPaint != null)
        {
            return;
        }
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setColor(mBorderColor);
    }

    private void initBackgroundPaint()
    {
        if (mBackgroundPaint != null)
        {
            return;
        }
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mBackgroundColor);
    }

    private void initTextPaint()
    {
        if (mTextPaint != null)
        {
            return;
        }
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        mTextPaint.setTextSize(mTextSize);
    }
}

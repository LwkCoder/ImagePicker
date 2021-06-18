package com.lwkandroid.imagepicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.rcvadapter.utils.RcvUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

/**
 * @description: 自定义单选框
 * @author: LWK
 * @date: 2021/6/15 9:52
 */
public class CheckView extends View implements View.OnClickListener
{
    public static final int CHECK_MODE_DRAWABLE = 0;
    public static final int CHECK_MODE_NUMBER = 1;

    private boolean mEnabled;
    private boolean mChecked;
    private int mCheckedColor;
    private int mBorderColor;
    private int mBorderWidth;
    private int mNumber;
    private Drawable mDrawable;
    @CheckMode
    private int mCheckMode;

    private Paint mBorderPaint;
    private Paint mBackgroundPaint;
    private TextPaint mTextPaint;

    private int mXCenter;
    private int mYCenter;
    private int mContentRadius;
    private Rect mDrawableRect;
    private OnCheckedChangeListener mListener;

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
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        if (MeasureSpec.EXACTLY != widthMode && MeasureSpec.EXACTLY != heightMode)
        {
            //宽高都未指定
            int defaultSize = getContext().getResources().getDimensionPixelOffset(R.dimen.check_view_size_default);
            measuredWidth = measuredHeight = defaultSize;
        } else
        {
            //宽高之中有一个未指定，设置未指定的等于指定的值
            measuredWidth = measuredHeight = Math.min(measuredWidth, measuredHeight);
        }
        //将padding一并加入
        measuredWidth = measuredWidth + paddingLeft + paddingRight;
        measuredHeight = measuredHeight + paddingTop + paddingBottom;

        //计算可绘制区域的尺寸（正方形区域）
        int size = Math.min(measuredWidth - getPaddingLeft() - getPaddingRight(),
                measuredHeight - getPaddingTop() - getPaddingBottom());
        //计算后续要用到的各种参数
        mContentRadius = size / 2;
        mXCenter = measuredWidth / 2;
        mYCenter = measuredHeight / 2;
        if (mDrawableRect == null)
        {
            mDrawableRect = new Rect();
        }
        int drawableSize = mContentRadius / 2 * 3;
        mDrawableRect.set(mXCenter - drawableSize / 2, mYCenter - drawableSize / 2,
                mXCenter + drawableSize / 2, mYCenter + drawableSize / 2);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        //绘制border
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setColor(mBorderColor);
        canvas.drawCircle(mXCenter, mYCenter, mContentRadius - mBorderWidth / 2, mBorderPaint);

        //绘制背景
        mBackgroundPaint.setColor(mChecked ? mCheckedColor : Color.TRANSPARENT);
        canvas.drawCircle(mXCenter, mYCenter, mContentRadius - mBorderWidth / 2, mBackgroundPaint);

        //绘制内容
        if (mChecked)
        {
            if (CHECK_MODE_NUMBER == mCheckMode)
            {
                mTextPaint.setTextSize(mContentRadius);
                String text = String.valueOf(mNumber);
                int baseX = (int) (getWidth() - mTextPaint.measureText(text)) / 2;
                int baseY = (int) (getHeight() - mTextPaint.descent() - mTextPaint.ascent()) / 2;
                canvas.drawText(text, baseX, baseY, mTextPaint);
            } else
            {
                if (mDrawable != null)
                {
                    mDrawable.setBounds(mDrawableRect);
                    mDrawable.draw(canvas);
                }
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        if (!mEnabled)
        {
            return;
        }
        setChecked(!isChecked());
    }

    public boolean isChecked()
    {
        return mChecked;
    }

    public void setChecked(boolean checked)
    {
        setChecked(checked, true);
    }

    public void setChecked(boolean checked, boolean invokeListener)
    {
        boolean c = mChecked;
        this.mChecked = checked;
        invalidate();
        if (mChecked != c && invokeListener && mListener != null)
        {
            mListener.onCheckChanged(this, mChecked);
        }
    }

    public int getCheckedColor()
    {
        return mCheckedColor;
    }

    public void setCheckedColor(@ColorInt int color)
    {
        this.mCheckedColor = color;
    }

    public int getBorderColor()
    {
        return mBorderColor;
    }

    public void setBorderColor(@ColorInt int color)
    {
        this.mBorderColor = color;
    }

    public int getBorderWidth()
    {
        return mBorderWidth;
    }

    public void setBorderWidth(int value)
    {
        this.mBorderWidth = value;
    }

    public int getNumber()
    {
        return mNumber;
    }

    public void setNumber(int number)
    {
        this.mNumber = number;
    }

    public Drawable getDrawable()
    {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable)
    {
        this.mDrawable = drawable;
    }

    public int getCheckMode()
    {
        return mCheckMode;
    }

    public void setCheckMode(@CheckMode int mode)
    {
        this.mCheckMode = mode;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener)
    {
        this.mListener = listener;
    }

    private void init(Context context, AttributeSet attrs)
    {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CheckView);
        mEnabled = ta.getBoolean(R.styleable.CheckView_android_enabled, true);
        mChecked = ta.getBoolean(R.styleable.CheckView_android_checked, false);
        mCheckMode = ta.getInt(R.styleable.CheckView_checkMode, CHECK_MODE_DRAWABLE);
        mCheckedColor = ta.getColor(R.styleable.CheckView_checkedColor, Color.BLUE);
        mBorderColor = ta.getColor(R.styleable.CheckView_borderColor, Color.WHITE);
        mBorderWidth = ta.getDimensionPixelOffset(R.styleable.CheckView_borderWidth,
                context.getResources().getDimensionPixelOffset(R.dimen.check_view_border_width_default));
        mNumber = ta.getInt(R.styleable.CheckView_numberValue, 0);
        mDrawable = ta.getDrawable(R.styleable.CheckView_drawableValue);
        if (mDrawable == null)
        {
            mDrawable = RcvUtils.getDrawableResources(context, R.drawable.image_picker_hook);
        }

        ta.recycle();

        initTextPaint();
        initBorderPaint();
        initBackgroundPaint();
        setOnClickListener(this);
    }

    private void initBorderPaint()
    {
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }

    private void initBackgroundPaint()
    {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    private void initTextPaint()
    {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    public interface OnCheckedChangeListener
    {
        void onCheckChanged(CheckView checkView, boolean isChecked);
    }
}

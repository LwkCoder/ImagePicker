package com.lwkandroid.imagepicker.widget.crop.gestures;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class CupcakeGestureDetector implements GestureDetector
{
    protected static final String TAG = CupcakeGestureDetector.class.getSimpleName();

    protected final float mTouchSlop;
    protected final float mMinimumVelocity;

    protected VelocityTracker mVelocityTracker;

    protected float mLastTouchX;
    protected float mLastTouchY;
    protected boolean mIsDragging;

    protected OnGestureListener mListener;

    public CupcakeGestureDetector(Context context)
    {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                mVelocityTracker = VelocityTracker.obtain();

                if (null != mVelocityTracker)
                {
                    mVelocityTracker.addMovement(ev);
                }

                mLastTouchX = getActiveX(ev);
                mLastTouchY = getActiveY(ev);
                mIsDragging = false;
                break;
            }

            case MotionEvent.ACTION_MOVE:
            {
                final float x = getActiveX(ev);
                final float y = getActiveY(ev);
                final float dx = x - mLastTouchX, dy = y - mLastTouchY;

                if (!mIsDragging)
                {
                    // Use 勾股定理   to see if drag length is larger than touch slop
                    mIsDragging = Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
                }

                if (mIsDragging)
                {
                    mListener.onDrag(dx, dy);
                    mLastTouchX = x;
                    mLastTouchY = y;

                    if (null != mVelocityTracker)
                    {
                        mVelocityTracker.addMovement(ev);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            {
                // Recycle Velocity Tracker
                if (null != mVelocityTracker)
                {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            {
                if (mIsDragging)
                {
                    if (null != mVelocityTracker)
                    {
                        mLastTouchX = getActiveX(ev);
                        mLastTouchY = getActiveY(ev);

                        // Compute velocity within the last 1000ms
                        mVelocityTracker.addMovement(ev);
                        mVelocityTracker.computeCurrentVelocity(1000);

                        final float vX = mVelocityTracker.getXVelocity(), vY = mVelocityTracker.getYVelocity();

                        // If the velocity is greater than minVelocity, call listener
                        if (Math.max(Math.abs(vX), Math.abs(vY)) >= mMinimumVelocity)
                        {
                            /** http://blog.csdn.net/ithomer/article/details/7455601 */
                            mListener.onFling(mLastTouchX, mLastTouchY, -vX, -vY);
                        }
                    }
                }

                // Recycle Velocity Tracker
                if (null != mVelocityTracker)
                {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            }
        }
        return true;
    }

    @Override
    public boolean isDragging()
    {
        return mIsDragging;
    }

    @Override
    public boolean isScaling()
    {
        return false;
    }

    @Override
    public void setOnGestureListener(OnGestureListener listener)
    {
        this.mListener = listener;
    }

    float getActiveX(MotionEvent ev)
    {
        return ev.getX();
    }

    float getActiveY(MotionEvent ev)
    {
        return ev.getY();
    }
}
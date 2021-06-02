package com.lwkandroid.imagepicker.widget.crop.gestures;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class FroyoGestureDetector extends EclairGestureDetector
{
    protected final ScaleGestureDetector mDetector;

    public FroyoGestureDetector(Context context)
    {
        super(context);

        ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener()
        {
            @Override
            public boolean onScale(ScaleGestureDetector detector)
            {
                float scaleFactor = detector.getScaleFactor();

                if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor))
                {
                    return false;
                }

                mListener.onScale(scaleFactor, detector.getFocusX(), detector.getFocusY());
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector)
            {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector)
            {
                // NO-OP
            }
        };
        mDetector = new ScaleGestureDetector(context, mScaleListener);
    }

    @Override
    public boolean isScaling()
    {
        return mDetector.isInProgress();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        mDetector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }
}
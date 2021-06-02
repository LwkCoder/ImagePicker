package com.lwkandroid.imagepicker.widget.crop;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class RotateBitmap
{
    private Bitmap bitmap;
    private int rotation;

    public RotateBitmap(Bitmap bitmap, int rotation)
    {
        this.bitmap = bitmap;
        this.rotation = rotation % 360;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setRotation(int rotation)
    {
        this.rotation = rotation;
    }

    public int getRotation()
    {
        return rotation;
    }

    public Matrix getRotateMatrix()
    {
        // By default this is an identity matrix
        Matrix matrix = new Matrix();
        if (bitmap != null && rotation != 0)
        {
            // We want to do the rotation at origin, but since the bounding
            // rectangle will be changed after rotation, so the delta values
            // are based on old & new width/height respectively.
            int cx = bitmap.getWidth() / 2;
            int cy = bitmap.getHeight() / 2;
            matrix.preTranslate(-cx, -cy);
            matrix.postRotate(rotation);
            matrix.postTranslate(getWidth() / 2, getHeight() / 2);
        }
        return matrix;
    }

    public boolean isOrientationChanged()
    {
        return (rotation / 90) % 2 != 0;
    }

    public int getWidth()
    {
        if (bitmap == null)
            return 0;
        if (isOrientationChanged())
        {
            return bitmap.getHeight();
        } else
        {
            return bitmap.getWidth();
        }
    }

    public int getHeight()
    {
        if (bitmap == null)
            return 0;
        if (isOrientationChanged())
        {
            return bitmap.getWidth();
        } else
        {
            return bitmap.getHeight();
        }
    }

    public void recycle()
    {
        if (bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
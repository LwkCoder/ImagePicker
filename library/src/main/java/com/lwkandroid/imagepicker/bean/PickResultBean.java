package com.lwkandroid.imagepicker.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @description: 选择结果
 * @author: LWK
 * @date: 2021/6/17 14:13
 */
public class PickResultBean implements Parcelable
{
    private boolean mOriginalFile;
    private List<MediaBean> mMediaList;

    public boolean isOriginalFile()
    {
        return mOriginalFile;
    }

    public void setOriginalFile(boolean b)
    {
        this.mOriginalFile = b;
    }

    public List<MediaBean> getMediaList()
    {
        return mMediaList;
    }

    public void setMediaList(List<MediaBean> list)
    {
        this.mMediaList = list;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeByte(this.mOriginalFile ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.mMediaList);
    }

    public PickResultBean()
    {
    }

    protected PickResultBean(Parcel in)
    {
        this.mOriginalFile = in.readByte() != 0;
        this.mMediaList = in.createTypedArrayList(MediaBean.CREATOR);
    }

    public static final Parcelable.Creator<PickResultBean> CREATOR = new Parcelable.Creator<PickResultBean>()
    {
        @Override
        public PickResultBean createFromParcel(Parcel source)
        {
            return new PickResultBean(source);
        }

        @Override
        public PickResultBean[] newArray(int size)
        {
            return new PickResultBean[size];
        }
    };
}

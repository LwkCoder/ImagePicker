package com.lwkandroid.imagepicker.custom.pick;

import com.lwkandroid.imagepicker.bean.MediaBean;

import java.util.LinkedList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

/**
 * @description: 临时存储所选数据的类, 多选模式下生效
 * @author: LWK
 * @date: 2021/6/16 13:31
 */
public class PickTempStorage
{
    private int mMaxNumber;
    private MutableLiveData<List<MediaBean>> mSelectedMediaLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mOriginFileStateLiveData = new MutableLiveData<>();

    public static PickTempStorage getInstance()
    {
        return Holder.INSTANCE;
    }

    private PickTempStorage()
    {
        clear();
    }

    private static final class Holder
    {
        private static final PickTempStorage INSTANCE = new PickTempStorage();
    }

    public void setMaxNumber(int maxNumber)
    {
        this.mMaxNumber = maxNumber;
    }

    public int getMaxNumber()
    {
        return mMaxNumber;
    }

    public boolean addMediaData(MediaBean mediaBean)
    {
        if (mSelectedMediaLiveData.getValue().size() == mMaxNumber)
        {
            return false;
        }
        mSelectedMediaLiveData.getValue().add(mediaBean);
        mSelectedMediaLiveData.postValue(mSelectedMediaLiveData.getValue());
        return true;
    }

    public boolean removeMediaData(MediaBean mediaBean)
    {
        boolean remove = mSelectedMediaLiveData.getValue().remove(mediaBean);
        if (remove)
        {
            mSelectedMediaLiveData.postValue(mSelectedMediaLiveData.getValue());
        }
        return remove;
    }

    public int indexMediaData(MediaBean mediaBean)
    {
        return mSelectedMediaLiveData.getValue().indexOf(mediaBean);
    }

    public MutableLiveData<List<MediaBean>> getSelectedMediaLiveData()
    {
        return mSelectedMediaLiveData;
    }

    public MutableLiveData<Boolean> getOriginFileStateLiveData()
    {
        return mOriginFileStateLiveData;
    }

    public boolean contains(MediaBean mediaBean)
    {
        return mediaBean != null
                && mSelectedMediaLiveData != null
                && mSelectedMediaLiveData.getValue() != null
                && mSelectedMediaLiveData.getValue().contains(mediaBean);
    }

    public void clear()
    {
        mSelectedMediaLiveData.postValue(new LinkedList<>());
        mOriginFileStateLiveData.postValue(false);
    }
}
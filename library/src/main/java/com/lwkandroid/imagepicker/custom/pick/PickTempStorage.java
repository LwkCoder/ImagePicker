package com.lwkandroid.imagepicker.custom.pick;

import com.lwkandroid.imagepicker.bean.MediaBean;

import java.util.LinkedList;
import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * @description: 临时存储所选数据的类
 * @author: LWK
 * @date: 2021/6/16 13:31
 */
public class PickTempStorage
{
    private int mMaxNumber;
    //已选文件集合，多选模式下生效
    private MutableLiveData<List<MediaBean>> mSelectedMediaLiveData = new MutableLiveData<>();

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

    public boolean addData(MediaBean mediaBean)
    {
        if (mSelectedMediaLiveData.getValue().size() == mMaxNumber)
        {
            return false;
        }
        mSelectedMediaLiveData.getValue().add(mediaBean);
        mSelectedMediaLiveData.postValue(mSelectedMediaLiveData.getValue());
        return true;
    }

    public boolean removeData(MediaBean mediaBean)
    {
        boolean remove = mSelectedMediaLiveData.getValue().remove(mediaBean);
        if (remove)
        {
            mSelectedMediaLiveData.postValue(mSelectedMediaLiveData.getValue());
        }
        return remove;
    }

    public int indexData(MediaBean mediaBean)
    {
        return mSelectedMediaLiveData.getValue().indexOf(mediaBean);
    }

    public void addObserver(LifecycleOwner owner, Observer<List<MediaBean>> observer)
    {
        mSelectedMediaLiveData.observe(owner, observer);
    }

    public void removeObservers(LifecycleOwner owner)
    {
        mSelectedMediaLiveData.removeObservers(owner);
    }

    public void clear()
    {
        mSelectedMediaLiveData.postValue(new LinkedList<>());
    }
}
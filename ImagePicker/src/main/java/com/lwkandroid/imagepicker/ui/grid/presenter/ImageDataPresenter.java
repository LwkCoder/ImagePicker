package com.lwkandroid.imagepicker.ui.grid.presenter;

import android.content.Context;
import android.media.ExifInterface;
import android.util.Log;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImageDataModel;
import com.lwkandroid.imagepicker.data.ImageFloderBean;
import com.lwkandroid.imagepicker.ui.grid.view.IImageDataView;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LWK
 * TODO ImageDataActivity的Presenter层
 */
public class ImageDataPresenter
{
    private IImageDataView mViewImpl;
    private ExecutorService mCachedThreadService = Executors.newCachedThreadPool();

    public ImageDataPresenter(IImageDataView view)
    {
        this.mViewImpl = view;
    }

    /**
     * 扫描图片
     */
    public void scanData(final Context context)
    {
        addNewRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                mViewImpl.showLoading();
                boolean success = ImageDataModel.getInstance().scanAllData(context);
                mViewImpl.hideLoading();
                if (!success)
                    mViewImpl.showShortToast(R.string.error_imagepicker_scanfail);
                mViewImpl.onFloderChanged(ImageDataModel.getInstance().getAllFloderList().get(0));
            }
        });
    }

    /**
     * 切换文件夹
     *
     * @param floderBean 文件夹对象
     */
    public void checkDataByFloder(final ImageFloderBean floderBean)
    {
        addNewRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                mViewImpl.onDataChanged(ImageDataModel.getInstance().getImagesByFloder(floderBean));
            }
        });
    }


    /**
     * 根据新图片路径创建ImageBean
     *
     * @param path 新图片路径
     * @return ImageBean
     */
    public ImageBean getImageBeanByPath(String path)
    {
        if (path == null || path.length() == 0)
            return null;

        try
        {
            File file = new File(path);

            ImageBean imageBean = new ImageBean();
            imageBean.setImagePath(path);
            imageBean.setLastModified(Long.valueOf(file.lastModified()));
            ExifInterface exifInterface = new ExifInterface(path);
            String width = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            String height = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            imageBean.setWidth(Integer.valueOf(width));
            imageBean.setHeight(Integer.valueOf(height));
            return imageBean;
        } catch (Exception e)
        {
            Log.e("ImagePicker", "ImageDataPresenter.getImageBeanByPath()--->" + e.toString());
        }

        return null;
    }

    //将子线程放到线程池中
    private void addNewRunnable(Runnable runnable)
    {
        mCachedThreadService.execute(runnable);
    }

    /**
     * 释放资源
     */
    public void onDestory()
    {
        ImageDataModel.getInstance().clear();
        mViewImpl = null;
    }
}

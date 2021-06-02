package com.lwkandroid.imagepicker.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.lwkandroid.imagepicker.utils.IImagePickerDisplayer;
import com.lwkandroid.imagepicker.utils.ImageComparator;
import com.lwkandroid.imagepicker.utils.ImagePickerComUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.collection.ArrayMap;

/**
 * 图片数据层
 */
public class ImageDataModel
{
    private ImageDataModel()
    {
    }

    private static final class ImageDataModelHolder
    {
        private static final ImageDataModel instance = new ImageDataModel();
    }

    public static ImageDataModel getInstance()
    {
        return ImageDataModelHolder.instance;
    }

    //所有图片
    private List<ImageBean> mAllImgList = new ArrayList<>();

    //所有文件夹List
    private List<ImageFolderBean> mAllFolderList = new ArrayList<>();

    //选中的图片List
    private List<ImageBean> mResultList = new ArrayList<>();

    //大图/预览模式下所有图片
    private List<ImageBean> mPagerList = new ArrayList<>();

    //图片显示器
    private IImagePickerDisplayer mDisPlayer;

    /**
     * 获取图片加载器对象
     *
     * @return 如果未设置则默认为GlideImagePickerDisPlayer
     */
    public IImagePickerDisplayer getDisPlayer()
    {
        return mDisPlayer != null ? mDisPlayer : (mDisPlayer = new GlideImagePickerDisplayer());
    }

    /**
     * 设置图片加载器对象
     *
     * @param player 需要实现IImagePickerDisPlayer接口
     */
    public void setDisPlayer(IImagePickerDisplayer player)
    {
        this.mDisPlayer = player;
    }

    /**
     * 获取所有图片数据List
     */
    public List<ImageBean> getAllImgList()
    {
        return mAllImgList;
    }

    /**
     * 获取所有文件夹数据List
     */
    public List<ImageFolderBean> getAllFolderList()
    {
        return mAllFolderList;
    }

    /**
     * 获取所有已选中图片数据List
     */
    public List<ImageBean> getResultList()
    {
        return mResultList;
    }

    /**
     * 添加新选中图片到结果中
     */
    public boolean addDataToResult(ImageBean imageBean)
    {
        if (mResultList != null)
            return mResultList.add(imageBean);
        return false;
    }

    /**
     * 移除已选中的某图片
     */
    public boolean delDataFromResult(ImageBean imageBean)
    {
        if (mResultList != null)
            return mResultList.remove(imageBean);
        return false;
    }

    /**
     * 判断是否已选中某张图
     */
    public boolean hasDataInResult(ImageBean imageBean)
    {
        if (mResultList != null)
            return mResultList.contains(imageBean);
        return false;
    }

    /**
     * 获取已选中的图片数量
     */
    public int getResultNum()
    {
        return mResultList != null ? mResultList.size() : 0;
    }

    /**
     * 临时存储大图/预览界面所有图片数据
     */
    public void setPagerDataList(List<ImageBean> list)
    {
        if (mPagerList == null)
            mPagerList = new ArrayList<>();
        mPagerList.clear();
        mPagerList.addAll(list);
    }

    /**
     * 获取大图/预览界面所有图片数据
     */
    public List<ImageBean> getPagerDataList()
    {
        return mPagerList;
    }

    /**
     * 清除大图/预览界面所有图片数据
     */
    public void clearPagerDataList()
    {
        if (mPagerList != null)
            mPagerList.clear();
    }

    /**
     * 扫描图片数据
     *
     * @param c context
     * @return 成功或失败
     */
    public boolean scanAllData(Context c)
    {
        try
        {
            Context context = c.getApplicationContext();
            //清空容器
            if (mAllImgList == null)
                mAllImgList = new ArrayList<>();
            if (mAllFolderList == null)
                mAllFolderList = new ArrayList<>();
            if (mResultList == null)
                mResultList = new ArrayList<>();
            mAllImgList.clear();
            mAllFolderList.clear();
            mResultList.clear();
            //创建“全部图片”的文件夹
            ImageFolderBean allImgFolder = new ImageFolderBean(
                    ImageContants.ID_ALL_IMAGE_FOLDER, context.getResources().getString(R.string.imagepicker_all_image_folder));
            mAllFolderList.add(allImgFolder);
            //临时存储所有文件夹对象的Map
            ArrayMap<String, ImageFolderBean> folderMap = new ArrayMap<>();

            //索引字段
            String columns[] =
                    new String[]{MediaStore.Images.Media._ID,//照片id
                            MediaStore.Images.Media.BUCKET_ID,//所属文件夹id
                            //                        MediaStore.Images.Media.PICASA_ID,
                            MediaStore.Images.Media.DATA,//图片地址
                            MediaStore.Images.Media.WIDTH,//图片宽度
                            MediaStore.Images.Media.HEIGHT,//图片高度
                            MediaStore.Images.Media.DISPLAY_NAME,//图片全名，带后缀
                            //                        MediaStore.Images.Media.TITLE,
                            //                        MediaStore.Images.Media.DATE_ADDED,//创建时间？
                            MediaStore.Images.Media.DATE_MODIFIED,//最后修改时间
                            //                        MediaStore.Images.Media.DATE_TAKEN,
                            //                        MediaStore.Images.Media.SIZE,//图片文件大小
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,//所属文件夹名字
                    };


            String selection = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?";
            //只筛选png、jpg、jpeg、PNG、JPG、JPEG
            String[] selectionArgs = {"image/png", "image/jpg", "image/jpeg", "image/PNG", "image/JPG", "image/JPEG"};
            String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
            //得到一个游标
            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs, sortOrder);

            if (cur != null && cur.moveToFirst())
            {
                //图片总数
                allImgFolder.setNum(cur.getCount());

                // 获取指定列的索引
                int imageIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int imagePathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                int imageModifyIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);
                int imageWidthIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH);
                int imageHeightIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);
                int imageNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                int folderIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
                int folderNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                do
                {
                    String imageId = cur.getString(imageIDIndex);
                    String imagePath = cur.getString(imagePathIndex);
                    String lastModify = cur.getString(imageModifyIndex);
                    String width = cur.getString(imageWidthIndex);
                    String height = cur.getString(imageHeightIndex);
                    String folderId = cur.getString(folderIdIndex);
                    String folderName = cur.getString(folderNameIndex);
                    String name = cur.getString(imageNameIndex);
                    if (TextUtils.isEmpty(name))
                    {
                        if (TextUtils.isEmpty(imagePath))
                            continue;
                        int index = imagePath.lastIndexOf(File.separator);
                        name = imagePath.substring(index + 1);
                        if (TextUtils.isEmpty(name))
                            continue;
                    }
                    //                    Log.e("ImagePicker", "imageId=" + imageId + "\n"
                    //                            + "imagePath=" + imagePath + "\n"
                    //                            + "lastModify=" + lastModify + "\n"
                    //                            + "width=" + width + "\n"
                    //                            + "height=" + height + "\n"
                    //                            + "name=" + name + "\n"
                    //                            + "folderId=" + folderId + "\n"
                    //                            + "folderName=" + folderName);

                    //创建图片对象
                    ImageBean imageBean = new ImageBean();
                    imageBean.setImageId(imageId);
                    imageBean.setImagePath(imagePath);
                    imageBean.setLastModified(ImagePickerComUtils.isNotEmpty(lastModify) ? Long.valueOf(lastModify) : 0);
                    imageBean.setWidth(ImagePickerComUtils.isNotEmpty(width) ? Integer.valueOf(width) : 0);
                    imageBean.setHeight(ImagePickerComUtils.isNotEmpty(height) ? Integer.valueOf(height) : 0);
                    imageBean.setFolderId(folderId);
                    mAllImgList.add(imageBean);
                    //更新文件夹对象
                    ImageFolderBean folderBean = null;
                    if (!TextUtils.isEmpty(folderId) && folderMap.containsKey(folderId))
                        folderBean = folderMap.get(folderId);
                    else
                        folderBean = new ImageFolderBean(folderId, folderName);
                    if (folderBean != null)
                    {
                        folderBean.setFirstImgPath(imagePath);
                        folderBean.gainNum();
                        folderMap.put(folderId, folderBean);
                    }
                } while (cur.moveToNext());
                cur.close();
            }

            //根据最后修改时间来降序排列所有图片
            Collections.sort(mAllImgList, new ImageComparator());
            //设置“全部图片”文件夹的第一张图片
            allImgFolder.setFirstImgPath(mAllImgList.size() != 0 ? mAllImgList.get(0).getImagePath() : null);
            //统一所有文件夹
            mAllFolderList.addAll(folderMap.values());

            return true;
        } catch (Exception e)
        {
            Log.e("ImagePicker", "ImagePicker scan data error:" + e);
            return false;
        }
    }

    /**
     * 根据文件夹获取该文件夹下所有图片数据
     *
     * @param folderBean 文件夹对象
     * @return 图片数据list
     */
    public List<ImageBean> getImagesByFolder(ImageFolderBean folderBean)
    {
        if (folderBean == null)
            return null;

        String folderId = folderBean.getFolderId();
        if (ImagePickerComUtils.isEquals(ImageContants.ID_ALL_IMAGE_FOLDER, folderId))
        {
            return mAllImgList;
        } else
        {
            ArrayList<ImageBean> resultList = new ArrayList<>();
            int size = mAllImgList.size();
            for (int i = 0; i < size; i++)
            {
                ImageBean imageBean = mAllImgList.get(i);
                if (imageBean != null && ImagePickerComUtils.isEquals(folderId, imageBean.getFolderId()))
                    resultList.add(imageBean);
            }
            return resultList;
        }
    }

    /**
     * 释放资源
     */
    public void clear()
    {
        mDisPlayer = null;
        if (mAllImgList != null)
            mAllImgList.clear();
        if (mAllFolderList != null)
            mAllFolderList.clear();
        if (mResultList != null)
            mResultList.clear();
        if (mPagerList != null)
            mPagerList.clear();
    }
}

package com.lwkandroid.imagepicker.ui.grid.view;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.base.activity.ImagePickerBaseActivity;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImageContants;
import com.lwkandroid.imagepicker.data.ImageDataModel;
import com.lwkandroid.imagepicker.data.ImageFolderBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.data.ImagePickerOptions;
import com.lwkandroid.imagepicker.ui.crop.ImageCropActivity;
import com.lwkandroid.imagepicker.ui.grid.adapter.ImageDataAdapter;
import com.lwkandroid.imagepicker.ui.grid.presenter.ImageDataPresenter;
import com.lwkandroid.imagepicker.ui.pager.view.ImagePagerActivity;
import com.lwkandroid.imagepicker.utils.ImagePickerComUtils;
import com.lwkandroid.imagepicker.utils.PermissionChecker;
import com.lwkandroid.imagepicker.utils.TakePhotoCompatUtils;
import com.lwkandroid.imagepicker.widget.ImagePickerActionBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static com.lwkandroid.imagepicker.data.ImageContants.REQUEST_CODE_PERMISSION_CAMERA;
import static com.lwkandroid.imagepicker.data.ImageContants.REQUEST_CODE_PERMISSION_SDCARD;
import static com.lwkandroid.imagepicker.utils.PermissionChecker.checkPermissions;

/**
 * 展示图片数据的Activity
 * [入口]
 */
public class ImageDataActivity extends ImagePickerBaseActivity implements IImageDataView
        , ImageFloderPop.onFloderItemClickListener, AbsListView.OnScrollListener
{

    private ImageDataPresenter mPresenter;
    private ImagePickerOptions mOptions;

    private ImagePickerActionBar mActionBar;
    private GridView mGridView;
    private ProgressBar mPgbLoading;
    private View mViewBottom;
    private View mViewFolder;
    private TextView mTvFolderName;
    private Button mBtnOk;
    private ImageDataAdapter mAdapter;
    private ImageFolderBean mCurFolder;
    private String mPhotoPath;
    private int mColumnWidth;
    private int mColumnNum;
    private Parcelable mState;

    @Override
    protected void beforeSetContentView(Bundle savedInstanceState)
    {
        super.beforeSetContentView(savedInstanceState);
        Intent intent = getIntent();
        mOptions = intent.getParcelableExtra(ImageContants.INTENT_KEY_OPTIONS);
    }

    @Override
    protected int getContentViewResId()
    {
        mPresenter = new ImageDataPresenter(this);
        return R.layout.activity_image_data;
    }

    @Override
    protected void initUI(View contentView)
    {
        if (mOptions == null)
        {
            showShortToast(R.string.error_imagepicker_lack_params);
            finish();
            return;
        }

        mActionBar = findView(R.id.acb_image_data);
        if (mOptions.getType() == ImagePickType.ONLY_CAMERA)
        {
            mActionBar.setTitle(R.string.imagepicker_title_take_photo);
            mActionBar.hidePreview();
            startTakePhoto();
        } else
        {
            mActionBar.setTitle(R.string.imagepicker_title_select_image);

            ViewStub viewStub = findView(R.id.vs_image_data);
            viewStub.inflate();
            mGridView = findView(R.id.gv_image_data);
            mGridView.setOnScrollListener(this);
            mPgbLoading = findView(R.id.pgb_image_data);
            mViewBottom = findView(R.id.fl_image_data_bottom);
            mViewFolder = findView(R.id.ll_image_data_bottom_floder);
            mTvFolderName = findView(R.id.tv_image_data_bottom_flodername);
            mBtnOk = findView(R.id.btn_image_data_ok);

            mViewFolder.setOnClickListener(this);
            if (mOptions.getType() == ImagePickType.SINGLE)
            {
                mBtnOk.setVisibility(View.GONE);
                mActionBar.hidePreview();
            } else
            {
                mActionBar.showPreview();
                mActionBar.setOnPreviewClickListener(this);
                mBtnOk.setVisibility(View.VISIBLE);
                mBtnOk.setOnClickListener(this);
                onSelectNumChanged(0);
            }
        }
    }

    @Override
    protected void initData()
    {
        if (mOptions == null)
        {
            return;
        }

        if (mOptions.getType() != ImagePickType.ONLY_CAMERA)
        {
            calColumn();
            mAdapter = new ImageDataAdapter(this, mColumnWidth, this);
            mGridView.setAdapter(mAdapter);
            doScanData();
        }
    }

    @Override
    public ImagePickerOptions getOptions()
    {
        return mOptions;
    }

    @Override
    public void startTakePhoto()
    {
        boolean hasPermissions = checkPermissions(this
                , new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , REQUEST_CODE_PERMISSION_CAMERA, R.string.dialog_imagepicker_permission_camera_message);
        //有权限就直接拍照
        if (hasPermissions)
        {
            //检查摄像头是否存在
            if (!TakePhotoCompatUtils.hasCamera())
            {
                showShortToast(R.string.error_no_camera);
                return;
            }

            doTakePhoto();
        }
    }

    //执行拍照的方法
    private void doTakePhoto()
    {
        mPhotoPath = TakePhotoCompatUtils.takePhoto(this, ImageContants.REQUEST_CODE_TAKE_PHOTO, mOptions.getCachePath());
    }

    //执行扫描sd卡的方法
    private void doScanData()
    {
        if (!ImagePickerComUtils.isSdExist())
        {
            showShortToast(R.string.error_no_sdcard);
            return;
        }

        boolean hasPermission = PermissionChecker.checkPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSION_SDCARD, R.string.dialog_imagepicker_permission_sdcard_message);
        //有权限直接扫描
        if (hasPermission)
        {
            mPresenter.scanData(this);
        }
    }

    @Override
    public void showLoading()
    {
        if (mPgbLoading != null)
        {
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mPgbLoading.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void hideLoading()
    {
        if (mPgbLoading != null)
        {
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mPgbLoading.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onDataChanged(final List<ImageBean> dataList)
    {
        if (mGridView != null && mAdapter != null)
        {
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mGridView.setVisibility(View.VISIBLE);
                    mAdapter.refreshDatas(dataList);
                    mGridView.setSelection(0);
                }
            });
        }
    }

    @Override
    public void onFloderChanged(ImageFolderBean floderBean)
    {
        if (mCurFolder != null && floderBean != null && mCurFolder.equals(floderBean))
            return;

        mCurFolder = floderBean;
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (mTvFolderName != null)
                {
                    mTvFolderName.setText(mCurFolder.getFolderName());
                }
            }
        });
        mPresenter.checkDataByFloder(floderBean);
    }

    @Override
    public void onImageClicked(ImageBean imageBean, int position)
    {
        if (mOptions.getType() == ImagePickType.SINGLE)
        {
            if (mOptions.isNeedCrop())
            {
                //执行裁剪
                ImageCropActivity.start(this, imageBean.getImagePath(), mOptions);
            } else
            {
                returnSingleImage(imageBean);
            }
        } else
        {
            //去查看大图的界面
            //如果有相机入口需要调整传递的数据
            int p = position;
            ArrayList<ImageBean> dataList = new ArrayList<>();
            dataList.addAll(mAdapter.getDatas());
            if (mOptions.isNeedCamera())
            {
                p--;
                dataList.remove(0);
            }
            ImagePagerActivity.start(this, dataList, p, mOptions, ImageContants.REQUEST_CODE_DETAIL);
        }
    }

    @Override
    public void onSelectNumChanged(int curNum)
    {
        mBtnOk.setText(getString(R.string.btn_imagepicker_ok, String.valueOf(curNum), String.valueOf(mOptions.getMaxNum())));
        if (curNum == 0)
        {
            mBtnOk.setEnabled(false);
            mActionBar.enablePreview(false);
        } else
        {
            mBtnOk.setEnabled(true);
            mActionBar.enablePreview(true);
        }
    }

    @Override
    public void warningMaxNum()
    {
        showShortToast(getString(R.string.warning_imagepicker_max_num, String.valueOf(mOptions.getMaxNum())));
    }

    @Override
    protected void onClick(View v, int id)
    {
        if (id == R.id.tv_imagepicker_actionbar_preview)
        {
            //去预览界面
            ImagePagerActivity.start(this, (ArrayList<ImageBean>) ImageDataModel.getInstance().getResultList()
                    , 0, mOptions, ImageContants.REQUEST_CODE_PREVIEW);
        } else if (id == R.id.ll_image_data_bottom_floder)
        {
            //弹出文件夹切换菜单
            new ImageFloderPop().showAtBottom(this, mContentView, mCurFolder, this);
        } else if (id == R.id.btn_image_data_ok)
        {
            //返回选中的图片
            returnAllSelectedImages();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("ImagePicker", "ImageDataActivity.onActivityResult--->requestCode=" + requestCode
                + ",resultCode=" + resultCode + ",data=" + data);

        //拍照返回
        if (requestCode == ImageContants.REQUEST_CODE_TAKE_PHOTO)
        {
            if (resultCode != RESULT_OK)
            {
                Log.e("ImagePicker", "ImageDataActivity take photo result not OK !!!");
                if (mOptions.getType() == ImagePickType.ONLY_CAMERA)
                {
                    finish();
                }
                return;
            }

            Log.i("ImagePicker", "ImageDataActivity take photo result OK--->" + mPhotoPath);
            //非多选模式下需要判断是否有裁剪的需求
            if (mOptions.getType() != ImagePickType.MULTI && mOptions.isNeedCrop())
            {
                //执行裁剪
                ImageCropActivity.start(this, mPhotoPath, mOptions);
            } else
            {
                returnSingleImage(mPresenter.getImageBeanByPath(mPhotoPath));
            }
        }
        //裁剪返回
        if (requestCode == ImageContants.REQUEST_CODE_CROP)
        {
            if (resultCode == RESULT_OK)
            {
                //裁剪成功返回数据
                String cropPath = data.getStringExtra(ImageContants.INTENT_KEY_CROP_PATH);
                returnSingleImage(mPresenter.getImageBeanByPath(cropPath));
            } else if (mOptions.getType() == ImagePickType.ONLY_CAMERA)
            {
                finish();
            }
        }
        //预览或者大图界面返回
        else if (requestCode == ImageContants.REQUEST_CODE_PREVIEW
                || requestCode == ImageContants.REQUEST_CODE_DETAIL)
        {
            if (resultCode == RESULT_OK)
            {
                returnAllSelectedImages();
            } else
            {
                //刷新视图
                mAdapter.notifyDataSetChanged();
                onSelectNumChanged(ImageDataModel.getInstance().getResultNum());
            }
        }
    }

    @Override
    public void onFloderItemClicked(ImageFolderBean floderBean)
    {
        onFloderChanged(floderBean);
    }

    //返回单张图片数据
    private void returnSingleImage(ImageBean imageBean)
    {
        ArrayList<ImageBean> list = new ArrayList<>();
        list.add(imageBean);
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA, list);
        setResult(RESULT_OK, intent);
        finish();
    }

    //返回所有已选中的图片
    private void returnAllSelectedImages()
    {
        ArrayList<ImageBean> resultList = new ArrayList<>();
        resultList.addAll(ImageDataModel.getInstance().getResultList());

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA, resultList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        boolean[] result;
        switch (requestCode)
        {
            case ImageContants.REQUEST_CODE_PERMISSION_CAMERA:
                if (mOptions.getType() == ImagePickType.ONLY_CAMERA)
                {
                    result = PermissionChecker.onRequestPermissionsResult(this, permissions, grantResults, true
                            , R.string.dialog_imagepicker_permission_camera_nerver_ask_message);
                    if (result[0])
                    {
                        doTakePhoto();
                    } else if (!result[1])
                    {
                        finish();
                    }
                } else
                {
                    result = PermissionChecker.onRequestPermissionsResult(this, permissions, grantResults, false
                            , R.string.dialog_imagepicker_permission_camera_nerver_ask_message);
                    if (result[0])
                    {
                        doTakePhoto();
                    }
                }
                break;
            case ImageContants.REQUEST_CODE_PERMISSION_SDCARD:
                result = PermissionChecker.onRequestPermissionsResult(this, permissions, grantResults, false
                        , R.string.dialog_imagepicker_permission_sdcard_nerver_ask_message);
                //                if (result[0])
                //                    mPresenter.scanData(this);
                //无论成功失败都去扫描，以便更新视图
                mPresenter.scanData(this);
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        calColumn();
        if (mGridView != null && mState != null)
        {
            mGridView.onRestoreInstanceState(mState);
        }
    }

    //计算列数和每列宽度
    private void calColumn()
    {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int densityDpi = metrics.densityDpi;
        int orientation = getResources().getConfiguration().orientation;
        int minColumn = orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3;

        //计算列数
        mColumnNum = screenWidth / densityDpi;
        mColumnNum = mColumnNum < minColumn ? minColumn : mColumnNum;
        //计算每列宽度
        int columnSpace = (int) (2 * metrics.density);
        mColumnWidth = (screenWidth - columnSpace * (mColumnNum - 1)) / mColumnNum;

        if (mGridView != null)
        {
            mGridView.setColumnWidth(mColumnWidth);
            mGridView.setNumColumns(mColumnNum);
        }
        if (mAdapter != null)
        {
            mAdapter.adjustLayoutSize(mColumnWidth);
        }
    }

    @Override
    protected void onDestroy()
    {
        mPresenter.onDestory();
        super.onDestroy();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        //记录下滚动位置
        mState = view.onSaveInstanceState();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        //nothing to do
    }
}

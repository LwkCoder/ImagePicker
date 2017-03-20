package com.lwkandroid.imagepicker.ui.grid.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.base.activity.ImagePickerBaseActivity;
import com.lwkandroid.imagepicker.data.Contants;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImageDataModel;
import com.lwkandroid.imagepicker.data.ImageFloderBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.data.ImagePickerOptions;
import com.lwkandroid.imagepicker.ui.grid.adapter.ImageDataAdapter;
import com.lwkandroid.imagepicker.ui.grid.presenter.ImageDataPresenter;
import com.lwkandroid.imagepicker.utils.ImagePickerComUtils;
import com.lwkandroid.imagepicker.utils.PermissionChecker;
import com.lwkandroid.imagepicker.utils.TakePhotoCompatUtils;
import com.lwkandroid.imagepicker.widget.ImagePickerActionBar;

import java.util.ArrayList;
import java.util.List;

import static com.lwkandroid.imagepicker.utils.PermissionChecker.checkPermissions;

/**
 * 展示图片数据的Activity
 */
public class ImageDataActivity extends ImagePickerBaseActivity implements IImageDataView, ImageFloderPop.onFloderItemClickListener
{

    //sdk23获取sd卡读写权限的requestCode
    private static final int REQUEST_CODE_PERMISSION_SDCARD = 110;
    //sdk23获取sd卡拍照权限的requestCode
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 111;
    //拍照请求码
    private static final int REQUEST_CODE_TAKE_PHOTO = 112;
    //裁剪请求码
    private static final int REQUEST_CODE_CROP = 113;

    private ImageDataPresenter mPresenter;
    private ImagePickerOptions mOptions;
    //    private ImagePickType mPickType;
    private int mResultCode;

    private ImagePickerActionBar mActionBar;
    private GridView mGridView;
    private ProgressBar mPgbLoading;
    private View mViewBottom;
    private View mViewFloder;
    private TextView mTvFloderName;
    private Button mBtnOk;
    private ImageDataAdapter mAdapter;
    private ImageFloderBean mCurFloder;
    private String mPhotoPath;

    @Override
    protected void beforSetContentView(Bundle savedInstanceState)
    {
        super.beforSetContentView(savedInstanceState);
        Intent intent = getIntent();
        mOptions = intent.getParcelableExtra(Contants.INTENT_OPTIONS_KEY);
        mResultCode = intent.getIntExtra(Contants.INTENT_RESULTCODE_KEY, ImagePicker.DEF_RESULT_CODE);
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
            ViewStub viewStub = findView(R.id.vs_image_data);
            viewStub.inflate();
            mGridView = findView(R.id.gv_image_data);
            mPgbLoading = findView(R.id.pgb_image_data);
            mViewBottom = findView(R.id.fl_image_data_bottom);
            mViewFloder = findView(R.id.ll_image_data_bottom_floder);
            mTvFloderName = findView(R.id.tv_image_data_bottom_flodername);
            mBtnOk = findView(R.id.btn_image_data_ok);

            mViewFloder.setOnClickListener(this);
            if (mOptions.getType() == ImagePickType.SINGLE)
            {
                mBtnOk.setVisibility(View.GONE);
                mActionBar.hidePreview();
            } else
            {
                mBtnOk.setVisibility(View.VISIBLE);
                onSelectNumChanged(0);
                mBtnOk.setOnClickListener(this);
            }
        }
    }

    @Override
    protected void initData()
    {
        if (mOptions == null)
            return;

        if (mOptions.getType() != ImagePickType.ONLY_CAMERA)
        {
            mAdapter = new ImageDataAdapter(this, this);
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
        if (!ImagePickerComUtils.isSdExist())
        {
            showShortToast(R.string.error_no_sdcard);
            return;
        }

        boolean hasPermissions = checkPermissions(this
                , new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , REQUEST_CODE_PERMISSION_CAMERA, R.string.dialog_imagepicker_permission_camera_message);
        //有权限就直接拍照
        if (hasPermissions)
            doTakePhoto();
    }

    //执行拍照的方法
    private void doTakePhoto()
    {
        mPhotoPath = TakePhotoCompatUtils.takePhoto(this, REQUEST_CODE_TAKE_PHOTO, mOptions.getCachePath());
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
            mPresenter.scanData(this);
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
                    mGridView.scrollTo(0, 0);
                }
            });
        }
    }

    @Override
    public void onFloderChanged(ImageFloderBean floderBean)
    {
        if (mCurFloder != null && floderBean != null && mCurFloder.equals(floderBean))
            return;

        mCurFloder = floderBean;
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (mTvFloderName != null)
                    mTvFloderName.setText(mCurFloder.getFloderName());
            }
        });
        mPresenter.checkDataByFloder(floderBean);
    }

    @Override
    public void onImageClicked(ImageBean imageBean)
    {
        if (mOptions.getType() == ImagePickType.SINGLE)
        {
            if (mOptions.isNeedCrop())
            {
                //TODO 执行裁剪
            } else
            {
                returnSingleImage(imageBean);
            }
        }
        //多选模式下进入大图查看界面
        else
        {
            //TODO 去查看大图的界面
        }
    }

    @Override
    public void onSelectNumChanged(int curNum)
    {
        if (mBtnOk != null)
        {
            mBtnOk.setText(getString(R.string.btn_imagepicker_ok, String.valueOf(curNum), String.valueOf(mOptions.getLimitNum())));
            if (curNum == 0)
                mBtnOk.setEnabled(false);
            else
                mBtnOk.setEnabled(true);
        }
    }

    @Override
    public void warningLimitNum()
    {
        showShortToast(getString(R.string.warning_imagepicker_limit_num, String.valueOf(mOptions.getLimitNum())));
    }

    @Override
    protected void onClick(View v, int id)
    {
        if (id == R.id.ll_image_data_bottom_floder)
        {
            //弹出文件夹切换菜单
            new ImageFloderPop().showAtBottom(this, mContentView, mCurFloder, this);
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

        if (resultCode != RESULT_OK)
        {
            if (mOptions.getType() == ImagePickType.ONLY_CAMERA)
                finish();
            return;
        }

        //拍照返回
        if (requestCode == REQUEST_CODE_TAKE_PHOTO)
        {
            //非多选模式下需要判断是否有裁剪的需求
            if (mOptions.getType() != ImagePickType.MUTIL && mOptions.isNeedCrop())
            {
                //TODO 执行裁剪
            } else
            {
                returnSingleImage(mPresenter.getImageBeanByPath(mPhotoPath));
            }
        }
    }

    @Override
    public void onFloderItemClicked(ImageFloderBean floderBean)
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
        setResult(mResultCode, intent);
        finish();
    }

    //返回所有已选中的图片
    private void returnAllSelectedImages()
    {
        ArrayList<ImageBean> resultList = new ArrayList<>();
        resultList.addAll(ImageDataModel.getInstance().getResultList());

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA, resultList);
        setResult(mResultCode, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        boolean[] result;
        switch (requestCode)
        {
            case REQUEST_CODE_PERMISSION_CAMERA:
                if (mOptions.getType() == ImagePickType.ONLY_CAMERA)
                {
                    result = PermissionChecker.onRequestPermissionsResult(this, permissions, grantResults, true
                            , R.string.dialog_imagepicker_permission_camera_nerver_ask_message);
                    if (result[0])
                        doTakePhoto();
                    else if (!result[1])
                        finish();
                } else
                {
                    result = PermissionChecker.onRequestPermissionsResult(this, permissions, grantResults, false
                            , R.string.dialog_imagepicker_permission_camera_nerver_ask_message);
                    if (result[0])
                        doTakePhoto();
                }
                break;
            case REQUEST_CODE_PERMISSION_SDCARD:
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
    protected void onDestroy()
    {
        mPresenter.onDestory();
        super.onDestroy();
    }
}

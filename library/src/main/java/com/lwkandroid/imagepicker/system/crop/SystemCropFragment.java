package com.lwkandroid.imagepicker.system.crop;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.config.SystemCropOptions;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.common.AbsMediatorFragment;
import com.lwkandroid.imagepicker.constants.ErrorCode;
import com.lwkandroid.imagepicker.utils.Utils;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * @description: 执行系统裁剪的Fragment
 * @author: LWK
 * @date: 2021/6/3 10:02
 */
public class SystemCropFragment extends AbsMediatorFragment<SystemCropOptions, File>
{
    private static final int REQUEST_CODE_CROP = 202;
    //华为手机标识
    private static final String HUAWEI = "HUAWEI";
    private File mResultFile;

    public SystemCropFragment(SystemCropOptions options, PickCallBack<File> callback)
    {
        super(options, callback);
    }

    public static void create(FragmentActivity activity, SystemCropOptions options, PickCallBack<File> callBack)
    {
        SystemCropFragment fragment = new SystemCropFragment(options, callBack);
        // 设置保留实例，不会因为屏幕方向或配置变化而重新创建
        fragment.setRetainInstance(true);
        fragment.attachActivity(activity);
    }

    @Override
    protected void doRequest()
    {
        //申请权限
        XXPermissions.with(this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback()
                {
                    @Override
                    public void onGranted(List<String> permissions, boolean all)
                    {
                        if (all)
                        {
                            File imageFile = getOption().getImageFile();
                            Uri imageUri = Utils.file2Uri(getContext(), imageFile);
                            Intent intent = new Intent("com.android.camera.action.CROP");
                            intent.setDataAndType(imageUri, "image/*");
                            intent.putExtra("crop", true);
                            //设置裁剪框比例
                            int aspectX = getOption().getAspectX();
                            int aspectY = getOption().getAspectY();
                            if (aspectX == aspectY)
                            {
                                //比例相等，代表需要正方形，但在华为手机上会显示为圆形裁剪框，需要额外处理这种情况
                                if (HUAWEI.equalsIgnoreCase(Build.MANUFACTURER)
                                        || android.os.Build.MODEL.contains(HUAWEI))
                                {
                                    aspectX = 9998;
                                    aspectY = 9999;
                                }
                            }
                            intent.putExtra("aspectX", aspectX);
                            intent.putExtra("aspectY", aspectY);
                            //设置输出图片的尺寸
                            intent.putExtra("outputX", getOption().getOutputX());
                            intent.putExtra("outputY", getOption().getOutputY());
                            //设置为不返回缩略图
                            intent.putExtra("return-data", false);
                            //设置输出图片的格式
                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                            //设置前置摄像头？？
                            intent.putExtra("noFaceDetection", false);
                            //设置开启缩放
                            intent.putExtra("scale", true);

                            File cacheDirFile = new File(Utils.getAvailableCacheDirPath(getContext(), getOption().getCacheDirPath()));
                            mResultFile = new File(cacheDirFile.getAbsolutePath(), "CROP_" + System.currentTimeMillis() + ".jpg");
                            Uri resultUri = Utils.file2Uri(getContext(), mResultFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, resultUri);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            {
                                //开启临时权限
                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, resultUri));
                            }

                            startActivityForResult(intent, REQUEST_CODE_CROP);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never)
                    {
                        String errorMessage = getString(R.string.permission_denied_of_crop_image);
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        if (never && getActivity() != null)
                        {
                            XXPermissions.startPermissionActivity(getActivity(), permissions);
                        }
                        invokeFailCallBack(ErrorCode.PERMISSION_DENIED, errorMessage);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_CROP == requestCode)
        {
            if (Activity.RESULT_OK == resultCode)
            {
                invokeSuccessCallBack(mResultFile);
            } else
            {
                detachActivity(getActivity());
            }
        }
    }
}

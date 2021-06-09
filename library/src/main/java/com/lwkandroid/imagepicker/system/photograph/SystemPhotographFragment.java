package com.lwkandroid.imagepicker.system.photograph;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.options.SystemPhotographOptions;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.common.AbsMediatorFragment;
import com.lwkandroid.imagepicker.constants.ErrorCode;
import com.lwkandroid.imagepicker.utils.Utils;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * @description: 执行系统拍照的Fragment
 * @author: LWK
 * @date: 2021/6/1 15:10
 */
public final class SystemPhotographFragment extends AbsMediatorFragment<SystemPhotographOptions, File>
{
    private static final int REQUEST_CODE_PHOTOGRAPH = 200;
    private File mResultFile;

    public SystemPhotographFragment(SystemPhotographOptions options, PickCallBack<File> callback)
    {
        super(options, callback);
    }

    public static void create(FragmentActivity activity, SystemPhotographOptions options, PickCallBack<File> callBack)
    {
        SystemPhotographFragment fragment = new SystemPhotographFragment(options, callBack);
        // 设置保留实例，不会因为屏幕方向或配置变化而重新创建
        fragment.setRetainInstance(true);
        fragment.attachActivity(activity);
    }

    @Override
    protected void doRequest()
    {
        //申请权限
        XXPermissions.with(this)
                .permission(Permission.CAMERA)
                .request(new OnPermissionCallback()
                {
                    @Override
                    public void onGranted(List<String> permissions, boolean all)
                    {
                        if (all)
                        {
                            doPhotograph();
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never)
                    {
                        String errorMessage = getString(R.string.permission_denied_of_take_photo);
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
        if (REQUEST_CODE_PHOTOGRAPH == requestCode)
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

    private void doPhotograph()
    {
        if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            try
            {
                File cacheDirFile = new File(Utils.getAvailableCacheDirPath(getContext(), getOption().getCacheDirPath()));
                mResultFile = new File(cacheDirFile.getAbsolutePath(), "IMG_" + System.currentTimeMillis() + ".jpg");
                Uri resultUri = Utils.file2Uri(getContext(), mResultFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, resultUri);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(intent, REQUEST_CODE_PHOTOGRAPH);
            } catch (Exception e)
            {
                e.printStackTrace();
                String errorMessage = getString(R.string.error_can_not_takephoto);
                invokeFailCallBack(ErrorCode.UNABLE_TO_PHOTOGRAPH, errorMessage);
            }
        } else
        {
            invokeFailCallBack(ErrorCode.CAMEAR_UNAVAILABLE, getString(R.string.error_no_camera));
        }
    }

}

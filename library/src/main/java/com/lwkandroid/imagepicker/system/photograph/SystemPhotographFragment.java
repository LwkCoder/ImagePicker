package com.lwkandroid.imagepicker.system.photograph;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.common.AbsMediatorFragment;
import com.lwkandroid.imagepicker.config.SystemPhotographOptions;
import com.lwkandroid.imagepicker.constants.ErrorCode;
import com.lwkandroid.imagepicker.utils.Utils;

import java.io.File;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * @description: 执行系统拍照的Fragment
 * @author: LWK
 * @date: 2021/6/1 15:10
 */
public final class SystemPhotographFragment extends AbsMediatorFragment<SystemPhotographOptions, File>
{
    private File mResultFile;
    private ActivityResultLauncher<Uri> mLauncher;

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
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture()
        {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, @NonNull Uri input)
            {
                Intent intent = super.createIntent(context, input);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                return intent;
            }
        }, result -> {
            if (result)
            {
                invokeSuccessCallBack(mResultFile);
            } else
            {
                detachActivity(getActivity());
            }
        });
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

    private void doPhotograph()
    {
        if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
        {
            try
            {
                File cacheDirFile = new File(Utils.getAvailableCacheDirPath(getContext(), getOption().getCacheDirPath()));
                mResultFile = new File(cacheDirFile.getAbsolutePath(), "IMG_" + System.currentTimeMillis() + ".jpg");
                Uri resultUri = Utils.file2Uri(getContext(), mResultFile);
                mLauncher.launch(resultUri);
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

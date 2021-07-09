package com.lwkandroid.imagepicker.custom.crop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.common.AbsMediatorFragment;
import com.lwkandroid.imagepicker.config.CustomCropImageOptions;
import com.lwkandroid.imagepicker.constants.ErrorCode;
import com.lwkandroid.imagepicker.constants.ImageConstants;
import com.lwkandroid.imagepicker.utils.Utils;

import java.io.File;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * @description: 执行自定义裁剪的Fragment
 * @author: LWK
 * @date: 2021/7/6 14:24
 */
public class CustomCropImageFragment extends AbsMediatorFragment<CustomCropImageOptions, File>
{
    private ActivityResultLauncher<CustomCropImageOptions> mLauncher;
    private File mResultFile;

    public static void create(FragmentActivity activity, CustomCropImageOptions options, PickCallBack<File> callBack)
    {
        CustomCropImageFragment fragment = new CustomCropImageFragment(options, callBack);
        // 设置保留实例，不会因为屏幕方向或配置变化而重新创建
        fragment.setRetainInstance(true);
        fragment.attachActivity(activity);
    }

    public CustomCropImageFragment(CustomCropImageOptions options, PickCallBack<File> callback)
    {
        super(options, callback);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        File cacheDirFile = new File(Utils.getAvailableCacheDirPath(getContext(), getOption().getCacheDirPath()));
        mResultFile = new File(cacheDirFile.getAbsolutePath(), "CROP_" + System.currentTimeMillis() + ".jpg");

        mLauncher = registerForActivityResult(new ActivityResultContract<CustomCropImageOptions, Boolean>()
        {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, CustomCropImageOptions input)
            {
                Intent intent = new Intent(context, CustomCropImageActivity.class);
                intent.putExtra(ImageConstants.KEY_INTENT_OPTIONS, input);
                intent.putExtra(ImageConstants.KEY_INTENT_FILE, Utils.file2Uri(context, mResultFile));
                return intent;
            }

            @Override
            public Boolean parseResult(int resultCode, @Nullable Intent intent)
            {
                return Activity.RESULT_OK == resultCode;
            }
        }, success -> {
            if (success)
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
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback()
                {
                    @Override
                    public void onGranted(List<String> permissions, boolean all)
                    {
                        mLauncher.launch(getOption());
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
}

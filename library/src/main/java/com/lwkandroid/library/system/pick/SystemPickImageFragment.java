package com.lwkandroid.library.system.pick;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.library.bean.SystemPickImageOptions;
import com.lwkandroid.library.callback.PickCallBack;
import com.lwkandroid.library.common.AbsMediatorFragment;
import com.lwkandroid.library.constants.ErrorCode;
import com.lwkandroid.library.utils.Utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * @description: 执行从系统相册选择图片的Fragment
 * @author: LWK
 * @date: 2021/6/2 14:00
 */
public class SystemPickImageFragment extends AbsMediatorFragment<SystemPickImageOptions, List<File>>
{
    private static final int REQUEST_CODE_PICK_IMAGE = 201;

    public SystemPickImageFragment(SystemPickImageOptions options, PickCallBack<List<File>> callback)
    {
        super(options, callback);
    }

    public static void create(FragmentActivity activity, SystemPickImageOptions options, PickCallBack<List<File>> callBack)
    {
        SystemPickImageFragment fragment = new SystemPickImageFragment(options, callBack);
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
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, Math.max(1, getOption().getMaxNumber()) > 1);
                            startActivityForResult(Intent.createChooser(intent, getContext().getPackageName()), REQUEST_CODE_PICK_IMAGE);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never)
                    {
                        String errorMessage = getString(R.string.permission_denied_of_pick_image);
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
        if (REQUEST_CODE_PICK_IMAGE == requestCode)
        {
            if (Activity.RESULT_OK == resultCode)
            {
                List<File> fileList = new LinkedList<>();
                //选择了单张图片时的结果
                Uri singleImageUri = data.getData();
                //选择了多张图片时的结果
                ClipData multiImageData = data.getClipData();
                if (singleImageUri != null)
                {
                    File imageFile = Utils.uri2File(getContext(), singleImageUri);
                    if (imageFile != null)
                    {
                        fileList.add(imageFile);
                        invokeSuccessCallBack(fileList);
                    } else
                    {
                        invokeFailCallBack(ErrorCode.UNKNOWN_ERROR, getString(R.string.unknown_error));
                    }
                } else if (multiImageData != null)
                {
                    //所选数量不得超过最大数量限制
                    int count = Math.min(getOption().getMaxNumber(), multiImageData.getItemCount());
                    for (int i = 0; i < count; i++)
                    {
                        File imageFile = Utils.uri2File(getContext(), multiImageData.getItemAt(i).getUri());
                        if (imageFile != null)
                        {
                            fileList.add(imageFile);
                        }
                    }
                    invokeSuccessCallBack(fileList);
                } else
                {
                    invokeFailCallBack(ErrorCode.UNKNOWN_ERROR, getString(R.string.unknown_error));
                }
            } else
            {
                detachActivity(getActivity());
            }
        }
    }
}

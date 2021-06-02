package com.lwkandroid.library.system.pick;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
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
public class SystemPickImageFragment extends AbsMediatorFragment
{
    private static final int REQUEST_CODE_PICK_IMAGE = 201;
    private SystemPickImageOptions mOption;
    private PickCallBack<List<File>> mCallBack;

    public SystemPickImageFragment(SystemPickImageOptions options, PickCallBack<List<File>> callBack)
    {
        this.mOption = options;
        this.mCallBack = callBack;
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
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, Math.max(1, mOption.getMaxNumber()) > 1);
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
                    String imagePath = getImagePathFromUri(singleImageUri);
                    fileList.add(new File(imagePath));
                    invokeSuccessCallBack(fileList);
                } else if (multiImageData != null)
                {
                    //所选数量不得超过最大数量限制
                    int count = Math.min(mOption.getMaxNumber(), multiImageData.getItemCount());
                    for (int i = 0; i < count; i++)
                    {
                        String imagePath = getImagePathFromUri(multiImageData.getItemAt(i).getUri());
                        fileList.add(new File(imagePath));
                    }
                    invokeSuccessCallBack(fileList);
                } else
                {
                    detachActivity(getActivity());
                }
            } else
            {
                detachActivity(getActivity());
            }
        }
    }

    private String getImagePathFromUri(Uri uri)
    {
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(getContext(), uri))
        {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority()))
            {
                String id = docId.split(":")[1];
                // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority()))
            {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }

        return imagePath;
    }

    private String getImagePath(Uri uri, String selection)
    {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void invokeSuccessCallBack(List<File> fileList)
    {
        if (mCallBack != null)
        {
            mCallBack.onPickSuccess(fileList);
        }
        detachActivity(getActivity());
    }

    private void invokeFailCallBack(int code, String message)
    {
        if (mCallBack != null)
        {
            mCallBack.onPickFailed(code, message);
        }
        detachActivity(getActivity());
    }

}

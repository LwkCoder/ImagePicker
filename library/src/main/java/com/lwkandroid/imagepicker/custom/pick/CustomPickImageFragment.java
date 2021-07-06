package com.lwkandroid.imagepicker.custom.pick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.PickResultBean;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.common.AbsMediatorFragment;
import com.lwkandroid.imagepicker.config.CustomPickImageOptions;
import com.lwkandroid.imagepicker.constants.ErrorCode;
import com.lwkandroid.imagepicker.constants.ImageConstants;

import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * @description: 执行从自定义界面选择图片的Fragment
 * @author: LWK
 * @date: 2021/6/9 13:39
 */
public class CustomPickImageFragment extends AbsMediatorFragment<CustomPickImageOptions, PickResultBean>
{
    private ActivityResultLauncher<CustomPickImageOptions> mLauncher;

    public CustomPickImageFragment(CustomPickImageOptions options, PickCallBack<PickResultBean> callback)
    {
        super(options, callback);
    }

    public static void create(FragmentActivity activity, CustomPickImageOptions options, PickCallBack<PickResultBean> callBack)
    {
        CustomPickImageFragment fragment = new CustomPickImageFragment(options, callBack);
        // 设置保留实例，不会因为屏幕方向或配置变化而重新创建
        fragment.setRetainInstance(true);
        fragment.attachActivity(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mLauncher = registerForActivityResult(new ActivityResultContract<CustomPickImageOptions, Boolean>()
        {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, CustomPickImageOptions input)
            {
                Intent intent = new Intent(context, GridPickImageActivity.class);
                intent.putExtra(ImageConstants.KEY_INTENT_OPTIONS, input);
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
                PickResultBean resultBean = new PickResultBean();
                resultBean.setMediaList(PickTempStorage.getInstance().getSelectedMediaLiveData().getValue());
                resultBean.setOriginalFile(PickTempStorage.getInstance().getOriginFileStateLiveData().getValue());
                invokeSuccessCallBack(resultBean);
            } else
            {
                detachActivity(getActivity());
            }
            PickTempStorage.getInstance().clear();
        });
    }

    @Override
    protected void doRequest()
    {
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
}

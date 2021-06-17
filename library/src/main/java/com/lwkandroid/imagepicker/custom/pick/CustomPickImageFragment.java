package com.lwkandroid.imagepicker.custom.pick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.bean.PickResultBean;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.common.AbsMediatorFragment;
import com.lwkandroid.imagepicker.config.CustomPickImageOptions;
import com.lwkandroid.imagepicker.constants.ImageConstants;

import java.util.LinkedList;
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
    private static final int REQUEST_CODE_PICK = 100;
    private ActivityResultLauncher<CustomPickImageOptions> mActivityLauncher;

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
        mActivityLauncher = registerForActivityResult(new ActivityResultContract<CustomPickImageOptions, List<MediaBean>>()
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
            public List<MediaBean> parseResult(int resultCode, @Nullable Intent intent)
            {
                if (Activity.RESULT_OK == resultCode)
                {
                    return intent.getParcelableArrayListExtra(ImageConstants.KEY_INTENT_RESULT);
                } else
                {
                    return new LinkedList<>();
                }
            }
        }, result -> {
            if (result != null && result.size() > 0)
            {
                PickResultBean resultBean = new PickResultBean();
                resultBean.setMediaList(result);
                invokeSuccessCallBack(resultBean);
            } else
            {
                detachActivity(getActivity());
            }
        });
    }

    @Override
    protected void doRequest()
    {
        //跳转到Activity
        mActivityLauncher.launch(getOption());
    }
}

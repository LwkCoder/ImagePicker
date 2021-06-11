package com.lwkandroid.imagepicker.custom.pick;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.BucketBean;
import com.lwkandroid.imagepicker.callback.PickCallBack;
import com.lwkandroid.imagepicker.config.CustomPickImageOptions;
import com.lwkandroid.imagepicker.config.CustomPickImageStyle;
import com.lwkandroid.imagepicker.constants.ImageConstants;
import com.lwkandroid.imagepicker.custom.model.MediaLoaderEngine;
import com.lwkandroid.imagepicker.utils.Utils;
import com.lwkandroid.rcvadapter.ui.RcvLoadingView;
import com.lwkandroid.widget.ComActionBar;
import com.lwkandroid.widget.StateFrameLayout;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 网格图片界面
 *
 * @author LWK
 */
public class GridImageActivity extends AppCompatActivity
{
    private static final int PAGE_SIZE = 50;

    private CustomPickImageOptions mOptions;

    private List<BucketBean> mAllBucketList;

    private LinearLayout mRootLinearLayout;
    private ComActionBar mActionBar;
    private RecyclerView mRecyclerView;
    private LinearLayout mBottomLinearLayout;
    private StateFrameLayout mStateFrameLayout;
    private RcvLoadingView mLoadingView;

    private MediaLoaderEngine mMediaLoaderEngine = new MediaLoaderEngine();

    private BucketBean mCurrentBucketBean;
    private int mCurrentPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_image);

        mOptions = getIntent().getParcelableExtra(ImageConstants.KEY_INTENT_OPTIONS);
        if (mOptions == null)
        {
            setResult(Activity.RESULT_CANCELED);
            Log.w("ImagePicker", "Can not pick image because of a null CustomPickImageOptions!");
            finish();
            return;
        }

        mRootLinearLayout = findViewById(R.id.ll_root_container);
        mActionBar = findViewById(R.id.actionBar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mBottomLinearLayout = findViewById(R.id.ll_bottom_operation);
        mStateFrameLayout = findViewById(R.id.stateFrameLayout);
        mLoadingView = findViewById(R.id.loadingView);

        initStyle();
        startLoadAllBuckets();
    }

    private void initStyle()
    {
        CustomPickImageStyle style = mOptions.getStyle();
        if (style == null)
        {
            style = CustomPickImageStyle.dark(this);
        }

        Utils.setStatusBarColor(this, style.getStatusBarColor(), true);
        //智能调节状态栏文字颜色
        Utils.setStatusBarDarkMode(this, !Utils.isDarkColor(style.getStatusBarColor()));
        Utils.setNavigationBarColor(this, style.getNavigationBarColor());

        mRootLinearLayout.setBackgroundColor(style.getRootBackgroundColor());

        mActionBar.setBackgroundColor(style.getActionBarColor());
        Drawable leftBackDrawable = AppCompatResources.getDrawable(this, R.drawable.image_picker_action_bar_arrow);
        leftBackDrawable.setTint(style.getActionBarTextColor());
        mActionBar.setLeftIconDrawable(leftBackDrawable);
        mActionBar.setTitleTextColor(style.getActionBarTextColor());
        mActionBar.setLeftTextColor(style.getActionBarTextColor());
        mActionBar.setRightTextColor01(style.getActionBarTextColor());
        mActionBar.setRightTextColor02(style.getActionBarTextColor());

        mBottomLinearLayout.setBackgroundColor(style.getNavigationBarColor());

        mLoadingView.setColor(style.getLoadingColor());
    }

    /**
     * 扫描所有媒体文件夹
     */
    private void startLoadAllBuckets()
    {
        mStateFrameLayout.switchToLoadingState();

        XXPermissions.with(this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback()
                {
                    @Override
                    public void onGranted(List<String> permissions, boolean all)
                    {
                        mMediaLoaderEngine.loadAllBucket(GridImageActivity.this, GridImageActivity.this, mOptions,
                                new PickCallBack<List<BucketBean>>()
                                {
                                    @Override
                                    public void onPickSuccess(List<BucketBean> result)
                                    {
                                        mStateFrameLayout.switchToContentState();
                                        mAllBucketList = result;
                                        //TODO 切换到第一个文件夹
                                    }

                                    @Override
                                    public void onPickFailed(int errorCode, String message)
                                    {
                                        Toast.makeText(GridImageActivity.this, R.string.can_not_scan_media_data, Toast.LENGTH_SHORT).show();
                                        setResult(Activity.RESULT_CANCELED);
                                        finish();
                                    }
                                });
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never)
                    {
                        Toast.makeText(GridImageActivity.this, R.string.permission_denied_of_pick_image, Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_CANCELED);
                        finish();
                    }
                });
    }
}
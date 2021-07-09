package com.lwkandroid.imagepicker.custom.crop;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.config.CustomCropImageOptions;
import com.lwkandroid.imagepicker.config.CustomCropImageStyle;
import com.lwkandroid.imagepicker.constants.ImageConstants;
import com.lwkandroid.imagepicker.utils.Utils;
import com.lwkandroid.imagepicker.widget.cropview.CropImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @description: 执行自定义裁剪图片的Activity
 * @author: LWK
 * @date: 2021/7/6 14:39
 */
public class CustomCropImageActivity extends AppCompatActivity implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener, View.OnClickListener
{
    private CustomCropImageOptions mOptions;
    private Uri mResultUri;

    private CropImageView mCropImageView;
    private View mRootContainer;
    private View mBottomContainer;
    private TextView mTvCancel;
    private TextView mTvDone;
    private ImageView mImgFlipVertical;
    private ImageView mImgFlipHorizontal;
    private ImageView mImgRotate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        mOptions = getIntent().getParcelableExtra(ImageConstants.KEY_INTENT_OPTIONS);
        mResultUri = getIntent().getParcelableExtra(ImageConstants.KEY_INTENT_FILE);
        if (mOptions == null)
        {
            setResult(Activity.RESULT_CANCELED);
            Log.w("ImagePicker", "Can not crop image because of a null CustomCropImageOptions!");
            finish();
            return;
        }
        if (mOptions.getStyle() == null)
        {
            mOptions.setStyle(CustomCropImageStyle.dark(this));
        }

        mRootContainer = findViewById(R.id.v_root_container);
        mCropImageView = findViewById(R.id.cropImageView);
        mBottomContainer = findViewById(R.id.v_bottom_operation);
        mTvDone = findViewById(R.id.tv_done);
        mTvCancel = findViewById(R.id.tv_cancel);
        mImgFlipHorizontal = findViewById(R.id.img_flip_horizontal);
        mImgFlipVertical = findViewById(R.id.img_flip_vertical);
        mImgRotate = findViewById(R.id.img_rotate);

        mTvCancel.setOnClickListener(this);

        initStyle();
        initData();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mCropImageView.setOnSetImageUriCompleteListener(null);
        mCropImageView.setOnCropImageCompleteListener(null);
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error)
    {
        if (error == null)
        {
            //证明图片加载成功
            mImgFlipHorizontal.setOnClickListener(this);
            mImgFlipVertical.setOnClickListener(this);
            mImgRotate.setOnClickListener(this);
            mTvDone.setOnClickListener(this);
        } else
        {
            Toast.makeText(this, R.string.can_not_load_image, Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result)
    {
        if (result.isSuccessful())
        {
            setResult(Activity.RESULT_OK);
            finish();
        } else
        {
            Toast.makeText(this, R.string.can_not_crop_image, Toast.LENGTH_SHORT).show();
        }
    }

    private void initStyle()
    {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        Utils.setStatusBarColor(this, Color.TRANSPARENT, false);
        Utils.setStatusBarDarkMode(this, true);
        Utils.setNavigationBarColor(this, mOptions.getStyle().getNavigationBarColor());

        mRootContainer.setBackgroundColor(mOptions.getStyle().getRootBackgroundColor());
        mBottomContainer.setBackgroundColor(mOptions.getStyle().getNavigationBarColor());
        mTvCancel.setTextColor(mOptions.getStyle().getOperationTintColor());
        mTvDone.setTextColor(mOptions.getStyle().getDoneTextColor());
        mImgRotate.getDrawable().setTint(mOptions.getStyle().getOperationTintColor());
        mImgFlipVertical.getDrawable().setTint(mOptions.getStyle().getOperationTintColor());
        mImgFlipHorizontal.getDrawable().setTint(mOptions.getStyle().getOperationTintColor());
    }

    private void initData()
    {
        mCropImageView.setCropShape(mOptions.isCircleShape() ? CropImageView.CropShape.OVAL : CropImageView.CropShape.RECTANGLE);
        mCropImageView.setAspectRatio(mOptions.getAspectX(), mOptions.getAspectY());
        //为了保证输出尺寸不会小于设置的值，这里将最小裁剪尺寸设置为输出尺寸
        mCropImageView.setMinCropResultSize(mOptions.getOutputX(), mOptions.getOutputY());

        mCropImageView.setImageUriAsync(Utils.file2Uri(this, mOptions.getImageFile()));
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        if (id == R.id.tv_done)
        {
            mCropImageView.saveCroppedImageAsync(mResultUri, Bitmap.CompressFormat.JPEG, 100,
                    mOptions.getOutputX(), mOptions.getOutputY());
        } else if (id == R.id.tv_cancel)
        {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.img_rotate)
        {
            mCropImageView.rotateImage(-90);
        } else if (id == R.id.img_flip_vertical)
        {
            mCropImageView.flipImageVertically();
        } else if (id == R.id.img_flip_horizontal)
        {
            mCropImageView.flipImageHorizontally();
        }
    }
}

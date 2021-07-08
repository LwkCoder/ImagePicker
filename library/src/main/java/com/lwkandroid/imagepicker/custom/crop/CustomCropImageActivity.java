package com.lwkandroid.imagepicker.custom.crop;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.config.CustomCropImageOptions;
import com.lwkandroid.imagepicker.constants.ImageConstants;
import com.lwkandroid.imagepicker.utils.Utils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @description: 执行自定义裁剪图片的Activity
 * @author: LWK
 * @date: 2021/7/6 14:39
 */
public class CustomCropImageActivity extends AppCompatActivity
{
    private CustomCropImageOptions mOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        mOptions = getIntent().getParcelableExtra(ImageConstants.KEY_INTENT_OPTIONS);
        if (mOptions == null)
        {
            setResult(Activity.RESULT_CANCELED);
            Log.w("ImagePicker", "Can not crop image because of a null CustomCropImageOptions!");
            finish();
            return;
        }

        Utils.setStatusBarColor(this, Color.TRANSPARENT, false);
        Utils.setStatusBarDarkMode(this, true);
    }
}

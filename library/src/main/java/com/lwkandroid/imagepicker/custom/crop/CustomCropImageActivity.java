package com.lwkandroid.imagepicker.custom.crop;

import android.os.Bundle;

import com.lwkandroid.imagepicker.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @description: 执行自定义裁剪图片的Activity
 * @author: LWK
 * @date: 2021/7/6 14:39
 */
public class CustomCropImageActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
    }
}

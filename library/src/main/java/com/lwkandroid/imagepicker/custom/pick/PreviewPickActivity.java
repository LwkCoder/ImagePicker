package com.lwkandroid.imagepicker.custom.pick;

import android.os.Bundle;

import com.lwkandroid.imagepicker.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @description: 预览所选图片界面
 * @author: LWK
 * @date: 2021年6月18日 16:05:05
 */
public class PreviewPickActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_pick);
    }
}
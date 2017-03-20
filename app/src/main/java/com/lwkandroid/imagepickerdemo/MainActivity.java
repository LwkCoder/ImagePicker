package com.lwkandroid.imagepickerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private final int REQUEST_CODE = 111;
    private final int RESULT_CODE = 112;

    private String cachePath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cachePath = getExternalCacheDir().getAbsolutePath();

        findViewById(R.id.btn_main01).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new ImagePicker.Builder()
                        .pickType(ImagePickType.ONLY_CAMERA)
                        .cachePath(cachePath)
                        .build()
                        .start(MainActivity.this, REQUEST_CODE, RESULT_CODE);
            }
        });

        findViewById(R.id.btn_main02).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new ImagePicker.Builder()
                        .pickType(ImagePickType.SINGLE)
                        .cachePath(cachePath)
                        .build()
                        .start(MainActivity.this, REQUEST_CODE, RESULT_CODE);
            }
        });

        findViewById(R.id.btn_main03).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new ImagePicker.Builder()
                        .pickType(ImagePickType.MUTIL)
                        .limitNum(9)
                        .cachePath(cachePath)
                        .build()
                        .start(MainActivity.this, REQUEST_CODE, RESULT_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("MainActivity", "onActivityResult: requestCode=" + requestCode + " resultCode=" + resultCode + " data=" + data);
        if (resultCode == RESULT_CODE && data != null)
        {
            List<ImageBean> resultList = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
            Log.e("MainActivity", "结果：" + resultList);
        }
    }
}

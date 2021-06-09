package com.lwkandroid.imagepickerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lwkandroid.library.ImagePicker;
import com.lwkandroid.library.bean.MediaBean;
import com.lwkandroid.library.callback.PickCallBack;
import com.lwkandroid.library.constants.ImageConstants;
import com.lwkandroid.library.custom.model.MediaLoaderEngine;
import com.lwkandroid.library.options.CustomPickImageOptions;

import java.io.File;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView mTextView;
    int index = 0;
    File mLastFile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.textView);
        findViewById(R.id.btnTest1).setOnClickListener(v -> ImagePicker.photographBySystem()
                .build()
                .doPhotograph(MainActivity.this, new PickCallBack<File>()
                {
                    @Override
                    public void onPickSuccess(File result)
                    {
                        mLastFile = result;
                        mTextView.setText(result.getAbsolutePath());
                    }

                    @Override
                    public void onPickFailed(int errorCode, String message)
                    {
                        mTextView.setText("code=" + errorCode + " msg=" + message);
                    }
                }));

        findViewById(R.id.btnTest2).setOnClickListener(v -> ImagePicker.pickImageBySystem()
                .setMaxPickNumber(9)
                .build()
                .doPickImage(MainActivity.this, new PickCallBack<List<File>>()
                {

                    @Override
                    public void onPickSuccess(List<File> result)
                    {
                        mLastFile = result.get(0);
                        String string = "";
                        for (File file : result)
                        {
                            string = string + file.getAbsolutePath() + "\n";
                        }
                        mTextView.setText(string);
                    }

                    @Override
                    public void onPickFailed(int errorCode, String message)
                    {
                        mTextView.setText("code=" + errorCode + " msg=" + message);
                    }
                }));

        findViewById(R.id.btnTest3).setOnClickListener(v -> {
            if (mLastFile == null)
                return;
            ImagePicker.cropImageBySystem(mLastFile)
                    .setAspectX(1)
                    .setAspectY(1)
                    .setOutputX(500)
                    .setOutputY(500)
                    .build()
                    .doCrop(MainActivity.this, new PickCallBack<File>()
                    {
                        @Override
                        public void onPickSuccess(File result)
                        {
                            mTextView.setText(result.getAbsolutePath());
                        }

                        @Override
                        public void onPickFailed(int errorCode, String message)
                        {
                            mTextView.setText("code=" + errorCode + " msg=" + message);
                        }
                    });
        });

        findViewById(R.id.btnTest4).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                ImagePicker.pickImageByCustom()
//                        .setMaxPickNumber(9)
//                        .build()
//                        .doPickImage(MainActivity.this, new PickCallBack<List<MediaBean>>()
//                        {
//                            @Override
//                            public void onPickSuccess(List<MediaBean> result)
//                            {
//                                String string = "";
//                                for (MediaBean mediaBean : result)
//                                {
//                                    string = string + mediaBean.toString() + "\n";
//                                }
//                                mTextView.setText(string);
//                            }
//
//                            @Override
//                            public void onPickFailed(int errorCode, String message)
//                            {
//                                mTextView.setText("code=" + errorCode + " msg=" + message);
//                            }
//                        });

                index++;
                MediaLoaderEngine engine=new MediaLoaderEngine();
                engine.loadPageImage(MainActivity.this, new CustomPickImageOptions(), ImageConstants.BUCKET_ID_ALL_IMAGE,
                        index, 3, new PickCallBack<List<MediaBean>>()
                        {
                            @Override
                            public void onPickSuccess(List<MediaBean> result)
                            {
                                String string = "";
                                for (MediaBean mediaBean : result)
                                {
                                    string = string + mediaBean.toString() + "\n";
                                }
                                mTextView.setText(string);
                            }

                            @Override
                            public void onPickFailed(int errorCode, String message)
                            {
                                mTextView.setText("code=" + errorCode + " msg=" + message);
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        }
    }

}

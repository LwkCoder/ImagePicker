package com.lwkandroid.imagepickerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.bean.MediaBean;
import com.lwkandroid.imagepicker.bean.PickResultBean;
import com.lwkandroid.imagepicker.callback.PickCallBack;

import java.io.File;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView mTextView;
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
            {
                return;
            }
            ImagePicker.cropImageBySystem(mLastFile)
                    .setAspectSize(1, 1)
                    .setOutputSize(500, 500)
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

        findViewById(R.id.btnTest4).setOnClickListener(v ->
                ImagePicker.pickImageByCustom(new GlideDisplayer())
                        .setMaxPickNumber(9)
                        .setShowOriginalFileCheckBox(true)
                        .setPageLoadSize(100)
                        .build()
                        .doPickImage(MainActivity.this, new PickCallBack<PickResultBean>()
                        {
                            @Override
                            public void onPickSuccess(PickResultBean result)
                            {
                                String string = "";
                                for (MediaBean mediaBean : result.getMediaList())
                                {
                                    string = string + mediaBean.toString() + "\n";
                                }
                                mTextView.setText(string);

                                mLastFile = new File(result.getMediaList().get(0).getPath());
                            }

                            @Override
                            public void onPickFailed(int errorCode, String message)
                            {
                                mTextView.setText("code=" + errorCode + " msg=" + message);
                            }
                        }));

        findViewById(R.id.btnTest5).setOnClickListener(v -> {
            if (mLastFile == null)
            {
                return;
            }
            ImagePicker.cropImageByCustom(mLastFile)
                    .setAspectSize(1, 1)
                    .setCircleShape(true)
                    .setOutputSize(320, 320)
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

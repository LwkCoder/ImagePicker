package com.lwkandroid.imagepickerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.data.ImagePickerCropParams;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private final int REQUEST_CODE = 111;
    private final int RESULT_CODE = 112;

    private String cachePath;

    private RadioGroup mRgType;
    private RadioButton mRbCamera;
    private RadioButton mRbSinlge;
    private RadioButton mRbMutil;
    private EditText mEdMaxNum;
    private CheckBox mCkNeedCamera;
    private CheckBox mCkNeedCrop;
    private View mViewCrop;
    private RadioGroup mRgCrop;
    private RadioButton mRbCircle;
    private RadioButton mRbRect;
    private EditText mEdMaxWidth;
    private EditText mEdRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cachePath = getExternalCacheDir().getAbsolutePath();

        mRgType = (RadioGroup) findViewById(R.id.rg_main_mode);
        mRbCamera = (RadioButton) findViewById(R.id.rb_main_mode_camera);
        mRbSinlge = (RadioButton) findViewById(R.id.rb_main_mode_single);
        mRbMutil = (RadioButton) findViewById(R.id.rb_main_mode_mutil);
        mEdMaxNum = (EditText) findViewById(R.id.ed_main_max_num);
        mCkNeedCamera = (CheckBox) findViewById(R.id.ck_main_need_camera);
        mCkNeedCrop = (CheckBox) findViewById(R.id.ck_main_need_crop);
        mViewCrop = findViewById(R.id.ll_main_crop_params);
        mRgCrop = (RadioGroup) findViewById(R.id.rg_main_crop_shape);
        mRbCircle = (RadioButton) findViewById(R.id.rb_main_crop_shape_circle);
        mRbRect = (RadioButton) findViewById(R.id.rb_main_crop_shape_rect);
        mEdMaxWidth = (EditText) findViewById(R.id.ed_main_crop_maxwidth);
        mEdRatio = (EditText) findViewById(R.id.ed_main_crop_ratio);

        mCkNeedCrop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                    mViewCrop.setVisibility(View.VISIBLE);
                else
                    mViewCrop.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.btn_main_start).setOnClickListener(this);
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

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_main_start:
                new ImagePicker.Builder()
                        .pickType(getPickType())
                        .maxNum(getMaxNum())
                        .needCamera(mCkNeedCamera.isChecked())
                        .cachePath(cachePath)//自定义缓存路径
                        .build()
                        .start(this, REQUEST_CODE, RESULT_CODE);
                break;
        }
    }

    private ImagePickType getPickType()
    {
        int id = mRgType.getCheckedRadioButtonId();
        if (id == R.id.rb_main_mode_camera)
            return ImagePickType.ONLY_CAMERA;
        else if (id == R.id.rb_main_mode_single)
            return ImagePickType.SINGLE;
        else
            return ImagePickType.MUTIL;
    }

    private int getMaxNum()
    {
        String numStr = mEdMaxNum.getText().toString();
        return (numStr != null && numStr.length() > 0) ? Integer.valueOf(numStr) : 1;
    }

    private ImagePickerCropParams getCropParams()
    {
        if (!mCkNeedCrop.isChecked())
            return null;

        //        ImagePickerCropParams cropParams = new ImagePickerCropParams();
        //
        //        int shapeId = mRgCrop.getCheckedRadioButtonId();
        //        if (shapeId == R.id.rb_main_crop_shape_circle)
        //            cropParams.setType(ImageCropType.CIRCLE);
        //        else
        //            cropParams.setType(ImageCropType.RECT);
        //
        //        String maxWidth = mEdMaxWidth.getText().toString();
        //        cropParams.setCropMaxWidth((maxWidth != null && maxWidth.length() > 0) ? Integer.valueOf(maxWidth) : 100);
        //
        //        String ratio = mEdRatio.getText().toString();
        //        cropParams.setRatio((ratio != null && ratio.length() > 0) ? Float.valueOf(ratio) : 1.0f);
        //        return cropParams;
        return null;
    }
}

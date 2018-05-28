package com.lwkandroid.imagepickerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.data.ImagePickerCropParams;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private final int REQUEST_CODE = 111;

    private String cachePath;

    private RadioGroup mRgType;
    private EditText mEdMaxNum;
    private CheckBox mCkNeedCamera;
    private CheckBox mCkNeedCrop;
    private View mViewCrop;
    private EditText mEdAsX;
    private EditText mEdAsY;
    private EditText mEdOpX;
    private EditText mEdOpY;
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cachePath = getFilesDir().getAbsolutePath();

        mRgType = (RadioGroup) findViewById(R.id.rg_main_mode);
        mEdMaxNum = (EditText) findViewById(R.id.ed_main_max_num);
        mCkNeedCamera = (CheckBox) findViewById(R.id.ck_main_need_camera);
        mCkNeedCrop = (CheckBox) findViewById(R.id.ck_main_need_crop);
        mViewCrop = findViewById(R.id.ll_main_crop_params);
        mEdAsX = (EditText) findViewById(R.id.ed_main_asX);
        mEdAsY = (EditText) findViewById(R.id.ed_main_asY);
        mEdOpX = (EditText) findViewById(R.id.ed_main_opX);
        mEdOpY = (EditText) findViewById(R.id.ed_main_opY);
        mTvResult = (TextView) findViewById(R.id.tv_main_result);

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
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            List<ImageBean> resultList = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
            String content = "";
            for (ImageBean imageBean : resultList)
            {
                content = content + imageBean.toString() + "\n";
            }
            mTvResult.setText(content);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_main_start:
                new ImagePicker()
                        .pickType(getPickType())//设置选取类型(拍照、单选、多选)
                        .maxNum(getMaxNum())//设置最大选择数量(拍照和单选都是1，修改后也无效)
                        .needCamera(mCkNeedCamera.isChecked())//是否需要在界面中显示相机入口(类似微信)
                        .cachePath(cachePath)//自定义缓存路径
                        .doCrop(getCropParams())//裁剪功能需要调用这个方法，多选模式下无效
                        .displayer(new GlideImagePickerDisplayer())//自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                        .start(this, REQUEST_CODE);
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
            return ImagePickType.MULTI;
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

        String asXString = mEdAsX.getText().toString().trim();
        String asYString = mEdAsY.getText().toString().trim();
        String opXString = mEdOpX.getText().toString().trim();
        String opYString = mEdOpY.getText().toString().trim();

        int asX = TextUtils.isEmpty(asXString) ? 1 : Integer.valueOf(asXString);
        int asY = TextUtils.isEmpty(asYString) ? 1 : Integer.valueOf(asYString);
        int opX = TextUtils.isEmpty(opXString) ? 0 : Integer.valueOf(opXString);
        int opY = TextUtils.isEmpty(opYString) ? 0 : Integer.valueOf(opYString);

        return new ImagePickerCropParams(asX, asY, opX, opY);
    }
}

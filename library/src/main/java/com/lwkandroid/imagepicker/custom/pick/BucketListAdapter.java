package com.lwkandroid.imagepicker.custom.pick;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwkandroid.imagepicker.R;
import com.lwkandroid.imagepicker.bean.BucketBean;
import com.lwkandroid.imagepicker.common.PickCommonConfig;
import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * @description: 文件夹列表适配器
 * @author: LWK
 * @date: 2021/6/18 10:01
 */
class BucketListAdapter extends RcvSingleAdapter<BucketBean>
{
    private final int mTextColor;

    public BucketListAdapter(Context context, List<BucketBean> datas, int textColor)
    {
        super(context, R.layout.adapter_sheet_bucket_list, datas);
        this.mTextColor = textColor;
    }

    @Override
    public void onBindView(RcvHolder holder, BucketBean itemData, int position)
    {
        ImageView imageView = holder.findView(R.id.imageView);
        TextView textView = holder.findView(R.id.textView);

        textView.setTextColor(mTextColor);
        textView.setText(getContext().getString(R.string.bucket_name_placeholder, itemData.getName(), itemData.getFileNumber()));

        if (PickCommonConfig.getInstance().getImagePickerDisplayer() != null)
        {
            PickCommonConfig.getInstance().getImagePickerDisplayer()
                    .displayImage(getContext(), itemData.getFirstImagePath(), imageView);
        }
    }
}

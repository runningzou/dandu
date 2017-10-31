package com.runningzou.dandu.ui.dialog;

import android.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.runningzou.dandu.R;
import com.runningzou.dandu.databinding.DialogRecommendBinding;

/**
 * Created by runningzou on 2017/10/30.
 */

public class RecommandDialog extends BaseDialog {

    private String url;

    @Override
    public int intLayoutId() {
        return R.layout.dialog_recommend;
    }

    @Override
    public void initView(ViewDataBinding binding) {
        setWidth(280);
        setHeight(400);
        DialogRecommendBinding bind = (DialogRecommendBinding) binding;
        Glide.with(this)
                .load(url)
                .into(bind.imageView);
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

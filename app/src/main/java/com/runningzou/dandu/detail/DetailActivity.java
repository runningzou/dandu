package com.runningzou.dandu.detail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.webkit.WebSettings;

import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.runningzou.dandu.R;
import com.runningzou.dandu.base.BaseActivity;
import com.runningzou.dandu.base.rxjava.Live;
import com.runningzou.dandu.databinding.ActivityArtDetailBinding;
import com.runningzou.dandu.di.Injector;
import com.runningzou.dandu.model.entity.ContentParseResult;
import com.runningzou.dandu.model.entity.Item;
import com.runningzou.dandu.util.AppUtil;
import com.runningzou.dandu.util.PaintViewUtil;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class DetailActivity extends BaseActivity implements Injector, ObservableScrollViewCallbacks {

    private ActivityArtDetailBinding mBinding;

    @Inject
    DetailViewModel mDetailViewModel;

    private int mParallaxImageHeight;

    private Item mItem;

    @Inject
    PaintViewUtil mPaintViewUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_art_detail);

        initView();

        mDetailViewModel
                .getGetDetailRxCommand()
                .switchToLatest()
                .compose(Live.<ContentParseResult>bindLifecycle(this))
                .subscribe(new Consumer<ContentParseResult>() {
                    @Override
                    public void accept(ContentParseResult contentParseResult) throws Exception {
                        if (contentParseResult.getType() == ContentParseResult.HTML5_TYPE) {
                            initWebViewSetting();
                            mBinding.newsParseWeb.setVisibility(View.GONE);
                            mBinding.image.setVisibility(View.GONE);
                            mBinding.webView.setVisibility(View.VISIBLE);
                            mBinding.newsTop.setVisibility(View.GONE);
                            mBinding.webView.loadUrl(contentParseResult.getHtml5Url());
                        } else {
                            mPaintViewUtil.addTypeView(contentParseResult, mBinding.newsParseWeb);
                        }

                    }
                });

        mItem = getIntent().getExtras().getParcelable("item");

        if (mItem != null){
            Glide.with(this).load(mItem.getThumbnail()).into(mBinding.image);
            mBinding.newsTopLeadLine.setVisibility(View.VISIBLE);
            mBinding.newsTopImgUnderLine.setVisibility(View.VISIBLE);
            mBinding.newsTopType.setText("文 字");
            mBinding.newsTopDate.setText(mItem.getUpdate_time());
            mBinding.newsTopTitle.setText(mItem.getTitle());
            mBinding.newsTopAuthor.setText(mItem.getAuthor());
            mBinding.newsTopLead.setText(mItem.getLead());
            mBinding.newsTopLead.setLineSpacing(1.5f,1.8f);

            mDetailViewModel.getGetDetailRxCommand().execute(mItem.getId());
        }
    }

    private void initView() {
        setSupportActionBar(mBinding.toolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mBinding.toolBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));
        mBinding.scrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
    }

    private void initWebViewSetting() {
        WebSettings localWebSettings = mBinding.webView.getSettings();
        localWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setSupportZoom(true);
        mBinding.webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        localWebSettings.setUseWideViewPort(true);
        localWebSettings.setLoadWithOverviewMode(true);
    }

    private String addParams2WezeitUrl(String url, boolean paramBoolean) {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append(url);
        localStringBuffer.append("?client=android");
        localStringBuffer.append("&device_id=" + AppUtil.getDeviceId(this));
        localStringBuffer.append("&version=" + "1.3.0");
        if (paramBoolean)
            localStringBuffer.append("&show_video=0");
        else {
            localStringBuffer.append("&show_video=1");
        }
        return localStringBuffer.toString();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.primary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mBinding.toolBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}

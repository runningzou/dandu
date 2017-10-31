package com.runningzou.dandu.video;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.webkit.WebSettings;

import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.runningzou.dandu.R;
import com.runningzou.dandu.base.BaseActivity;
import com.runningzou.dandu.base.rxjava.Live;
import com.runningzou.dandu.databinding.ActivityVideoDetailBinding;
import com.runningzou.dandu.di.Injector;
import com.runningzou.dandu.model.entity.ContentParseResult;
import com.runningzou.dandu.model.entity.Item;
import com.runningzou.dandu.util.PaintViewUtil;

import javax.inject.Inject;

import cn.jzvd.JZVideoPlayer;
import io.reactivex.functions.Consumer;

/**
 * Created by runningzou on 2017/10/31.
 */

public class VideoDetailActivity extends BaseActivity implements Injector {

    private ActivityVideoDetailBinding mBinding;

    @Inject
    VideoDetailViewModel mViewModel;

    @Inject
    PaintViewUtil mPaintViewUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_detail);

        initView();
        
        mViewModel.getGetDetailRxCommand()
                .switchToLatest()
                .compose(Live.<ContentParseResult>bindLifecycle(this))
                .subscribe(new Consumer<ContentParseResult>() {
                    @Override
                    public void accept(ContentParseResult contentParseResult) throws Exception {

                        if (contentParseResult.getType() == ContentParseResult.HTML5_TYPE) {
                            initWebViewSetting();
                            mBinding.newsParseWeb.setVisibility(View.GONE);
                            mBinding.video.setVisibility(View.GONE);
                            mBinding.webView.setVisibility(View.VISIBLE);
                            mBinding.newsTop.setVisibility(View.GONE);
                            mBinding.webView.loadUrl(contentParseResult.getHtml5Url());
                        } else {
                            mBinding.newsTopLeadLine.setVisibility(View.VISIBLE);
                            mBinding.newsTopImgUnderLine.setVisibility(View.VISIBLE);
                            mPaintViewUtil.addTypeView(contentParseResult, mBinding.newsParseWeb);
                        }
                    }
                });

        Bundle bundle = getIntent().getExtras();
        Item item = bundle.getParcelable("item");
        if (item != null){
            mBinding.video.setUp(item.getVideo(), JZVideoPlayer.SCREEN_LAYOUT_LIST,"");
            Glide.with(this).load(item.getThumbnail()).into(mBinding.video.thumbImageView);
            mBinding.newsTopType.setText("视 频");
            mBinding.newsTopLeadLine.setVisibility(View.VISIBLE);
            mBinding.newsTopImgUnderLine.setVisibility(View.VISIBLE);
            mBinding.newsTopDate.setText(item.getUpdate_time());
            mBinding.newsTopTitle.setText(item.getTitle());
            mBinding.newsTopAuthor.setText(item.getAuthor());
            mBinding.newsTopLead.setText(item.getLead());
            mViewModel.getGetDetailRxCommand().execute(item.getId());
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

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        JZVideoPlayer.releaseAllVideos();
        super.onPause();
    }
}

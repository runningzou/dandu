package com.runningzou.dandu.audio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.orhanobut.logger.Logger;
import com.runningzou.dandu.R;
import com.runningzou.dandu.base.BaseActivity;
import com.runningzou.dandu.base.rxjava.Live;
import com.runningzou.dandu.databinding.ActivityAudioBinding;
import com.runningzou.dandu.model.entity.ContentParseResult;
import com.runningzou.dandu.di.Injector;
import com.runningzou.dandu.model.entity.Item;
import com.runningzou.dandu.util.PaintViewUtil;
import com.runningzou.dandu.util.TimeUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by runningzou on 2017/10/30.
 */

public class AudioDetailActivity extends BaseActivity implements ObservableScrollViewCallbacks, IPlayback.Callback,Injector {

    private PlaybackService mPlaybackService;

    private ActivityAudioBinding mBinding;

    @Inject
    PaintViewUtil mPaintViewUtil;

    @Inject
    AudioDetailViewModel mViewModel;

    private String mSong;

    private Disposable mTimerDisposable;

    private int mParallaxImageHeight;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mPlaybackService = ((PlaybackService.LocalBinder) service).getService();
            register();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            unRegister();
            mPlaybackService = null;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_audio);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
        bindService(new Intent(this, PlaybackService.class), mConnection, Context.BIND_AUTO_CREATE);

        Bundle bundle = getIntent().getExtras();
        Item item = bundle.getParcelable("item");

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
                            mBinding.image.setVisibility(View.GONE);
                            mBinding.webView.setVisibility(View.VISIBLE);
                            mBinding.newsTop.setVisibility(View.GONE);
                            mBinding.webView.loadUrl(contentParseResult.getHtml5Url());
                        } else {
                            mPaintViewUtil.addTypeView(contentParseResult, mBinding.newsParseWeb);
                        }

                    }
                });


        if (item != null) {
            mSong = item.getFm();
            Glide.with(this).load(item.getThumbnail()).into(mBinding.image);
            mBinding.newsTopLeadLine.setVisibility(View.VISIBLE);
            mBinding.newsTopImgUnderLine.setVisibility(View.VISIBLE);
            mBinding.newsTopType.setText("音 频");
            mBinding.newsTopDate.setText(item.getUpdate_time());
            mBinding.newsTopTitle.setText(item.getTitle());
            mBinding.newsTopAuthor.setText(item.getAuthor());
            mBinding.newsTopLead.setText(item.getLead());
            mBinding.newsTopLead.setLineSpacing(1.5f, 1.8f);
            mViewModel.getGetDetailRxCommand().execute(item.getId());
        }

        RxView.clicks(mBinding.buttonPlayToggle)
                .compose(Live.bindLifecycle(this))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (mPlaybackService == null || mSong == null) {
                            Logger.d("mPlaybackService == null");
                            return;
                        }
                        if (mPlaybackService.isPlaying()) {
                            if (mSong.equals(mPlaybackService.getSong())) {
                                mPlaybackService.pause();
                                mBinding.buttonPlayToggle.setImageResource(R.drawable.ic_play);
                            } else {
                                mPlaybackService.play(mSong);
                                mBinding.buttonPlayToggle.setImageResource(R.drawable.ic_pause);
                            }
                        } else {
                            if (mSong.equals(mPlaybackService.getSong())) {
                                mPlaybackService.play();
                            } else {
                                mPlaybackService.play(mSong);
                            }
                            mBinding.buttonPlayToggle.setImageResource(R.drawable.ic_pause);
                        }
                    }
                });


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
        mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                cancelTimer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlaybackService.seekTo(getSeekDuration(seekBar.getProgress()));
                playTimer();
            }
        });
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

    private void register() {
        mPlaybackService.registerCallback(this);
    }

    private void unRegister() {
        if (mPlaybackService != null) {
            mPlaybackService.unregisterCallback(this);
        }
    }


    @Override
    public void onComplete(PlayState state) {

    }

    @Override
    public void onPlayStatusChanged(PlayState status) {
        Logger.d("onPlayStatusChanged.......status=" + status);
        switch (status) {
            case INIT:
                break;
            case PREPARE:
                break;
            case PLAYING:
                updateDuration();
                playTimer();
                mBinding.buttonPlayToggle.setImageResource(R.drawable.ic_pause);
                Logger.d(mPlaybackService.getDuration());
                break;
            case PAUSE:
                cancelTimer();
                mBinding.buttonPlayToggle.setImageResource(R.drawable.ic_play);
                break;
            case ERROR:
                break;
            case COMPLETE:
                cancelTimer();
                mBinding.buttonPlayToggle.setImageResource(R.drawable.ic_play);
                mBinding.seekBar.setProgress(0);
                break;
        }
    }


    private void playTimer() {
        mTimerDisposable = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(Live.<Long>bindLifecycle(this))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (mPlaybackService.isPlaying()) {
                            if (isFinishing()) {
                                return;
                            }

                            int progress = (int) (mBinding.seekBar.getMax()
                                    * ((float) mPlaybackService.getProgress() / (float) mPlaybackService.getDuration()));

                            updateProgressTextWithProgress(mPlaybackService.getProgress());

                            if (progress >= 0 && progress <= mBinding.seekBar.getMax()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    mBinding.seekBar.setProgress(progress, true);
                                } else {
                                    mBinding.seekBar.setProgress(progress);
                                }
                            }
                        }
                    }
                });
    }

    private void cancelTimer() {

        mTimerDisposable.dispose();
    }

    @Override
    public void onPosition(int position) {

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

    private void updateDuration() {
        mBinding.textViewDuration.setText(TimeUtil.formatDuration(mPlaybackService.getDuration()));
    }
    private void updateProgressTextWithProgress(int progress) {
        mBinding.textViewProgress.setText(TimeUtil.formatDuration(progress));
    }

    private int getSeekDuration(int progress) {
        return (int) (getCurrentSongDuration() * ((float) progress / mBinding.seekBar.getMax()));
    }

    private int getCurrentSongDuration() {
        int duration=0;
        if (mPlaybackService != null){
            duration = mPlaybackService.getDuration();
        }
        return duration;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlaybackService.pause();
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}

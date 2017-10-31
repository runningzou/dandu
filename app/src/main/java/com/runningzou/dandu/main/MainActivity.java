package com.runningzou.dandu.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.runningzou.dandu.R;
import com.runningzou.dandu.app.DanduApp;
import com.runningzou.dandu.base.BaseActivity;
import com.runningzou.dandu.base.rxjava.Live;
import com.runningzou.dandu.base.rxjava.RxBus;
import com.runningzou.dandu.databinding.ActivityMainBinding;
import com.runningzou.dandu.di.Injector;
import com.runningzou.dandu.model.entity.GetListParameter;
import com.runningzou.dandu.model.entity.Item;
import com.runningzou.dandu.ui.dialog.RecommandDialog;
import com.runningzou.dandu.util.PreferenceUtils;
import com.runningzou.dandu.util.TimeUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by runningzou on 17-10-27.
 */

public class MainActivity extends BaseActivity implements Injector {

    private ActivityMainBinding mBinding;

    private SlidingMenu mSlidingMenu;

    private LeftMenuFragment mLeftMenuFragment;
    private RightMenuFragment mRightMenuFragment;

    private VerticalPagerAdapter mPagerAdapter;
    private boolean mIsLoading = false;
    private int page = 1;

    private long mLastClickTime;

    @Inject
    MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mViewModel.getListRxCommand()
                .switchToLatest()
                .compose(Live.<List<Item>>bindLifecycle(this))
                .subscribe(new Consumer<List<Item>>() {
                    @Override
                    public void accept(List<Item> items) throws Exception {
                        mPagerAdapter.setDataList(items);
                        page++;
                    }
                });

        mViewModel.getListRxCommand()
                .executing()
                .compose(Live.<Boolean>bindLifecycle(this))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        mIsLoading = aBoolean;
                    }
                });

        mViewModel.getRecommendRxCommand()
                .switchToLatest()
                .compose(Live.<String>bindLifecycle(this))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        RecommandDialog dialog = new RecommandDialog();
                        dialog.setUrl(s);
                        dialog.show(getSupportFragmentManager());
                        PreferenceUtils.setPrefString(MainActivity.this, "getLunar", TimeUtil.getDate("yyyyMMdd"));
                    }
                });

        RxView.clicks(mBinding.leftSlide)
                .compose(Live.bindLifecycle(this))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        mSlidingMenu.showMenu();
                        mLeftMenuFragment.startAnim();
                    }
                });

        RxView.clicks(mBinding.rightSlide)
                .compose(Live.bindLifecycle(this))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        mSlidingMenu.showSecondaryMenu();
                        mRightMenuFragment.startAnim();
                    }
                });



        initMenu(savedInstanceState);
        initPage();

        String getLunar = PreferenceUtils.getPrefString(this, "getLunar", null);
        if (!TimeUtil.getDate("yyyyMMdd").equals(getLunar)) {
            mViewModel.getRecommendRxCommand().execute(DanduApp.getmDeviceId());
        }


        GetListParameter getListParameter = new GetListParameter();
        getListParameter.setPage(1);
        getListParameter.setModel(0);
        getListParameter.setPageId("pageId");
        getListParameter.setCreateTime("0");
        mViewModel.getListRxCommand().execute(getListParameter);

        RxBus.getInstance().toObservable(ShowContentEvent.class)
                .compose(Live.<ShowContentEvent>bindLifecycle(this))
                .subscribe(new Consumer<ShowContentEvent>() {
                    @Override
                    public void accept(ShowContentEvent showContentEvent) throws Exception {
                        mSlidingMenu.showContent();
                    }
                });

    }

    private void initMenu(@Nullable Bundle savedInstanceState) {
        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.setFadeEnabled(true);
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        mSlidingMenu.setMenu(R.layout.left_menu);
        mSlidingMenu.setSecondaryMenu(R.layout.right_menu);

        if (savedInstanceState != null) {
            mLeftMenuFragment = (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LeftMenuFragment.class.getCanonicalName());
            mRightMenuFragment = (RightMenuFragment) getSupportFragmentManager().findFragmentByTag(RightMenuFragment.class.getCanonicalName());
        } else {
            mLeftMenuFragment = LeftMenuFragment.newInstance();
            mRightMenuFragment = RightMenuFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.left_menu, mLeftMenuFragment, LeftMenuFragment.class.getCanonicalName())
                    .commit();


            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.right_menu, mRightMenuFragment, RightMenuFragment.class.getCanonicalName())
                    .commit();
        }

    }

    private void initPage() {
        mPagerAdapter = new VerticalPagerAdapter(getSupportFragmentManager());
        mBinding.viewPager.setAdapter(mPagerAdapter);
        mBinding.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mPagerAdapter.getCount() <= position + 2 && !mIsLoading) {
                    if (mIsLoading) {
                        Toast.makeText(MainActivity.this, "正在努力加载...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //加载更多
                    GetListParameter parameter = new GetListParameter();
                    parameter.setPage(page);
                    parameter.setModel(0);
                    parameter.setPageId(mPagerAdapter.getLastItemId());
                    parameter.setCreateTime(mPagerAdapter.getLastItemCreateTime());
                    mViewModel.getListRxCommand().execute(parameter);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mSlidingMenu.isMenuShowing() || mSlidingMenu.isSecondaryMenuShowing()) {
            mSlidingMenu.showContent();
        } else {
            if (System.currentTimeMillis() - mLastClickTime <= 2000L) {
                super.onBackPressed();
            } else {
                mLastClickTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }

        }

    }

}

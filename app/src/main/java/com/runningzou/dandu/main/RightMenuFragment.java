package com.runningzou.dandu.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.jakewharton.rxbinding2.view.RxView;
import com.runningzou.dandu.R;
import com.runningzou.dandu.base.rxjava.Live;
import com.runningzou.dandu.base.rxjava.RxBus;
import com.runningzou.dandu.databinding.FragmentRightMenuBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by runningzou on 17-10-28.
 */

public class RightMenuFragment extends Fragment {

    public static RightMenuFragment newInstance() {
        RightMenuFragment fragment = new RightMenuFragment();
        return fragment;
    }

    private FragmentRightMenuBinding mBinding;

    private List<View> mViewList = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_right_menu, container, false);

        mViewList.add(mBinding.notificationTv);
        mViewList.add(mBinding.favoritesTv);
        mViewList.add(mBinding.downloadTv);
        mViewList.add(mBinding.noteTv);

        RxView.clicks(mBinding.rightSlideClose)
                .compose(Live.bindLifecycle(this))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        RxBus.getInstance().post(new ShowContentEvent());
                    }
                });

        return mBinding.getRoot();

    }

    public void startAnim() {
        startIconAnim(mBinding.rightSlideClose);
        startIconAnim(mBinding.setting);
        startColumnAnim();
    }

    private void startColumnAnim() {
        TranslateAnimation localTranslateAnimation = new TranslateAnimation(0F, 0.0F, 0.0F, 0.0F);
        localTranslateAnimation.setDuration(700L);
        for (int j=0;j<mViewList.size();j++){
            View localView = this.mViewList.get(j);
            localView.startAnimation(localTranslateAnimation);
            localTranslateAnimation = new TranslateAnimation(j * 35,0.0F, 0.0F, 0.0F);
            localTranslateAnimation.setDuration(700L);
        }
    }

    private void startIconAnim(View paramView) {
        ScaleAnimation localScaleAnimation = new ScaleAnimation(0.1F, 1.0F, 0.1F, 1.0F, paramView.getWidth() / 2, paramView.getHeight() / 2);
        localScaleAnimation.setDuration(1000L);
        paramView.startAnimation(localScaleAnimation);
        float f1 = paramView.getWidth() / 2;
        float f2 = paramView.getHeight() / 2;
        localScaleAnimation = new ScaleAnimation(1.0F, 0.5F, 1.0F, 0.5F, f1, f2);
        localScaleAnimation.setInterpolator(new BounceInterpolator());
    }
}

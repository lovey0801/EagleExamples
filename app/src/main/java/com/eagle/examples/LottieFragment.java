package com.eagle.examples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yibasan.lizhifm.LZLottieAnimationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Eagle on 2017/5/12.
 */

public class LottieFragment extends BaseFragment {

    @BindView(R.id.animation_view)
    LZLottieAnimationView animationView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lottie, null);
        unbinder = ButterKnife.bind(this, view);
        animationView.setURLAnimation("http://192.168.24.245:8008/ring.zip");
        animationView.loop(true);
        animationView.playAnimation();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }
}

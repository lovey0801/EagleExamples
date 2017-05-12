package com.eagle.examples;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.EnvironmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Eagle on 2017/5/12.
 */

public class LottieFragment extends Fragment {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lottie, null);
        unbinder = ButterKnife.bind(this, view);
        animationView.setComposition(LottieComposition.Factory.fromFileSync(getActivity(), "/sdcard/183/ring/data.json"));
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

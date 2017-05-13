package com.eagle.examples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationExView;
import com.airbnb.lottie.LottieAnimationView;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Eagle on 2017/5/12.
 */

public class LottieFragment extends BaseFragment {

    @BindView(R.id.animation_view)
    LottieAnimationExView animationView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lottie, null);
        unbinder = ButterKnife.bind(this, view);
        try {
            animationView.setAnimation(new URL("http://192.168.24.245:8008/ring.zip?time=" + time));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//        animationView.setFileAnimation("/storage/emulated/0/Download/cache/291e10360f32c0631d54576633438469/data.json", "/storage/emulated/0/Download/cache/291e10360f32c0631d54576633438469/images");
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

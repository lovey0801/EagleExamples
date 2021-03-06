package com.yibasan.lizhifm;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;

/**
 * Created by Eagle on 2017/5/12.
 */

public class LZLottieAnimationView extends LottieAnimationView {

    public LZLottieAnimationView(Context context) {
        super(context);
    }

    public LZLottieAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LZLottieAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setURLAnimation(String url) {
        LZLottieResourceParser parser = new LZLottieResourceParser(getContext());
        parser.parse(url, new LZLottieResourceParser.ParseCompletion() {
            @Override
            public void onComplete(final String animationName) {
                Log.i(getClass().getSimpleName(), "setURLAnimation name=" + animationName);
                post(new Runnable() {
                    @Override
                    public void run() {
                        String folder = new File(new File(animationName).getParentFile(), "images").getAbsolutePath();
                        Log.i(getClass().getSimpleName(), "setURLAnimation name=" + animationName + ", folder=" + folder);
                        setFileAnimation(animationName,  folder);
                    }
                });
            }

            @Override
            public void onError() {
            }
        });
    }

}

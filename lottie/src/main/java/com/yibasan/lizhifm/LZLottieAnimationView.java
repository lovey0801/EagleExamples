package com.yibasan.lizhifm;


import android.content.Context;
import android.util.AttributeSet;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;
import java.net.URL;

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
                post(new Runnable() {
                    @Override
                    public void run() {
                        String folder = new File(new File(animationName).getParentFile(), "images").getAbsolutePath();
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

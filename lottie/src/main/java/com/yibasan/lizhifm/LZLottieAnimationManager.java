package com.yibasan.lizhifm;

import android.os.Environment;

/**
 * Created by Eagle on 2017/5/12.
 */

public class LZLottieAnimationManager {

    private static LZLottieAnimationManager instance = null;

    public static LZLottieAnimationManager getInstance() {
        if (instance == null) {
            synchronized (LZLottieAnimationManager.class) {
                if (instance == null) {
                    instance = new LZLottieAnimationManager();
                }
            }
        }
        return instance;
    }

    private String cachePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/cache";

    private LZLottieAnimationManager() {}

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public String getCachePath() {
        return cachePath;
    }

}

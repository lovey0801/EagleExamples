package com.airbnb.lottie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

class ImageFileBitmapManager extends AbstractImageBitmapManager {


    ImageFileBitmapManager(Drawable.Callback callback, String imagesFolder,
                           ImageDelegate assetDelegate, Map<String, LottieImage> imageAssets) {
        super(callback, imagesFolder, assetDelegate, imageAssets);
    }

    @Override
    protected InputStream openImageInputStream(String fileName) throws IOException {
        return new FileInputStream(new File(imagesFolder, fileName));
    }
}

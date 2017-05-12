package com.airbnb.lottie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;

abstract class AbstractImageBitmapManager {
    protected final Context context;
    protected String imagesFolder;
    @Nullable
    protected ImageDelegate assetDelegate;
    protected final Map<String, LottieImage> images;
    protected final Map<String, Bitmap> bitmaps = new HashMap<>();

    AbstractImageBitmapManager(Drawable.Callback callback, String imagesFolder,
                               ImageDelegate assetDelegate, Map<String, LottieImage> imageAssets) {
        assertNotNull(callback);

        this.imagesFolder = imagesFolder;
        if (!TextUtils.isEmpty(imagesFolder) &&
                this.imagesFolder.charAt(this.imagesFolder.length() - 1) != '/') {
            this.imagesFolder += '/';
        }

        if (!(callback instanceof View)) {
            Log.w(L.TAG, "LottieDrawable must be inside of a view for images to work.");
            this.images = new HashMap<>();
            context = null;
            return;
        }

        context = ((View) callback).getContext();
        this.images = imageAssets;
        setAssetDelegate(assetDelegate);
    }

    void setAssetDelegate(@Nullable ImageDelegate assetDelegate) {
        this.assetDelegate = assetDelegate;
    }

    @Nullable
    Bitmap bitmapForId(String id) {
        Bitmap bitmap = bitmaps.get(id);
        if (bitmap == null) {
            LottieImage image = images.get(id);
            if (image == null) {
                return null;
            }
            if (assetDelegate != null) {
                bitmap = assetDelegate.fetchBitmap(image);
                bitmaps.put(id, bitmap);
                return bitmap;
            }

            InputStream is;
            try {
                if (TextUtils.isEmpty(imagesFolder)) {
                    throw new IllegalStateException("You must set an images folder before loading an image." +
                            " Set it with LottieComposition#setImagesFolder or LottieDrawable#setImagesFolder");
                }
                is = openImageInputStream(image.getFileName());
            } catch (IOException e) {
                Log.w(L.TAG, "Unable to open asset.", e);
                return null;
            }
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inScaled = true;
            opts.inDensity = 160;
            bitmap = BitmapFactory.decodeStream(is, null, opts);
            bitmaps.put(id, bitmap);
        }
        return bitmap;
    }

    protected abstract InputStream openImageInputStream(String fileName) throws IOException;

    void recycleBitmaps() {
        Iterator<Map.Entry<String, Bitmap>> it = bitmaps.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Bitmap> entry = it.next();
            entry.getValue().recycle();
            it.remove();
        }
    }

    boolean hasSameContext(Context context) {
        return context == null && this.context == null ||
                context != null && this.context.equals(context);
    }

    public static AbstractImageBitmapManager createImageBitmapManager(int lottieType, Drawable.Callback callback, String imagesFolder,
                                                                      ImageDelegate assetDelegate, Map<String, LottieImage> images) {
        switch (lottieType) {
            case LottieComposition.LOTTIE_TYPE_ASSETS:
                return new ImageAssetBitmapManager(callback, imagesFolder, assetDelegate, images);
            default:
               return new ImageFileBitmapManager(callback, imagesFolder, assetDelegate, images);
        }
    }
}

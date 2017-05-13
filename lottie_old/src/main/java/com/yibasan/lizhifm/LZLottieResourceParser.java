package com.yibasan.lizhifm;

/**
 * Created by Eagle on 2017/5/12.
 */


import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by PonyCui_Home on 16/6/18.
 */
public class LZLottieResourceParser {

    public Context context;

    public LZLottieResourceParser(Context context) {
        this.context = context;
    }

    interface ParseCompletion {
        void onComplete(String animationName);

        void onError();
    }


    public String parse(String urlStr) {
        try {
            if (cacheDir(cacheKey(urlStr)).exists()) {
                return parse(null, cacheKey(urlStr));
            } else {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection != null) {
                    connection.setConnectTimeout(20 * 1000);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    return parse(connection.getInputStream(), cacheKey(url));
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "parse url [" + urlStr + "] failed!", e);
        }
        return null;
    }

    public void parse(final String urlStr, final ParseCompletion callback) {
        new Thread() {
            @Override
            public void run() {
                String animationName = parse(urlStr);
                if (animationName == null) {
                    if (callback != null) {
                        callback.onError();
                    }
                } else {
                    if (callback != null) {
                        callback.onComplete(animationName);
                    }
                }
            }
        }.start();
    }

    public String parse(InputStream inputStream, String cacheKey) {
        if (inputStream != null) {
            if (!cacheDir(cacheKey).exists()) {
                unzip(inputStream, cacheKey);
            }
        }

        return new File(cacheDir(cacheKey), "data.json").getAbsolutePath();
    }

    private String cacheKey(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "cacheKey [" + str + "] failed!", e);
        }

        return null;
    }

    private String cacheKey(URL url) {
        return cacheKey(url.toString());
    }

    private File cacheDir(String cacheKey) {
        return new File(LZLottieAnimationManager.getInstance().getCachePath() + "/" + cacheKey + "/");
    }

    private void unzip(InputStream inputStream, String cacheKey) {
        try {
            File cacheDir = cacheDir(cacheKey);
            cacheDir.mkdirs();
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
            while (true) {
                ZipEntry zipItem = zipInputStream.getNextEntry();
                if (zipItem == null)
                    break;
                File file = new File(cacheDir, zipItem.getName());
                if (zipItem.isDirectory()) {
                    file.mkdir();
                } else {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte[] buff = new byte[2048];
                    while (true) {
                        int readBytes = zipInputStream.read(buff);
                        if (readBytes <= 0) {
                            break;
                        }
                        fileOutputStream.write(buff, 0, readBytes);
                    }
                    fileOutputStream.close();
                    zipInputStream.closeEntry();
                }
            }
            zipInputStream.close();
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "unzip [" + cacheKey + "] failed!", e);
        }
    }

}

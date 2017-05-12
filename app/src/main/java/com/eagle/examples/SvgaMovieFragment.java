package com.eagle.examples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Eagle on 2017/5/4.
 */

public class SvgaMovieFragment extends BaseFragment {

//    @BindView(R.id.svga_image_view)
    SVGAImageView svgaImageView;
//    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fagment_svga_movie, null);
//        unbinder = ButterKnife.bind(this, view);
        svgaImageView = new SVGAImageView(getActivity());
        svgaImageView.setBackgroundColor(0x80000000);
        SVGAParser parser = new SVGAParser(getActivity());
        try {
            parser.parse(new URL("http://192.168.24.245:8008/posche.svga?time=" + time), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    Log.i("SvgaMovieFragment", "onCreateView videoItem = " + videoItem);
                    SVGADrawable drawable = new SVGADrawable(videoItem);
                    svgaImageView.setImageDrawable(drawable);
                    svgaImageView.startAnimation();
                }
                @Override
                public void onError() {
                    Log.e("SvgaMovieFragment", "onCreateView onError");
                }
            });
        } catch (MalformedURLException e) {
            Log.e("SvgaMovieFragment", "onCreateView", e);
        }

        return svgaImageView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (unbinder != null) {
//            unbinder.unbind();
//        }
    }
}

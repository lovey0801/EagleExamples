package com.airbnb.lottie;

import android.content.res.Resources;

import org.json.JSONObject;

final class JsonCompositionLoader extends CompositionLoader<JSONObject> {
  private final Resources res;
  private final OnCompositionLoadedListener loadedListener;

  JsonCompositionLoader(Resources res, int lottieType, OnCompositionLoadedListener loadedListener) {
    this.lottieType = lottieType;
    this.res = res;
    this.loadedListener = loadedListener;
  }

  @Override protected LottieComposition doInBackground(JSONObject... params) {
    return LottieComposition.Factory.fromJsonSync(res, lottieType, params[0]);
  }

  @Override protected void onPostExecute(LottieComposition composition) {
    loadedListener.onCompositionLoaded(composition);
  }
}

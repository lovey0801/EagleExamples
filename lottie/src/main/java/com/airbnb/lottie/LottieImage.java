package com.airbnb.lottie;

import org.json.JSONObject;

/**
 * Data class describing an image asset exported by bodymovin.
 */
@SuppressWarnings("WeakerAccess")
public class LottieImage {
  private final int width;
  private final int height;
  private final String id;
  private final String fileName;

  private LottieImage(int width, int height, String id, String fileName) {
    this.width = width;
    this.height = height;
    this.id = id;
    this.fileName = fileName;
  }

  static class Factory {
    private Factory() {
    }

    static LottieImage newInstance(JSONObject imageJson) {
      return new LottieImage(imageJson.optInt("w"), imageJson.optInt("h"), imageJson.optString("id"),
          imageJson.optString("p"));
    }
  }

  @SuppressWarnings("WeakerAccess") public int getWidth() {
    return width;
  }

  @SuppressWarnings("WeakerAccess")public int getHeight() {
    return height;
  }

  public String getId() {
    return id;
  }

  public String getFileName() {
    return fileName;
  }
}

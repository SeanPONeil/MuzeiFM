package com.seanponeil.muzeifm.data.api.model;

import com.google.gson.annotations.SerializedName;

public class Image {
  @SerializedName("#text") public final String text;
  public final String size;

  public Image(String text, String size) {
    this.text = text;
    this.size = size;
  }
}
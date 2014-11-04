package com.seanponeil.muzeifm.data.api.model;

import java.util.List;

public class Album {
  public final String name;
  public final String playcount;
  public final String url;
  public final Artist artist;
  public final List<Image> image;

  public Album(String name, String playcount, String url, Artist artist, List<Image> image) {
    this.name = name;
    this.playcount = playcount;
    this.url = url;
    this.artist = artist;
    this.image = image;
  }
}
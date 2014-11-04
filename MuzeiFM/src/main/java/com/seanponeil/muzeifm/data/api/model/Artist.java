package com.seanponeil.muzeifm.data.api.model;

import java.util.List;

public class Artist {
  public final String name;
  public final String playcount;
  public final String mbid;
  public final String url;
  public final List<Image> image;

  public Artist(String name, String playcount, String mbid, String url, List<Image> image) {
    this.name = name;
    this.playcount = playcount;
    this.mbid = mbid;
    this.url = url;
    this.image = image;
  }
}
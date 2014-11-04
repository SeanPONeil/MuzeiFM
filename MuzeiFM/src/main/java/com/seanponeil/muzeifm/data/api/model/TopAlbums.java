package com.seanponeil.muzeifm.data.api.model;

import java.util.List;

public class TopAlbums {
  public final List<Album> album;

  public TopAlbums(List<Album> albums) {
    this.album = albums;
  }
}
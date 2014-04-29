package com.seanponeil.muzeifm;

import java.util.List;

public class TopAlbumsResponse {

  TopAlbums topalbums;

  static class TopAlbums {
    List<Album> album;
  }

  static class Album {
    String name;
    String playcount;
    String url;
    Artist artist;
    List<Image> image;
  }

  static class Artist {
    String name;
    String playcount;
    String mbid;
    String url;
  }
}

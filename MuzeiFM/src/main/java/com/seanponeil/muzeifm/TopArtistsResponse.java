package com.seanponeil.muzeifm;

import java.util.List;

public class TopArtistsResponse {

  TopArtists topartists;

  static class TopArtists {
    List<Artist> artist;
  }

  static class Artist {
    String name;
    String playcount;
    String mbid;
    String url;
    List<Image> image;
  }
}

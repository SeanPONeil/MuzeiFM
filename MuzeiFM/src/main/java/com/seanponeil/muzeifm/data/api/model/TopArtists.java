package com.seanponeil.muzeifm.data.api.model;

import java.util.List;

public class TopArtists {
  public final List<Artist> artist;

  public TopArtists(List<Artist> artist) {
    this.artist = artist;
  }
}
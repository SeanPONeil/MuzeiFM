package com.seanponeil.muzeifm;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public interface LastFmService {
  @GET("/?method=user.getTopAlbums&format=json")
  public TopAlbumsResponse getTopAlbums(@Query("user") String user, @Query("period") String period,
      @Query("limit") String limit, @Query("page") String page);

  @GET("/?method=user.getTopArtists&format=json")
  public TopArtistsResponse getTopArtists(@Query("user") String user,
      @Query("period") String period, @Query("limit") String limit, @Query("page") String page);

  @GET("/?method=user.getTopArtists&format=json")
  public Response getTopArtistsResponse(@Query("user") String user,
      @Query("period") String period, @Query("limit") String limit, @Query("page") String page);
}

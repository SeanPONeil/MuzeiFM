package com.seanponeil.muzeifm.data.api;

import com.seanponeil.muzeifm.TopAlbumsResponse;
import com.seanponeil.muzeifm.TopArtistsResponse;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public interface LastFmService {
  @GET("/?method=user.getTopAlbums")
  public TopAlbumsResponse getTopAlbums(@Query("user") String user, @Query("period") String period,
      @Query("limit") String limit, @Query("page") String page);

  @GET("/?method=user.getTopArtists")
  public TopArtistsResponse getTopArtists(@Query("user") String user,
      @Query("period") String period, @Query("limit") String limit, @Query("page") String page);

  @GET("/?method=user.getTopArtists")
  public Response getTopArtistsResponse(@Query("user") String user,
      @Query("period") String period, @Query("limit") String limit, @Query("page") String page);
}

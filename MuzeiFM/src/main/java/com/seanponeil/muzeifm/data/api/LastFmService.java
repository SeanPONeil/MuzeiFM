package com.seanponeil.muzeifm.data.api;

import com.seanponeil.muzeifm.data.api.model.TopAlbumsResponse;
import com.seanponeil.muzeifm.data.api.model.TopArtistsResponse;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public interface LastFmService {
  @GET("/?method=user.getTopAlbums")
  public TopAlbumsResponse getTopAlbums(@Query("limit") String limit, @Query("page") String page);

  @GET("/?method=user.getTopArtists")
  public TopArtistsResponse getTopArtists(@Query("limit") String limit, @Query("page") String page);

  @GET("/?method=user.getTopArtists")
  public Response getTopArtistsResponse(@Query("limit") String limit, @Query("page") String page);
}

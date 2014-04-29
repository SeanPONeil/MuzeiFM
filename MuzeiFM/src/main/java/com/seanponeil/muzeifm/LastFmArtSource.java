package com.seanponeil.muzeifm;

import android.content.Intent;
import android.net.Uri;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import java.util.Random;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class LastFmArtSource extends RemoteMuzeiArtSource {

  LastFmService lastFmService;
  String currentToken;

  private static final int ROTATE_TIME_MILLIS = 60 * 60 * 1000; // rotate every hour

  public LastFmArtSource() {
    super("LastFmArtSource");
  }

  @Override public void onCreate() {
    super.onCreate();
    setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());
    }
  }

  @Override
  protected void onTryUpdate(int reason) throws RetryException {
    currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;

    RestAdapter.Builder restAdapterBuilder =
        new RestAdapter.Builder().setEndpoint("http://ws.audioscrobbler.com/2.0/")
            .setRequestInterceptor(new RequestInterceptor() {
              @Override
              public void intercept(RequestFacade request) {
                request.addQueryParam("api_key", Config.LAST_FM_API_KEY);
              }
            })
            .setErrorHandler(new ErrorHandler() {
              @Override
              public Throwable handleError(RetrofitError retrofitError) {
                int statusCode = retrofitError.getResponse().getStatus();
                if (retrofitError.isNetworkError() || (500 <= statusCode && statusCode < 600)) {
                  return new RetryException();
                }
                scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
                return retrofitError;
              }
            });

    if (BuildConfig.DEBUG) {
      restAdapterBuilder.setLogLevel(LogLevel.FULL);
    }

    lastFmService = restAdapterBuilder
        .build()
        .create(LastFmService.class);

    switch (new Random().nextInt() % 2) {
      case 0:
        publishTopAlbumArtwork();
        break;
      case 1:
        publishTopArtistArtwork();
        break;
    }

    scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
  }

  private void publishTopAlbumArtwork() throws RetryException {
    TopAlbumsResponse response = lastFmService.getTopAlbums("oneilse14", "6month", null, null);

    if (response == null || response.topalbums == null) {
      throw new RetryException();
    }

    if (response.topalbums.album.size() == 0) {
      Timber.w("No albums returned from Last.fm API.");
      scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
      return;
    }

    TopAlbumsResponse.Album album;
    String token;
    while (true) {
      album = response.topalbums.album.get(new Random().nextInt(response.topalbums.album.size()));
      token = album.name;
      if (response.topalbums.album.size() <= 1 || !token.equals(currentToken)) {
        break;
      }
    }

    //Replace Image URI with high res image URI
    String link = album.image.get(3).text;
    String highResLink = link.replace("300x300", "_");
    publishArtwork(new Artwork.Builder()
        .title(album.name)
        .byline(album.artist.name)
        .imageUri(Uri.parse(highResLink))
        .token(token)
        .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(album.url)))
        .build());
  }

  private void publishTopArtistArtwork() throws RetryException {
    TopArtistsResponse response = lastFmService.getTopArtists("oneilse14", "6month", null, null);

    if (response == null || response.topartists == null) {
      throw new RetryException();
    }

    if (response.topartists.artist.size() == 0) {
      Timber.w("No artists returned from Last.fm API.");
      scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
      return;
    }
    TopArtistsResponse.Artist artist;
    String token;
    while (true) {
      artist =
          response.topartists.artist.get(new Random().nextInt(response.topartists.artist.size()));
      token = artist.name;
      if (response.topartists.artist.size() <= 1 || !token.equals(currentToken)) {
        break;
      }
    }

    //Replace Image URI with high res image URI
    String link = artist.image.get(4).text;
    publishArtwork(new Artwork.Builder()
        .title(artist.name)
        .byline("Play Count: " + artist.playcount)
        .imageUri(Uri.parse(link))
        .token(token)
        .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(artist.url)))
        .build());
  }
}

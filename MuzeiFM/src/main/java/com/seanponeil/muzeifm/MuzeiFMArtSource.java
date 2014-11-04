package com.seanponeil.muzeifm;

import android.content.Intent;
import android.net.Uri;
import com.crashlytics.android.Crashlytics;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.seanponeil.muzeifm.data.DataModule;
import com.seanponeil.muzeifm.data.api.LastFmApiModule;
import com.seanponeil.muzeifm.data.api.LastFmService;
import dagger.ObjectGraph;
import java.util.Random;
import javax.inject.Inject;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class MuzeiFMArtSource extends RemoteMuzeiArtSource {

  ObjectGraph objectGraph;

  @Inject LastFmService lastFmService;
  String currentToken;

  private static final int ROTATE_TIME_MILLIS = 60 * 60 * 1000; // rotate every hour

  public MuzeiFMArtSource() {
    super("MuzeiFmArtSource");
  }

  @Override public void onCreate() {
    super.onCreate();
    setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    buildObjectGraphAndInject();
    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());
    }
    Crashlytics.start(this);
  }

  public void buildObjectGraphAndInject() {
    objectGraph = ObjectGraph.create(new DataModule(this), new LastFmApiModule());
    objectGraph.inject(this);
  }

  @Override protected void onTryUpdate(int reason) throws RetryException {
    currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;

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

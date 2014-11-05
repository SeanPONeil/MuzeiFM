package com.seanponeil.muzeifm.data;

import android.content.Intent;
import android.net.Uri;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.seanponeil.muzeifm.data.api.LastFmService;
import com.seanponeil.muzeifm.data.api.model.Album;
import com.seanponeil.muzeifm.data.api.model.Artist;
import com.seanponeil.muzeifm.data.api.model.TopAlbumsResponse;
import com.seanponeil.muzeifm.data.api.model.TopArtistsResponse;
import java.util.Random;
import javax.inject.Inject;
import timber.log.Timber;

public class LastFmArtwork {

  LastFmService lastFmService;

  @Inject public LastFmArtwork(LastFmService lastFmService) {
    this.lastFmService = lastFmService;
  }

  public Artwork get() throws RemoteMuzeiArtSource.RetryException {
    if (new Random().nextInt() % 2 == 0) {
      return getTopAlbumArtwork();
    } else {
      return getTopArtistArtwork();
    }
  }

  private Artwork getTopAlbumArtwork() throws RemoteMuzeiArtSource.RetryException {
    TopAlbumsResponse response = lastFmService.getTopAlbums(null, null);

    if (response == null || response.topalbums == null) {
      throw new RemoteMuzeiArtSource.RetryException();
    }

    if (response.topalbums.album.size() == 0) {
      Timber.w("No albums returned from Last.fm API.");
      return null;
    }

    Album album = response.topalbums.album.get(new Random().nextInt(response.topalbums.album.size()));

    //Replace Image URI with high res image URI
    String link = album.image.get(3).text;
    String highResLink = link.replace("300x300", "_");
    return new Artwork.Builder()
        .title(album.name)
        .byline(album.artist.name)
        .imageUri(Uri.parse(highResLink))
        .token(album.name)
        .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(album.url)))
        .build();
  }

  private Artwork getTopArtistArtwork() throws RemoteMuzeiArtSource.RetryException {
    TopArtistsResponse response = lastFmService.getTopArtists(null, null);

    if (response == null || response.topartists == null) {
      throw new RemoteMuzeiArtSource.RetryException();
    }

    if (response.topartists.artist.size() == 0) {
      Timber.w("No artists returned from Last.fm API.");
      return null;
    }

    Artist artist =
        response.topartists.artist.get(new Random().nextInt(response.topartists.artist.size()));

    //Replace Image URI with high res image URI
    String link = artist.image.get(4).text;
    return new Artwork.Builder()
        .title(artist.name)
        .byline("Play Count: " + artist.playcount)
        .imageUri(Uri.parse(link))
        .token(artist.name)
        .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(artist.url)))
        .build();
  }
}

package com.seanponeil.muzeifm;

import com.crashlytics.android.Crashlytics;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.seanponeil.muzeifm.data.ArtworkRotationTime;
import com.seanponeil.muzeifm.data.LastFmArtwork;
import com.seanponeil.muzeifm.data.prefs.IntPreference;
import dagger.ObjectGraph;
import javax.inject.Inject;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class MuzeiFMArtSource extends RemoteMuzeiArtSource {

  ObjectGraph objectGraph;

  @Inject LastFmArtwork lastFmArtwork;
  @Inject @ArtworkRotationTime IntPreference artworkRotationTime;

  public MuzeiFMArtSource() {
    super("MuzeiFmArtSource");
  }

  @Override public void onCreate() {
    super.onCreate();
    setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    buildObjectGraphAndInject();
    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());
    } else {
      Crashlytics.start(this);
    }
  }

  public void buildObjectGraphAndInject() {
    objectGraph = ObjectGraph.create(Modules.list(this));
    objectGraph.inject(this);
  }

  @Override protected void onTryUpdate(int reason) throws RetryException {
    Artwork artwork = lastFmArtwork.get();
    if (artwork != null) {
      publishArtwork(lastFmArtwork.get());
    }
    scheduleUpdate(System.currentTimeMillis() + artworkRotationTime.get());
  }
}

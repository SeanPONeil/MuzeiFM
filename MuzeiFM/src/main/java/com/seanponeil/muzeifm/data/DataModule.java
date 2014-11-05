package com.seanponeil.muzeifm.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.seanponeil.muzeifm.MuzeiFMArtSource;
import com.seanponeil.muzeifm.SettingsActivity;
import com.seanponeil.muzeifm.data.api.LastFmApiModule;
import com.seanponeil.muzeifm.data.prefs.IntPreference;
import com.seanponeil.muzeifm.data.prefs.StringPreference;
import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import java.io.IOException;
import javax.inject.Singleton;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;

@Module(
    injects = {
        MuzeiFMArtSource.class,
        SettingsActivity.class,
    },
    includes = LastFmApiModule.class,
    complete = false,
    library = true
)
public final class DataModule {
  static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

  private final Context context;

  public DataModule(Context context) {
    this.context = context;
  }

  @Provides Context provideContext() {
    return context;
  }

  @Provides SharedPreferences provideSharedPreferences(Context context) {
    return context.getSharedPreferences("MuzeiFM", MODE_PRIVATE);
  }

  @Provides @Singleton OkHttpClient provideOkHttpClient(Context context) {
    return createOkHttpClient(context);
  }

  @Provides @LastFmUsername StringPreference provideLastFmUsername(SharedPreferences preferences) {
    return new StringPreference(preferences, "username");
  }

  @Provides @LastFmPeriod StringPreference provideLastFmPeriod(SharedPreferences preferences) {
    return new StringPreference(preferences, "period", "6month");
  }

  @Provides @ArtworkRotationTime IntPreference provideArtworkRotationTime(
      SharedPreferences preferences) {
    int hourInMillis = 3 * 60 * 60 * 1000; // rotate every three hours
    return new IntPreference(preferences, "rotation_time", hourInMillis);
  }

  static OkHttpClient createOkHttpClient(Context context) {
    OkHttpClient client = new OkHttpClient();

    // Install an HTTP cache in the application cache directory.
    try {
      File cacheDir = new File(context.getCacheDir(), "http");
      HttpResponseCache cache = new HttpResponseCache(cacheDir, DISK_CACHE_SIZE);
      client.setResponseCache(cache);
    } catch (IOException e) {
      Timber.e(e, "Unable to install disk cache.");
    }

    return client;
  }
}

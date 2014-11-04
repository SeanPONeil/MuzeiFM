package com.seanponeil.muzeifm;

import android.content.Context;
import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

@Module(
    injects = {
        SettingsActivity.class,
    },
    complete = false,
    library = true
)
public final class DataModule {
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

  @Provides @LastFmUsername StringPreference provideLastFmUsername(SharedPreferences preferences) {
    return new StringPreference(preferences, "username");
  }
}

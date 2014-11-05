package com.seanponeil.muzeifm;

import android.content.SharedPreferences;
import com.seanponeil.muzeifm.data.DataModule;
import com.seanponeil.muzeifm.data.LastFmUsername;
import com.seanponeil.muzeifm.data.prefs.StringPreference;
import dagger.Module;
import dagger.Provides;

@Module(
    addsTo = DataModule.class,
    complete = false,
    library = true,
    overrides = true
)
public final class DebugDataModule {

  @Provides @LastFmUsername StringPreference provideLastFmUsername(SharedPreferences preferences) {
    return new StringPreference(preferences, "username", "oneilse14");
  }
}

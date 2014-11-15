package com.seanponeil.muzeifm;

import android.content.Context;
import com.seanponeil.muzeifm.data.DataModule;
import com.seanponeil.muzeifm.data.api.LastFmApiModule;

final class Modules {
  static Object[] list(Context context) {
    return new Object[] {
        new DataModule(context),
        new LastFmApiModule()
    };
  }

  private Modules() {

  }
}

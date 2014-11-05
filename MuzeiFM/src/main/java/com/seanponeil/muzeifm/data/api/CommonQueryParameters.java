package com.seanponeil.muzeifm.data.api;

import com.seanponeil.muzeifm.data.LastFmPeriod;
import com.seanponeil.muzeifm.data.LastFmUsername;
import com.seanponeil.muzeifm.data.prefs.StringPreference;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit.RequestInterceptor;

@Singleton
public final class CommonQueryParameters implements RequestInterceptor {

  private final String lastFmApiKey;
  private final StringPreference lastFmUserName;
  private final StringPreference lastFmPeriod;

  @Inject public CommonQueryParameters(@LastFmApiKey String lastFmApiKey,
      @LastFmUsername StringPreference lastFmUserName, @LastFmPeriod StringPreference lastFmPeriod) {
    this.lastFmApiKey = lastFmApiKey;
    this.lastFmUserName = lastFmUserName;
    this.lastFmPeriod = lastFmPeriod;
  }

  @Override public void intercept(RequestFacade request) {
    request.addQueryParam("period", lastFmPeriod.get());
    request.addQueryParam("user", lastFmUserName.get());
    request.addQueryParam("api_key", lastFmApiKey);
    request.addQueryParam("format", "json");
  }
}

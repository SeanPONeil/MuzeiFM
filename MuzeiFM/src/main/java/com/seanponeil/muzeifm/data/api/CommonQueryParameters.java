package com.seanponeil.muzeifm.data.api;

import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit.RequestInterceptor;

@Singleton
public final class CommonQueryParameters implements RequestInterceptor {

  private final String lastFmApiKey;

  @Inject public CommonQueryParameters(@LastFmApiKey String lastFmApiKey) {
    this.lastFmApiKey = lastFmApiKey;
  }

  @Override public void intercept(RequestFacade request) {
    request.addQueryParam("api_key", lastFmApiKey);
    request.addQueryParam("format", "json");
  }
}

package com.seanponeil.muzeifm.data.api;

import com.seanponeil.muzeifm.BuildConfig;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

@Module(
    complete = false,
    library = true
)
public final class LastFmApiModule {
  public static final String LAST_FM_PRODUCTION_URL = "http://ws.audioscrobbler.com/2.0/";

  @Provides @Singleton @LastFmApiKey String provideLastFmApiKey() {
    return BuildConfig.LASTFM_API_KEY;
  }

  @Provides @Singleton Endpoint provideEndpoint() {
    return Endpoints.newFixedEndpoint(LAST_FM_PRODUCTION_URL);
  }

  @Provides @Singleton Client provideClient(OkHttpClient client) {
    return new OkClient(client);
  }

  @Provides @Singleton RestAdapter provideRestAdapter(Endpoint endpoint, Client client,
      CommonQueryParameters parameters) {
    return new RestAdapter.Builder() //
        .setEndpoint(endpoint) //
        .setClient(client) //
        .setRequestInterceptor(parameters) //
        .build();
  }

  @Provides @Singleton LastFmService provideLastFmService(RestAdapter restAdapter) {
    return restAdapter.create(LastFmService.class);
  }
}

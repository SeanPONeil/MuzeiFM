package com.seanponeil.muzeifm.data.api;

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
  public static final String LAST_FM_API_KEY = "63314efccbb3250fb901a950a7401f12";

  @Provides @Singleton @LastFmApiKey String provideLastFmApiKey() {
    return LAST_FM_API_KEY;
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

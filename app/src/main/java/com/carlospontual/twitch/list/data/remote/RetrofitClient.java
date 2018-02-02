package com.carlospontual.twitch.list.data.remote;

import com.carlospontual.twitch.list.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by carlospontual on 02/04/16.
 * Using Retrofit 2.0.1 for HTTP communication
 */
public class RetrofitClient {

    private static final String BASE_URL = "https://api.twitch.tv/helix/";

    private final Retrofit retrofit;

    public RetrofitClient() {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

            builder.client(client);
        }
        retrofit = builder.build();
    }

    public <T> T create(Class<T> apiRequestMethods) {
        return retrofit.create(apiRequestMethods);
    }
}


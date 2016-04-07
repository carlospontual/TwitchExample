package com.carlospontual.twitch.list.injection;

import com.carlospontual.twitch.list.BuildConfig;
import com.carlospontual.twitch.list.TwitchTopGames;
import com.carlospontual.twitch.list.data.remote.RetrofitClient;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by carlospontual on 06/04/16.
 */
@Module
public class AppModule {

    private TwitchTopGames application;

    public AppModule(TwitchTopGames application) {
        this.application = application;
    }
    @Provides
    @Singleton
    public TwitchTopGames providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    public Picasso providesPicasso(TwitchTopGames application) {
        return new Picasso.Builder(application).downloader(new OkHttp3Downloader(application, Integer.MAX_VALUE))
                .build();
    }

    @Provides
    @Singleton
    public RetrofitClient providesRetrofit() {
        return new RetrofitClient();
    }

}

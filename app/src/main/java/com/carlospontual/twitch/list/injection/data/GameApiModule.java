package com.carlospontual.twitch.list.injection.data;

import com.carlospontual.twitch.list.data.remote.GamesApiServices;
import com.carlospontual.twitch.list.data.remote.RetrofitClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by carlospontual on 06/04/16.
 */
@Module
public class GameApiModule {
    @Provides
    @Singleton
    public GamesApiServices providesGamesApiServices(RetrofitClient retrofitClient) {
        return new GamesApiServices(retrofitClient);
    }
}

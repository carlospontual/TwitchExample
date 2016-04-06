package com.carlospontual.twitch.list.injection.data;

import com.carlospontual.twitch.list.data.cache.GamesCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by carlospontual on 06/04/16.
 */
@Module
public class CacheModule {

    @Provides
    @Singleton
    GamesCache providesCache() {
        return new GamesCache();
    }
}

package com.carlospontual.twitch.list;

import android.app.Application;

import com.carlospontual.twitch.list.injection.AppComponent;
import com.carlospontual.twitch.list.injection.AppModule;
import com.carlospontual.twitch.list.injection.DaggerAppComponent;

import io.paperdb.Paper;

/**
 * Created by carlospontual on 06/04/16.
 */
public class TwitchTopGames extends Application {

    private static TwitchTopGames twitchApp;

    public AppComponent component;

    public static TwitchTopGames getInstance() {
        return twitchApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        twitchApp = this;
        Paper.init(this);

        component = createAppComponent();
    }

    public AppComponent getAppComponent() {
        return component;
    }

    AppComponent createAppComponent() {
        return DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }
}

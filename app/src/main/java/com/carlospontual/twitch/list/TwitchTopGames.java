package com.carlospontual.twitch.list;

import android.app.Application;

import io.paperdb.Paper;

/**
 * Created by carlospontual on 06/04/16.
 */
public class TwitchTopGames extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
    }
}

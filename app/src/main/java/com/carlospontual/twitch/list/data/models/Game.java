package com.carlospontual.twitch.list.data.models;

import com.squareup.moshi.Json;

/**
 * Created by carlospontual on 02/04/16.
 */
public class Game {
    public final int viewers;
    public final int channels;

    @Json(name = "game")
    public final GameData gameData;

    public Game(int viewers, int channels, GameData gameData) {
        this.viewers = viewers;
        this.channels = channels;
        this.gameData = gameData;
    }
}

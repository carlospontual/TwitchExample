package com.carlospontual.twitch.list.data.models;

import com.squareup.moshi.Json;

import java.io.Serializable;

/**
 * Created by carlospontual on 02/04/16.
 */
public class Game implements Serializable {

    public static final String GAME_TAG = "game";
    public final int viewers;
    public final int channels;

    @Json(name = "game")
    public final GameData gameData;

    public Game() {
        this(0, 0, null);
    }

    public Game(int viewers, int channels, GameData gameData) {
        this.viewers = viewers;
        this.channels = channels;
        this.gameData = gameData;
    }
}

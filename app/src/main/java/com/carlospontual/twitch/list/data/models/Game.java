package com.carlospontual.twitch.list.data.models;

/**
 * Created by carlospontual on 02/04/16.
 */
public class Game {
    public final int viewers;
    public final int channels;

    public final GameData game;

    public Game(int viewers, int channels, GameData game) {
        this.viewers = viewers;
        this.channels = channels;
        this.game = game;
    }
}

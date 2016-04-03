package com.carlospontual.twitch.list.data.models;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by carlospontual on 02/04/16.
 */
public class TopGames {
    @Json(name = "_total")
    public final int total;
    @Json(name = "top")
    public final List<Game> games;
    @Json(name = "_links")
    public final GameLinks links;

    public TopGames(int total, List<Game> games, GameLinks links) {
        this.total = total;
        this.games = games;
        this.links = links;
    }
}

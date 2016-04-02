package com.carlospontual.twitch.list.data.models;

import java.util.List;

/**
 * Created by carlospontual on 02/04/16.
 */
public class TopGames {

    public final int total;
    public final List<Game> games;
    public final GameLinks links;

    public TopGames(int total, List<Game> games, GameLinks links) {
        this.total = total;
        this.games = games;
        this.links = links;
    }
}

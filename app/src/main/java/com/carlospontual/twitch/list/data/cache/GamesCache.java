package com.carlospontual.twitch.list.data.cache;

import com.carlospontual.twitch.list.data.models.TopGames;

import io.paperdb.Paper;

/**
 * Created by carlospontual on 06/04/16.
 */
public class GamesCache {

    public void save(TopGames topGames) {
        Paper.book().write(TopGames.TOPGAMES_TAG, topGames);
    }

    public TopGames retrieve() {
        return Paper.book().read(TopGames.TOPGAMES_TAG, null);
    }
}

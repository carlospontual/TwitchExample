package com.carlospontual.twitch.list.ui.list;

import com.carlospontual.twitch.list.TwitchTopGames;
import com.carlospontual.twitch.list.data.cache.GamesCache;
import com.carlospontual.twitch.list.data.models.Game;
import com.carlospontual.twitch.list.data.models.TopGames;
import com.carlospontual.twitch.list.data.remote.GamesApiServices;
import com.carlospontual.twitch.list.data.remote.ResponseHandler;

import javax.inject.Inject;

/**
 * Created by carlospontual on 03/04/16.
 */
public class GameListPresenter implements GameListContract.Presenter, ResponseHandler<TopGames> {

    GameListContract.View view;

    @Inject
    GamesCache gamesCache;
    @Inject
    GamesApiServices apiServices;

    TopGames topGamesCache;

    public GameListPresenter(GameListContract.View view) {
        this.view = view;
        inject();
    }

    void inject() {
        if (TwitchTopGames.getInstance() != null) {
            TwitchTopGames.getInstance().getAppComponent().inject(this);
        }
    }

    @Override
    public void onSuccess(TopGames response) {
        if (view != null) {
            view.dismissRefreshing();
            if (response != null && response.games != null && !response.games.isEmpty()) {
                topGamesCache = response;
                gamesCache.save(response);
                view.updateTopGames(response.games);
            } else {
                //TODO: use cache here if response is malformatted.
                view.showEmptyResult();
            }
        }
    }

    @Override
    public void onError(int responseCode, String message) {
        view.dismissRefreshing();
        view.showError();
        if (isCacheEmpty(topGamesCache)) {
            view.showEmptyResult();
        }
    }

    @Override
    public void onCreate() {
        topGamesCache = retrieveCache();
        refreshTopGames();
    }

    TopGames retrieveCache() {
        TopGames topGames = gamesCache.retrieve();
        if (!isCacheEmpty(topGames)) {
            view.updateTopGames(topGames.games);
        } else {
            view.showLoadingFirst();
        }
        return topGames;
    }

    @Override
    public void refreshTopGames() {
        if (view != null) {
            view.showRefreshing();
            if (isCacheEmpty(topGamesCache)) {
                view.showLoadingFirst();
            }
            apiServices.getGames(this, true);
        }
    }

    boolean isCacheEmpty(TopGames topGamesCache) {
        return topGamesCache == null || topGamesCache.games.isEmpty();
    }

    @Override
    public void onDestroy() {
        apiServices.cancel();
    }

    @Override
    public void onGameSelected(Game game) {
        if (view != null) {
            view.showGameDetails(game);
        }
    }
}

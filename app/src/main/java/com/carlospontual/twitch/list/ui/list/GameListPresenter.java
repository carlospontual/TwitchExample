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
        //TODO: use cache here if response is malformatted.
        if (topGamesCache == null || topGamesCache.games.isEmpty()) {
            view.showEmptyResult();
        }

    }

    @Override
    public void onCreate() {
        topGamesCache = retrieveCache();
        refreshTopGames();
    }

    void inject() {
        if (TwitchTopGames.getInstance() != null) {
            TwitchTopGames.getInstance().getAppComponent().inject(this);
        }
    }

    TopGames retrieveCache() {
        TopGames topGames = gamesCache.retrieve();
        if (topGames != null && !topGames.games.isEmpty()) {
            view.updateTopGames(topGames.games);
        } else {
            //Show loading screen
        }
        return topGames;
    }

    @Override
    public void refreshTopGames() {
        if (view != null) {
            view.showRefreshing();
            apiServices.getGames(this, true);
        }
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

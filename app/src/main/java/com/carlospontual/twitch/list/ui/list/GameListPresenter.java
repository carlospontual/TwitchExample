package com.carlospontual.twitch.list.ui.list;

import com.carlospontual.twitch.list.data.models.Game;
import com.carlospontual.twitch.list.data.models.TopGames;
import com.carlospontual.twitch.list.data.remote.GamesApiServices;
import com.carlospontual.twitch.list.data.remote.ResponseHandler;
import com.carlospontual.twitch.list.data.remote.RetrofitClient;

/**
 * Created by carlospontual on 03/04/16.
 */
public class GameListPresenter implements GameListContract.Presenter, ResponseHandler<TopGames> {

    GameListContract.View view;
    GamesApiServices gamesApiServices;

    public GameListPresenter(GameListContract.View view) {
        this.view = view;
        gamesApiServices = new GamesApiServices(new RetrofitClient());
    }

    @Override
    public void onSuccess(TopGames response) {
        if (view != null) {
            view.dismissRefreshing();
            if (response != null && response.games != null && !response.games.isEmpty()) {
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
        view.showEmptyResult();

    }

    @Override
    public void onCreate() {
        refreshTopGames();
    }

    @Override
    public void refreshTopGames() {
        if (view != null) {
            view.showRefreshing();
            gamesApiServices.getGames(this, true);
        }
    }

    @Override
    public void onDestroy() {
        gamesApiServices.cancel();
    }

    @Override
    public void onGameSelected(Game game) {
        if (view != null) {
            view.showGameDetails(game);
        }
    }
}

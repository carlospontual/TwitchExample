package com.carlospontual.twitch.list.injection.ui;

import com.carlospontual.twitch.list.injection.PerActivity;
import com.carlospontual.twitch.list.ui.list.GameListContract;
import com.carlospontual.twitch.list.ui.list.GameListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by carlospontual on 06/04/16.
 */
@Module
public class GameListPresenterModule {

    private GameListContract.View view;

    private GameListPresenter presenter;

    public GameListPresenterModule(GameListContract.View view) {
        this.view = view;
        presenter = new GameListPresenter(view);
    }

    @PerActivity
    @Provides
    GameListContract.View providesGameListView() {
        return view;
    }

    @PerActivity
    @Provides
    GameListPresenter providesGameListPresenter(GameListContract.View view) {
        return presenter;
    }

    @PerActivity
    @Provides
    GameListContract.Presenter providesPresenter() {
        return presenter;
    }
}

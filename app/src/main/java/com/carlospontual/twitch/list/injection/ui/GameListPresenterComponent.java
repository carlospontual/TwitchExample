package com.carlospontual.twitch.list.injection.ui;

import com.carlospontual.twitch.list.injection.PerActivity;
import com.carlospontual.twitch.list.ui.list.GameListActivity;

import dagger.Component;

/**
 * Created by carlospontual on 06/04/16.
 */
@PerActivity
@Component(modules = GameListPresenterModule.class)
public interface GameListPresenterComponent {
    void inject(GameListActivity gameListActivity);
}

package com.carlospontual.twitch.list.injection;

import com.carlospontual.twitch.list.data.remote.RetrofitClient;
import com.carlospontual.twitch.list.injection.data.CacheModule;
import com.carlospontual.twitch.list.injection.data.GameApiModule;
import com.carlospontual.twitch.list.ui.details.GameDetailsActivity;
import com.carlospontual.twitch.list.ui.list.GameListAdapter;
import com.carlospontual.twitch.list.ui.list.GameListPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by carlospontual on 06/04/16.
 */
@Singleton
@Component(modules = {AppModule.class, CacheModule.class, GameApiModule.class})
public interface AppComponent {

    RetrofitClient getRetrofit();

    void inject(GameDetailsActivity gameDetailsActivity);
    void inject(GameListAdapter adapter);
    void inject(GameListPresenter presenter);
}

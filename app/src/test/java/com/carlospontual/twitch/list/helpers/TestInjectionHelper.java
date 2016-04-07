package com.carlospontual.twitch.list.helpers;

import com.carlospontual.twitch.list.TwitchTopGames;
import com.carlospontual.twitch.list.data.cache.GamesCache;
import com.carlospontual.twitch.list.data.remote.GamesApiServices;
import com.carlospontual.twitch.list.data.remote.RetrofitClient;
import com.carlospontual.twitch.list.injection.AppComponent;
import com.carlospontual.twitch.list.injection.ui.GameListPresenterComponent;
import com.carlospontual.twitch.list.ui.details.GameDetailsActivityTest;
import com.carlospontual.twitch.list.ui.list.GameListActivityTest;
import com.carlospontual.twitch.list.ui.list.GameListContract;
import com.carlospontual.twitch.list.ui.list.GameListPresenter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by carlospontual on 06/04/16.
 */
public class TestInjectionHelper {

    @Singleton
    @Component(modules = MockGameListPresenterModule.class)
    public interface TestGameListPresenterComponent extends GameListPresenterComponent {
        void inject(GameListActivityTest gameListActivityTest);
    }


    @Module
    public static class MockGameListPresenterModule {

        private GameListContract.View view;
        @Mock
        GameListPresenter presenter;


        public MockGameListPresenterModule(GameListContract.View view) {
            this.view = view;
            MockitoAnnotations.initMocks(this);
        }

        @Provides
        @Singleton
        public GameListContract.View providesView() {
            return view;
        }

        @Provides
        @Singleton
        public GameListPresenter providesGameListPresenter(GameListContract.View view) {
            return presenter;
        }

        @Provides
        @Singleton
        public GameListContract.Presenter providesPresenter() {
            return presenter;
        }

    }

    @Singleton
    @Component(modules = MockAppModule.class)
    public interface TestAppComponent extends AppComponent {
        void inject(GameDetailsActivityTest gameDetailsActivityTest);
    }

    @Module
    public static class MockAppModule {
        TwitchTopGames application;
        @Mock
        Picasso picasso;
        @Mock
        RetrofitClient retrofitClient;
        @Mock
        GamesCache gamesCache;
        @Mock
        GamesApiServices gamesApiServices;

        public MockAppModule(TwitchTopGames application) {
            this.application = application;
            MockitoAnnotations.initMocks(this);

            RequestCreator creator = mock(RequestCreator.class);
            when(picasso.load(anyString())).thenReturn(creator);
            when(creator.placeholder(anyInt())).thenReturn(creator);
        }

        @Provides
        @Singleton
        public TwitchTopGames providesApplication() {
            return application;
        }

        @Provides
        @Singleton public Picasso providesPicasso(TwitchTopGames application) { return picasso; }

        @Provides
        @Singleton public RetrofitClient providesRetrofit() {
            return retrofitClient;
        }

        @Provides
        @Singleton GamesCache providesCache() { return gamesCache; }

        @Provides
        @Singleton GamesApiServices providesGamesApiServices() { return gamesApiServices; }
    }
}

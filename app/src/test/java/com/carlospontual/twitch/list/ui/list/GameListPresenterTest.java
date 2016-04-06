package com.carlospontual.twitch.list.ui.list;

import com.carlospontual.twitch.list.MockHelpers;
import com.carlospontual.twitch.list.data.models.Game;
import com.carlospontual.twitch.list.data.models.TopGames;
import com.carlospontual.twitch.list.data.remote.GamesApiServices;
import com.carlospontual.twitch.list.ui.list.GameListContract;
import com.carlospontual.twitch.list.ui.list.GameListPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by carlospontual on 03/04/16.
 */
public class GameListPresenterTest {

    @Mock
    GameListContract.View view;
    @Mock
    GamesApiServices services;
    GameListPresenter presenter, spyPresenter;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new GameListPresenter(view);
        presenter.gamesApiServices = services;
        spyPresenter = spy(presenter);
    }

    @Test
    public void onSuccess_sould_dismiss_view_refresh() {
        presenter.onSuccess(null);
        verify(view, times(1)).dismissRefreshing();
    }

    @Test
    public void onSuccess_sould_update_games_with_correct_return() {
        TopGames topGames = MockHelpers.mockData();
        presenter.onSuccess(topGames);
        verify(view, times(1)).updateTopGames(topGames.games);
    }

    @Test
    public void onSuccess_sould_show_empty_view_with_null_result() {
        presenter.onSuccess(null);
        verifyEmptyResult();
    }

    @Test
    public void onSuccess_sould_show_empty_view_with_null_game_list() {
        presenter.onSuccess(mock(TopGames.class));
        verifyEmptyResult();
    }

    @Test
    public void onSuccess_sould_show_empty_view_with_empty_game_list() {
        TopGames topGames = new TopGames(0, Collections.<Game>emptyList(), null);
        presenter.onSuccess(topGames);
        verifyEmptyResult();
    }

    private void verifyEmptyResult() {
        verify(view, times(1)).dismissRefreshing();
        verify(view, times(1)).showEmptyResult();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void onError_should_dismissRefreshing_showError_and_showEmptyResult() {
        presenter.onError(-1, null);
        verify(view, times(1)).dismissRefreshing();
        verify(view, times(1)).showError();
        verify(view, times(1)).showEmptyResult();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void onCreate_should_refreshTopGames() {
        doNothing().when(spyPresenter).refreshTopGames();
        spyPresenter.onCreate();
        verify(spyPresenter, times(1)).refreshTopGames();
    }

    @Test
    public void refreshTopGames_should_showRefreshing() {
        presenter.refreshTopGames();
        verify(view, times(1)).showRefreshing();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void refreshTopGames_should_getGames() {
        presenter.refreshTopGames();
        verify(services, times(1)).getGames(presenter, true);
        verifyNoMoreInteractions(services);
    }

    @Test
    public void onDestroy_should_cancel_request() {
        presenter.onDestroy();
        verify(services, times(1)).cancel();
        verifyNoMoreInteractions(services);
    }


}
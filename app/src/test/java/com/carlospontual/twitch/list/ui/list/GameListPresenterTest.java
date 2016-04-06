package com.carlospontual.twitch.list.ui.list;

import com.carlospontual.twitch.list.MockHelpers;
import com.carlospontual.twitch.list.data.cache.GamesCache;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by carlospontual on 03/04/16.
 */
public class GameListPresenterTest {

    @Mock
    GameListContract.View view;
    @Mock
    GamesApiServices services;
    GameListPresenter presenter, spyPresenter;
    @Mock
    GamesCache cache;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new GameListPresenter(view);
        presenter.gamesCache = cache;
        presenter.apiServices = services;
        spyPresenter = spy(presenter);
        doReturn(false).when(spyPresenter).isCacheEmpty(any(TopGames.class));
    }

    @Test
    public void onSuccess_sould_dismiss_view_refresh() {
        presenter.onSuccess(null);
        verify(view, times(1)).dismissRefreshing();
    }

    @Test
    public void onSuccess_sould_store_games_cache_on_correct_return() {
        TopGames topGames = MockHelpers.mockData();
        presenter.onSuccess(topGames);
        verify(cache, times(1)).save(topGames);
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
    public void onError_should_dismissRefreshing_showError() {
        spyPresenter.onError(-1, null);
        verify(view, times(1)).dismissRefreshing();
        verify(view, times(1)).showError();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void onError_should_showEmptyResult_if_cache_isEmpty() {
        doReturn(true).when(spyPresenter).isCacheEmpty(any(TopGames.class));
        spyPresenter.onError(-1, null);
        verify(view, times(1)).showEmptyResult();
    }

    @Test
    public void onCreate_should_retrieveCache_and_refreshTopGames() {
        doNothing().when(spyPresenter).refreshTopGames();
        when(cache.retrieve()).thenReturn(MockHelpers.mockData());
        spyPresenter.onCreate();
        verify(spyPresenter, times(1)).retrieveCache();
        verify(spyPresenter, times(1)).refreshTopGames();
    }

    @Test
    public void retrieveCache_should_read_cache_values() {
        presenter.retrieveCache();
        verify(cache, times(1)).retrieve();
    }

    @Test
    public void retrieveCache_should_return_cache_value() {
        TopGames mock = MockHelpers.mockData();
        when(cache.retrieve()).thenReturn(mock);
        assertEquals(mock, presenter.retrieveCache());
    }

    @Test
    public void retrieveCache_should_updateTopGames_when_cache_not_empty() {
        TopGames mock = MockHelpers.mockData();
        when(cache.retrieve()).thenReturn(mock);
        presenter.retrieveCache();
        verify(view, times(1)).updateTopGames(mock.games);
        verifyNoMoreInteractions(view);
    }

    @Test
    public void retrieveCache_should_showLoadingFirst_when_cache_empty() {
        doReturn(true).when(spyPresenter).isCacheEmpty(any(TopGames.class));
        spyPresenter.retrieveCache();
        verify(view, times(1)).showLoadingFirst();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void refreshTopGames_should_showRefreshing() {
        spyPresenter.refreshTopGames();
        verify(view, times(1)).showRefreshing();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void refreshTopGames_should_showLoadingFirst_when_cache_isEmpty() {
        doReturn(true).when(spyPresenter).isCacheEmpty(any(TopGames.class));
        spyPresenter.refreshTopGames();
        verify(view, times(1)).showRefreshing();
    }

    @Test
    public void refreshTopGames_should_getGames() {
        presenter.refreshTopGames();
        verify(services, times(1)).getGames(presenter, true);
        verifyNoMoreInteractions(services);
    }

    @Test
    public void isCacheEmpty_should_return_true_when_null() {
        assertTrue(presenter.isCacheEmpty(null));
    }

    @Test
    public void isCacheEmpty_should_return_true_when_empty_games() {
        TopGames topGames = new TopGames(0, Collections.<Game>emptyList(), null);
        assertTrue(presenter.isCacheEmpty(topGames));
    }

    @Test
    public void isCacheEmpty_should_return_false_for_expected_result() {
        assertFalse(presenter.isCacheEmpty(MockHelpers.mockData()));
    }

    @Test
    public void onDestroy_should_cancel_request() {
        presenter.onDestroy();
        verify(services, times(1)).cancel();
        verifyNoMoreInteractions(services);
    }

    @Test
    public void onGameSelected_should_show_gameDetails() {
        Game game = mock(Game.class);
        presenter.onGameSelected(game);
        verify(view, times(1)).showGameDetails(game);
    }

}

package com.carlospontual.twitch.list.ui.details;

import android.content.Intent;
import android.os.Bundle;

import com.carlospontual.twitch.list.BuildConfig;
import com.carlospontual.twitch.list.TwitchTopGames;
import com.carlospontual.twitch.list.data.models.Game;
import com.carlospontual.twitch.list.data.models.TopGames;
import com.carlospontual.twitch.list.helpers.MockHelpers;
import com.carlospontual.twitch.list.helpers.TestInjectionHelper;
import com.squareup.picasso.Picasso;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

/**
 * Created by ccastro on 4/6/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = 27)
public class GameDetailsActivityTest {

    GameDetailsActivity gameDetailsActivity, spyActivity;

    @Inject
    Picasso picasso;

    TopGames mockGames;
    Game firstGame;

    @Before
    public void setUp() {
        mockGames = MockHelpers.mockData();
        firstGame = mockGames.games.get(0);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Game.GAME_TAG, firstGame);
        intent.putExtras(bundle);

        gameDetailsActivity = ActivityController.of(new GameDetailsActivity(), intent)
                .create()
                .get();

        inject();
        spyActivity = spy(gameDetailsActivity);
    }

    private void inject() {
        ((TestInjectionHelper.TestAppComponent) TwitchTopGames.getInstance()
                .getAppComponent()).inject(this);
        spyActivity = spy(gameDetailsActivity);
    }

    @Test
    public void onCreate_should_set_current_game() {
        assertEquals(mockGames.games.get(0), spyActivity.currentGame);
    }

    @Test
    public void populate_should_set_title() {
        gameDetailsActivity.populate();
        assertEquals(firstGame.gameData.name, gameDetailsActivity.gameTitle.getText()
                .toString());
    }

    @Test
    public void populate_should_set_channels() {
        gameDetailsActivity.populate();
        assertEquals(firstGame.channels, Integer.parseInt(gameDetailsActivity.gameChannels.getText()
                                                                  .toString()));
    }

    @Test
    public void populate_should_set_viewers() {
        gameDetailsActivity.populate();
        assertEquals(firstGame.viewers, Integer.parseInt(gameDetailsActivity.gameViews.getText()
                                                                 .toString()));
    }

}

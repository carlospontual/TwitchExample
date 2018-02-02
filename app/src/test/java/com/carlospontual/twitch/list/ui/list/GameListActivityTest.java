package com.carlospontual.twitch.list.ui.list;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.carlospontual.twitch.list.BuildConfig;
import com.carlospontual.twitch.list.R;
import com.carlospontual.twitch.list.data.models.Game;
import com.carlospontual.twitch.list.data.models.TopGames;
import com.carlospontual.twitch.list.helpers.DaggerTestInjectionHelper_TestGameListPresenterComponent;
import com.carlospontual.twitch.list.helpers.MockHelpers;
import com.carlospontual.twitch.list.helpers.ShadowSnackbar;
import com.carlospontual.twitch.list.helpers.TestInjectionHelper;
import com.carlospontual.twitch.list.injection.ui.GameListPresenterComponent;
import com.carlospontual.twitch.list.ui.details.GameDetailsActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by carlospontual on 06/04/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = 27,
        shadows = {ShadowSnackbar.class})
public class GameListActivityTest {

    private GameListActivity customActivity = getCustomActivity();

    private GameListActivity robolectricActivity;

    private GameListPresenterComponent component;

    @Inject
    GameListContract.Presenter presenter;

    private ActivityController<GameListActivity> activityController;

    @Before
    public void setUp() throws Exception {
        ((TestInjectionHelper.TestGameListPresenterComponent) getTestComponent()).inject(this);
        activityController = ActivityController.of(customActivity).setup();
        robolectricActivity = activityController.get();

    }

    @Test
    public void onCreate_should_call_presenter() {
        //Activity already initialized after "activityController.get();"
        verify(presenter, times(1)).onCreate();
        verifyNoMoreInteractions(presenter);
    }

    @Test
    public void showError_should_show_error_SnackBar() {
        robolectricActivity.showError();
        Snackbar snackbar = ShadowSnackbar.getLatestSnackbar();
        assertNotNull(snackbar);
        assertEquals(robolectricActivity.getString(R.string.game_list_error_offline),
                     ShadowSnackbar.getTextOfLatestSnackbar());
    }

    @Test
    public void updateTopGames_should_update_adapter() {
        assertNotNull("Adapter should not be null", robolectricActivity.adapter);
        assertEquals("Adapter initial size should be zero", 0, robolectricActivity.adapter.getItemCount());
        TopGames mockGames = MockHelpers.mockData();
        robolectricActivity.updateTopGames(mockGames.games);
        assertEquals("Adapter should have same quantity of games as mocked data", mockGames.games.size(),
                     robolectricActivity.adapter.getItemCount());
    }

    @Test
    public void showGameDetails_should_start_GameDetailsActivity() {
        ShadowActivity shadowActivity = Shadow.extract(robolectricActivity);
        TopGames mockGames = MockHelpers.mockData();
        robolectricActivity.showGameDetails(mockGames.games.get(0));

        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertThat(startedIntent.getComponent()
                           .getClassName(), equalTo(GameDetailsActivity.class.getName()));

        Game bundleGame = (Game) startedIntent.getExtras().getSerializable(Game.GAME_TAG);
        assertEquals(mockGames.games.get(0).gameData.name, bundleGame.gameData.name);

    }

    @Test
    public void showEmptyResult_should_call_showEmptyErrorScreen_no_first_load() {
        GameListActivity spyAct = spy(robolectricActivity);
        spyAct.showEmptyResult();
        verify(spyAct, times(1)).showEmptyErrorScreen(false);
    }

    @Test
    public void showLoadingFirst_should_call_showEmptyErrorScreen_first_load() {
        GameListActivity spyAct = spy(robolectricActivity);
        spyAct.showLoadingFirst();
        verify(spyAct, times(1)).showEmptyErrorScreen(true);
    }

    @Test
    public void showEmptyErrorScreen_should_toggleListVisibility() {
        GameListActivity spyAct = spy(robolectricActivity);
        spyAct.showEmptyErrorScreen(false);
        verify(spyAct, times(1)).toggleListVisibility(false);
    }

    @Test
    public void showEmptyErrorScreen_on_firstload_should_show_Progress_hide_emptyViewMessage() {
        robolectricActivity.showEmptyErrorScreen(true);
        assertInvisible(robolectricActivity.emptyViewText, "Empty View message");
        assertVisible(robolectricActivity.progressFirstLoad, "Progress");
    }

    @Test
    public void showEmptyErrorScreen_not_firstload_should_hide_Progress_show_emptyViewMessage() {
        robolectricActivity.showEmptyErrorScreen(false);
        assertVisible(robolectricActivity.emptyViewText, "Empty View message");
        assertGone(robolectricActivity.progressFirstLoad, "Progress");
    }

    @Test
    public void toggleListVisibility_list_visible_should_show_swipeRefresh_hide_emptyView() {
        robolectricActivity.toggleListVisibility(true);
        assertVisible(robolectricActivity.swipeRefreshLayout, "Swipe Refresh layout");
        assertGone(robolectricActivity.emptyView, "EmptyView");
    }

    @Test
    public void toggleListVisibility_list_not_visible_should_show_swipeRefresh_hide_emptyView() {
        robolectricActivity.toggleListVisibility(false);
        assertGone(robolectricActivity.swipeRefreshLayout, "Swipe Refresh layout");
        assertVisible(robolectricActivity.emptyView, "EmptyView");
    }

    private void assertVisible(View view, String fieldName) {
        assertEquals(fieldName + " should be visible", View.VISIBLE, view.getVisibility());
    }

    private void assertInvisible(View view, String fieldName) {
        assertEquals(fieldName + " should be invisible", View.INVISIBLE, view.getVisibility());
    }

    private void assertGone(View view, String fieldName) {
        assertEquals(fieldName + " should be gone", View.GONE, view.getVisibility());
    }

    @Test
    public void refreshGames_should_refreshGames() {
        robolectricActivity.refreshGames();
        verify(presenter, times(1)).refreshTopGames();
    }

    @Test
    public void onGameClicked_should_call_onGameSelected() {
        TopGames topGames = MockHelpers.mockData();
        robolectricActivity.onGameClicked(topGames.games.get(0));
        verify(presenter, times(1)).onGameSelected(topGames.games.get(0));
    }


    public GameListPresenterComponent getTestComponent() {
        if (component == null) {
            component = DaggerTestInjectionHelper_TestGameListPresenterComponent.builder()
                    .mockGameListPresenterModule(
                            new TestInjectionHelper.MockGameListPresenterModule(customActivity))
                    .build();
        }
        return component;
    }

    private GameListActivity getCustomActivity() {
        return new GameListActivity() {
            @Override
            GameListPresenterComponent getComponent() {
                return getTestComponent();
            }
        };
    }

}

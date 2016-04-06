package com.carlospontual.twitch.list.ui.list;

import android.content.Context;
import android.test.mock.MockContext;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlospontual.twitch.list.MockHelpers;
import com.carlospontual.twitch.list.data.models.Game;
import com.carlospontual.twitch.list.data.models.GameData;
import com.carlospontual.twitch.list.data.models.TopGames;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
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
public class GameListAdapterTest {

    MockContext context;

    GameListAdapter adapter, spyAdapter;
    @Mock
    Picasso picasso;
    @Mock
    View inflatedView;
    @Mock
    GameListAdapter.OnGameSelectedListener listener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); //TODO: remove if not used
        adapter = new GameListAdapter(mock(Context.class), listener);
        adapter.picasso = picasso;
        spyAdapter = spy(adapter);
        doReturn(inflatedView).when(spyAdapter).inflateView(anyInt(), any(ViewGroup.class), anyBoolean());
//        doReturn(picasso).when(spyAdapter).getPicasso();
        spyAdapter.picasso = picasso;
        doNothing().when(spyAdapter).bindViews(anyObject(), any(View.class));
    }

    @Test
    public void onCreateViewHolder_should_return_a_GameViewHolder() {
        assertThat(spyAdapter.onCreateViewHolder(mock(ViewGroup.class), 0),
                instanceOf(GameListAdapter.GameViewHolder.class));
    }

    @Test
    public void onBindViewHolder_should_populate_view_with_correct_object() {
        TopGames topGames = MockHelpers.mockData();
        GameListAdapter.GameViewHolder holder = mock(GameListAdapter.GameViewHolder.class);
        spyAdapter.games = topGames.games;
        spyAdapter.onBindViewHolder(holder, 1);
        verify(holder, times(1)).populate(topGames.games.get(1));
        verifyNoMoreInteractions(holder);
    }

    @Test
    public void isListEmpty_should_return_true_when_null() {
        assertTrue(spyAdapter.isListEmpty());
    }

    @Test
    public void isListEmpty_should_return_true_when_empty() {
        spyAdapter.games = Collections.emptyList();
        assertTrue(spyAdapter.isListEmpty());
    }

    @Test
    public void isListEmpty_should_return_false_when_list_has_elements() {
        spyAdapter.games = MockHelpers.mockData().games;
        assertFalse(spyAdapter.isListEmpty());
    }

    @Test
    public void getItemCount_should_return_list_size() {
        TopGames topGames = MockHelpers.mockData();
        spyAdapter.games = topGames.games;
        assertEquals(topGames.games.size(), spyAdapter.getItemCount());
    }

    @Test
    public void GameVH_populate_should_load_image() {
        Game game = MockHelpers.mockData().games.get(1);
        RequestCreator creator = mock(RequestCreator.class);
        GameListAdapter.GameViewHolder mockVH = prepareGameVH(game.gameData, creator);
        mockVH.populate(game);
        verify(picasso, times(1)).load(game.gameData.boxImages.large);
        verify(creator, times(1)).into(mockVH.cover);
    }

    @Test
    public void GameVH_populate_should_set_title() {
        Game game = MockHelpers.mockData().games.get(1);
        GameListAdapter.GameViewHolder mockVH = prepareGameVH(game.gameData, null);
        mockVH.populate(game);
        verify(mockVH.title, times(1)).setText(game.gameData.name);
    }

    @Test
    public void GameVH_click_should_call_listener() {
        Game game = MockHelpers.mockData().games.get(1);
        GameListAdapter.GameViewHolder mockVH = prepareGameVH(game.gameData, null);
        mockVH.populate(game);
        mockVH.onGameClick();
        verify(listener, times(1)).onGameClicked(game);
    }

    private GameListAdapter.GameViewHolder prepareGameVH(GameData gameData, RequestCreator creator) {
        View view = mock(View.class);
        GameListAdapter.GameViewHolder mockVH = spyAdapter.new GameViewHolder(view);
        GameListAdapter.GameViewHolder spyMockVH = spy(mockVH);
        spyMockVH.cover = mock(ImageView.class);
        spyMockVH.title = mock(TextView.class);
        if (creator == null) {
            creator = mock(RequestCreator.class);
        }
        when(picasso.load(gameData.boxImages.large)).thenReturn(creator);
        when(creator.placeholder(anyInt())).thenReturn(creator);

        return spyMockVH;
    }
}

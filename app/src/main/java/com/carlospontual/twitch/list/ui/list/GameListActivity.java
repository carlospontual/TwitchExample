package com.carlospontual.twitch.list.ui.list;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.carlospontual.twitch.list.R;
import com.carlospontual.twitch.list.data.models.Game;
import com.carlospontual.twitch.list.ui.details.GameDetailsActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameListActivity extends AppCompatActivity implements GameListContract.View,
        GameListAdapter.OnGameSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler)
    RecyclerView recycler;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.lyt_empty_list)
    View emptyView;

    GameListAdapter adapter;
    GameListContract.Presenter presenter;
    boolean refreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initViews();
        if (presenter != null) {
            presenter.onCreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initViews() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        presenter = new GameListPresenter(this);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new GameListAdapter(this, this);
        recycler.setAdapter(adapter);
        swipeRefreshHack();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshGames();
            }
        });
    }

    private void swipeRefreshHack() {
        swipeRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    swipeRefreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    swipeRefreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setRefreshing(true);

                }
            }
        });
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showRefreshing() {
        refreshing = true;
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void dismissRefreshing() {
        refreshing = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError() {
        Snackbar.make(fab, R.string.game_list_error_offline, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void updateTopGames(List<Game> games) {
        if (adapter != null) {
            toggleListVisibility(true);
            adapter.setGames(games);
        }
    }

    @Override
    public void showGameDetails(Game game) {
        Intent gameDetails = new Intent(this, GameDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Game.GAME_TAG, game);
        gameDetails.putExtras(bundle);
        startActivity(gameDetails);
    }

    @Override
    public void showEmptyResult() {
        toggleListVisibility(false);
    }

    private void toggleListVisibility(boolean isListVisible) {
        swipeRefreshLayout.setVisibility(isListVisible ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(isListVisible ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.btn_reload)
    public void refreshGames() {
        if (presenter != null) {
            presenter.refreshTopGames();
        }
    }

    @Override
    public void onGameClicked(Game game) {
        if (presenter != null) {
            presenter.onGameSelected(game);
        }
    }
}

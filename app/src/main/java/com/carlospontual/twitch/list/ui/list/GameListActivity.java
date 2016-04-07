package com.carlospontual.twitch.list.ui.list;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.carlospontual.twitch.list.R;
import com.carlospontual.twitch.list.data.models.Game;
import com.carlospontual.twitch.list.injection.PerActivity;
import com.carlospontual.twitch.list.injection.ui.DaggerGameListPresenterComponent;
import com.carlospontual.twitch.list.injection.ui.GameListPresenterComponent;
import com.carlospontual.twitch.list.injection.ui.GameListPresenterModule;
import com.carlospontual.twitch.list.ui.details.GameDetailsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameListActivity extends AppCompatActivity implements GameListContract.View,
        GameListAdapter.OnGameSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler)
    RecyclerView recycler;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.lyt_empty_list)
    View emptyView;
    @Bind(R.id.img_empty_list)
    ImageView emptyViewImage;
    @Bind(R.id.lyt_empty_text)
    View emptyViewText;
    @Bind(R.id.pgr_first_load)
    ProgressBar progressFirstLoad;

    @PerActivity @Inject
    GameListContract.Presenter presenter;

    GameListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getComponent().inject(this);

        initViews();
        if (presenter != null) {
            presenter.onCreate();
        }
    }

    private void initViews() {
        ButterKnife.bind(this);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }
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
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorAccent), 0, 0, 0);
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

    @Override
    public void showRefreshing() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void dismissRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError() {
        Snackbar.make(recycler, R.string.game_list_error_offline, Snackbar.LENGTH_LONG).show();
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
        showEmptyErrorScreen(false);
    }

    @Override
    public void showLoadingFirst() {
        showEmptyErrorScreen(true);
    }

    void showEmptyErrorScreen(boolean isFirstLoading) {
        toggleListVisibility(false);
        emptyViewImage.setImageResource(isFirstLoading ? R.drawable.img_cloud_sync
                : R.drawable.img_cloud_error_sync);
        emptyViewText.setVisibility(isFirstLoading ? View.INVISIBLE : View.VISIBLE);
        progressFirstLoad.setVisibility(isFirstLoading ? View.VISIBLE : View.GONE);
    }

    void toggleListVisibility(boolean isListVisible) {
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

    GameListPresenterComponent getComponent() {
        return DaggerGameListPresenterComponent.builder()
                .gameListPresenterModule(new GameListPresenterModule(this))
                .build();
    }
}

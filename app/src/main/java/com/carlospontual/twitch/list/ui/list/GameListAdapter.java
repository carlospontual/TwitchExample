package com.carlospontual.twitch.list.ui.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlospontual.twitch.list.R;
import com.carlospontual.twitch.list.TwitchTopGames;
import com.carlospontual.twitch.list.data.models.Game;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by carlospontual on 03/04/16.
 */
public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {

    List<Game> games;
    Context context;

    OnGameSelectedListener listener;

    @Inject
    Picasso picasso;

    public GameListAdapter(Context context, OnGameSelectedListener listener) {
        this(context, null, listener);
    }

    public GameListAdapter(Context context, List<Game> games, OnGameSelectedListener listener) {
        this.context = context;
        this.games = games;
        this.listener = listener;
        inject();
    }

    void inject() {
        if (TwitchTopGames.getInstance() != null) {
            TwitchTopGames.getInstance().getAppComponent().inject(this);
        }
    }

    public void setGames(List<Game> games) {
        this.games = games;
        notifyDataSetChanged();
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameViewHolder(inflateView(R.layout.game_list_card, parent, false));
    }

    View inflateView(int viewId, ViewGroup parent, boolean attachToParent) {
        return LayoutInflater.from(parent.getContext()).inflate(viewId, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        if (games != null) {
            Game currentGame = games.get(position);
            holder.populate(currentGame);
        }
    }

    @Override
    public int getItemCount() {
        return isListEmpty() ? 0 : games.size();
    }

    boolean isListEmpty() {
        return games == null || games.size() == 0;
    }

    void bindViews(Object target, View view) {
        ButterKnife.bind(target, view);
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_game)
        ImageView cover;
        @BindView(R.id.txt_game_title)
        TextView title;
        Game game;


        public GameViewHolder(View itemView) {
            super(itemView);
            bindViews(this, itemView);
        }

        public void populate(Game game) {
            this.game = game;
            if (game.gameData.boxImages.large != null && !game.gameData.boxImages.large.isEmpty()) {
                picasso.load(game.gameData.boxImages.large)
                        .placeholder(R.drawable.camera_loading)
                        .into(cover);
            }
            title.setText(game.gameData.name);
        }

        @OnClick(R.id.card_view)
        public void onGameClick() {
            if (listener != null) {
                listener.onGameClicked(game);
            }
        }
    }

    public interface OnGameSelectedListener {
        void onGameClicked(Game game);
    }
}

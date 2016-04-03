package com.carlospontual.twitch.list.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlospontual.twitch.list.BuildConfig;
import com.carlospontual.twitch.list.R;
import com.carlospontual.twitch.list.data.models.Game;
import com.carlospontual.twitch.list.data.models.GameData;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by carlospontual on 03/04/16.
 */
public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {

    List<Game> games;
    Context context;
    Picasso picasso;

    public GameListAdapter(Context context) {
        this(context, null);
    }

    public GameListAdapter(Context context, List<Game> games) {
        this.context = context;
        this.games = games;
        picasso = new Picasso.Builder(context).downloader(new OkHttp3Downloader(context, Integer.MAX_VALUE)).build();
        if (BuildConfig.DEBUG) {
            picasso.setIndicatorsEnabled(true);
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
        Game currentGame = games.get(position);
        holder.populate(currentGame.gameData);
    }

    @Override
    public int getItemCount() {
        return isListEmpty() ? 0 : games.size();
    }

    boolean isListEmpty() {
        return games == null || games.size() == 0;
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_game)
        ImageView cover;
        @Bind(R.id.txt_game_title)
        TextView title;

        public GameViewHolder(View itemView) {
            super(itemView);
            bindViews(itemView);
        }

        private void bindViews(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        public void populate(GameData gameData) {
            if (gameData.boxImages.large != null && !gameData.boxImages.large.isEmpty()) {
                picasso.load(gameData.boxImages.large)
                        .placeholder(R.drawable.camera_loading)
                        .into(cover);
            }
            title.setText(gameData.name);
        }
    }
}

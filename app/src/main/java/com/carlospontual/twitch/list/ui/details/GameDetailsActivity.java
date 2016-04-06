package com.carlospontual.twitch.list.ui.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlospontual.twitch.list.R;
import com.carlospontual.twitch.list.data.models.Game;
import com.carlospontual.twitch.list.data.models.GameData;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by carlospontual on 04/04/16.
 */
public class GameDetailsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.img_game_logo)
    ImageView imgGameLogo;
    @Bind(R.id.txt_game_title)
    TextView gameTitle;
    @Bind(R.id.txt_game_channels)
    TextView gameChannels;
    @Bind(R.id.txt_game_views)
    TextView gameViews;
    @Bind(R.id.img_game)
    ImageView imgGameBox;

    Game currentGame;
    Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_details_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        picasso = new Picasso.Builder(this).downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE)).build();
        retrieveGame();
        if (currentGame != null) {
            populate();
        }
    }

    private void retrieveGame() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(Game.GAME_TAG)) {
            currentGame = (Game) extras.getSerializable(Game.GAME_TAG);
        }
    }

    private void populate() {
        GameData gameData = currentGame.gameData;
        if (gameData.logoImages != null && gameData.logoImages.large != null) {
            picasso.load(gameData.logoImages.large)
                    .into(imgGameLogo);
        }

        if (gameData.boxImages != null && gameData.boxImages.large != null) {
            picasso.load(gameData.boxImages.large)
                    .into(imgGameBox);
        }

        gameTitle.setText(gameData.name);
        gameChannels.setText(Integer.toString(currentGame.channels));
        gameViews.setText(Integer.toString(currentGame.viewers));
    }
}

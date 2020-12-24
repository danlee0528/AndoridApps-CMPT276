package com.example.daniellee.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private ImageView loadingImage;
    private TextView gameName;
    private TextView authorName;

    private final GameConfiguration gameConfig = GameConfiguration.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MediaPlayer openingSound = MediaPlayer.create(this, R.raw.opening_jingle);
        openingSound.start();
        runAnimation();
        setSkipButton();
        loadGameConfig();
    }

    // Loads all stored values form shared preferences
    // Gson used to convert integer array in to json format for easy storage and access
    private void loadGameConfig(){
        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREF_NAME), MODE_PRIVATE);
        // Loading Board Size
        gameConfig.set_boardSize(prefs.getInt(getString(R.string.PREF_NUM_ROW),4),
                prefs.getInt(getString(R.string.PREF_NUM_COL),6));

        // Loading Number of Mines
        gameConfig.setNUM_MINES(prefs.getInt(getString(R.string.PREF_NUM_MINES),6));

        // Loading Total Games Played
        gameConfig.setTotalGamesPlayed(prefs.getInt(getString(R.string.PREF_GAMES_PLAYED),0));

        // Loading User HiScores for each Configuration
        Gson gson = new Gson();
        String json = prefs.getString(getString(R.string.PREF_USER_SCORE), "");
        if (!json.equals("")) {
            gameConfig.setUserHiScores(gson.fromJson(json, int[].class));
        }
    }

    private void runAnimation() {
        loadingImage = (ImageView) findViewById(R.id.loading_image);
        gameName = (TextView) findViewById(R.id.mine_seeker_header);
        authorName = (TextView) findViewById(R.id.author_name);
        Animation rotate_loadingImage = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.loading_image_spin);
        Animation fade_text = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.name_fade);
        loadingImage.startAnimation(rotate_loadingImage);
        gameName.startAnimation(fade_text);
        authorName.startAnimation(fade_text);
    }

    private void setSkipButton() {
        Button skipButton = (Button) findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go2Menu = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(go2Menu);
                finish();
            }
        });
    }

}

package com.example.daniellee.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity {
    private ImageView skeleton_flying_image;
    private ImageView skeleton_with_bomb_image;
    private ImageView air_ballons_image;
    private Button playButton;
    private Button helpButton;
    private Button optionButton;
    private final String helpDialogTag = "Help Dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        MediaPlayer backgroundMusic = MediaPlayer.create(this, R.raw.background_theme_music);
        backgroundMusic.start();
        setImages();
        setPlayButton();
        setHelpButton();
        setOptionButton();
    }

    private void setPlayButton() {
        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2PlayActivity = new Intent(getApplicationContext(), PlayActivity.class);
                startActivity(intent2PlayActivity);
            }
        });
    }

    private void setHelpButton() {
        helpButton = (Button) findViewById(R.id.help_button);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                HelpAcitivty dialog = new HelpAcitivty();
                dialog.show(manager, helpDialogTag);
                //Log.i("MyApp", "Just  showed the dialog");
            }
        });
    }

    private void setOptionButton() {
        optionButton = (Button) findViewById(R.id.options_button);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2OptionActivity = new Intent(getApplicationContext(), OptionsActivity.class);
                startActivity(intent2OptionActivity);
            }
        });
    }

    private void setImages() {
        skeleton_flying_image = (ImageView) findViewById(R.id.skeleton_flying_image);
        skeleton_with_bomb_image = (ImageView) findViewById(R.id.skeleton_with_bomb_image);
        air_ballons_image = (ImageView) findViewById(R.id.background_ballon_image);
    }
}

package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

/**
 * Main menu. Is connected to the SelectJourney, CarbonFootprint and Utilities screens.
 * Nothing too special.
 * <p>
 * Car icon taken from: https://thenounproject.com/term/car/72/
 * Open source.
 */


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.TipHelperSingleton;

public class MainMenu extends AppCompatActivity {

    private static final String SHAREDPREF_SET = "CarbonFootprintTrackerTips";

    public static final String tJEmission = "JEmission";
    public static final String tJDist = "JDist";
    public static final String tNGasAmount = "NGasAmount";
    public static final String tNGasEmission = "NGasEmission";
    public static final String tElecAmount = "ElecAmount";
    public static final String tElecEmission = "ElecEmission";
    public static final String tLastUtil = "LastUtil";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setupButtons();
        loadTips();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Loads the previous state of the tiphelpersingleton from the last launch
    private void loadTips() {
        TipHelperSingleton tipHelper = TipHelperSingleton.getInstance();
        SharedPreferences kpref = getSharedPreferences(SHAREDPREF_SET, MODE_PRIVATE);
        tipHelper.setJourneyEmission(Double.longBitsToDouble(kpref.getLong(tJEmission, 0)));
        tipHelper.setJourneyDist(Double.longBitsToDouble(kpref.getLong(tJDist, 0)));
        tipHelper.setnGasAmount(Double.longBitsToDouble(kpref.getLong(tNGasAmount, 0)));
        tipHelper.setnGasEmission(Double.longBitsToDouble(kpref.getLong(tNGasEmission, 0)));
        tipHelper.setElecAmount(Double.longBitsToDouble(kpref.getLong(tElecAmount, 0)));
        tipHelper.setElecEmission(Double.longBitsToDouble(kpref.getLong(tElecEmission, 0)));
        tipHelper.setLastUtil(kpref.getString(tLastUtil, ""));
    }

    private void setupButtons() {
        // directs to "SelectJourney" screen
        ImageButton journey_button = (ImageButton) findViewById(R.id.create_a_new_journey_button);
        journey_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainMenu2SelectJourney = SelectJourney.makeIntent(MainMenu.this);
                startActivity(MainMenu2SelectJourney);
            }
        });
        // directs to "CarbonFootPrint" screen
        ImageButton footprint_button = (ImageButton) findViewById(R.id.view_cabon_footprint_button);
        footprint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainMenu2DataActivityPicker = DataActivityPicker.makeIntent(MainMenu.this);
                startActivity(MainMenu2DataActivityPicker);
            }
        });
        // directs to "Utility Bill" screen
        ImageButton utility_button = (ImageButton) findViewById(R.id.utility_add_button);
        utility_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainMenu2Utilities = SelectUtilities.makeIntent(MainMenu.this);
                startActivity(MainMenu2Utilities);
            }
        });
        // directs to "About" screen
        ImageButton about_button = (ImageButton) findViewById(R.id.about_btn);
        about_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                AboutFragment dialog = new AboutFragment();
                dialog.show(manager, "About");
            }
        });

        //directs to Settings screen
        ImageButton settings_button = (ImageButton) findViewById(R.id.btnSettings);
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainMenu2Settings = SettingsActivity.makeIntent(MainMenu.this);
                startActivity(MainMenu2Settings);
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenu.class);
    }
}

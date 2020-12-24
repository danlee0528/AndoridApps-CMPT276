package com.example.daniellee.myapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;

public class OptionsActivity extends AppCompatActivity {


    private final GameConfiguration gameConfig = GameConfiguration.getInstance();
    private Spinner spinner_for_boardSize;
    private Spinner spinner_for_number_of_mines;
    private Button ok_button;
    private TextView currentNumMines;
    private TextView currentBoardSize;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        updateCurrentBoardSize();
        updateCurrentNumMines();

        setSpinner4BoardSize();
        setSpinner4NumberofMines();
        saveConfig();
        setOkButton();
        setResetDataButton();
    }

    // Saves the Configuration user selected
    // Triggered when the user presses the okay button
    private void saveConfig() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREF_NAME), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(getString(R.string.PREF_NUM_MINES), gameConfig.getNUM_MINES());
        editor.putInt(getString(R.string.PREF_NUM_ROW), gameConfig.getNUM_ROW());
        editor.putInt(getString(R.string.PREF_NUM_COL), gameConfig.getNUM_COL());
        editor.apply();
    }

    private void updateCurrentBoardSize() {
        currentBoardSize = (TextView) findViewById(R.id.currBoardSize);
        String text = getString(R.string.current_board_size);
        currentBoardSize.setText( text + gameConfig.getNUM_ROW() + " x " + gameConfig.getNUM_COL());
    }
    private void updateCurrentNumMines() {
        currentNumMines = (TextView) findViewById(R.id.currentNumMines);
        String text = getString(R.string.current_mines);
        currentNumMines.setText(text + gameConfig.getNUM_MINES());
    }

    private void setOkButton() {
        ok_button = (Button) findViewById(R.id.ok_button);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfig();
                finish();
            }
        });
    }


    private void setResetDataButton() {
        ok_button = (Button) findViewById(R.id.resetDataButton);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
            }
        });
    }

    // Resets total games played and user scores when reset button is pressed
    // Resets Data in shared preferences as well
    private void resetData(){
        gameConfig.setTotalGamesPlayed(0);
        Gson gson = new Gson();
        String scoreArray = gson.toJson(gameConfig.resetUserHighScores());

        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREF_NAME),MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.PREF_USER_SCORE),scoreArray);
        editor.putInt(getString(R.string.PREF_GAMES_PLAYED), gameConfig.getTotalGamesPlayed());
        editor.apply();
        Toast.makeText(getApplicationContext(),
                R.string.resetDataToastMessage,
                Toast.LENGTH_LONG).show();
    }



    // Sets a drop down menu for number of mines
    private void setSpinner4NumberofMines() {
        spinner_for_number_of_mines = (Spinner) findViewById (R.id.spinner_for_num_mines);
        spinner_for_number_of_mines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1: gameConfig.setNUM_MINES(6);    break;
                    case 2: gameConfig.setNUM_MINES(10);   break;
                    case 3: gameConfig.setNUM_MINES(15);   break;
                    case 4: gameConfig.setNUM_MINES(20);   break;
                    default:                                            break;
                }
                updateCurrentNumMines();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // Sets a drop down menu for Board size
    private void setSpinner4BoardSize() {
        spinner_for_boardSize = (Spinner) findViewById(R.id.spinner_for_board_size);
        spinner_for_boardSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: break;
                    case 1: gameConfig.set_boardSize(4,6);     break;
                    case 2: gameConfig.set_boardSize(5,10);    break;
                    case 3: gameConfig.set_boardSize(6,15);    break;
                    default:                                                break;
                }
                updateCurrentBoardSize();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


}

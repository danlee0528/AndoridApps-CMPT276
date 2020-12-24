package com.example.daniellee.myapplication;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Arrays;

public class PlayActivity extends AppCompatActivity {

    private final GameConfiguration gameConfig= GameConfiguration.getInstance();
    private final int NUM_ROWS = gameConfig.getNUM_ROW();
    private final int NUM_COLS = gameConfig.getNUM_COL();
    private final int NUM_MINES = gameConfig.getNUM_MINES();
    private final Button[][] buttons = new Button[NUM_ROWS][NUM_COLS];
    private final Boolean[][] isClicked = new Boolean[NUM_ROWS][NUM_COLS];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

            // Initializing Boolean Array
            for(int i=0; i<NUM_ROWS;i++)    {
                Arrays.fill(isClicked[i],Boolean.FALSE);
            }

            populateButtons();
            gameConfig.InitializeBoard();
            updateScansMines(); // Updates the text of Mine and Scan text views;
            setHiScoreGamesPlayed(); // Sets up HiScore and Played Text View
    }

    private void setHiScoreGamesPlayed() {
        TextView tv = (TextView) findViewById(R.id.hiScoreText);
        TextView tvGames = (TextView) findViewById(R.id.totalGamesPlayedTV);
        String hiScore = tv.getText().toString() + " " + gameConfig.getUserHiScore();
        String totalGames  = tvGames.getText().toString() + " " + gameConfig.getTotalGamesPlayed();
        tv.setText(hiScore);
        tvGames.setText(totalGames);
    }

    private void updateScansMines() {
        TextView tvm = (TextView) findViewById(R.id.minesFound);
        TextView tvs = (TextView) findViewById(R.id.scansUsed);

        tvm.setText(getString(R.string.of_mines_discovered) +
                gameConfig.getMinesFound() +  "/" + NUM_MINES);
        tvs.setText(getString(R.string.of_scans_used) +
                gameConfig.getScans());
    }

    private void populateButtons() {
        TableLayout table = (TableLayout) findViewById(R.id.tableForButtons);

        for (int row = 0; row < NUM_ROWS; row++){
            TableRow tablerow = new TableRow(this);
            tablerow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tablerow);

            for (int col = 0; col < NUM_COLS; col++){
                final int FINAL_COL = col;
                final int FINAL_ROW = row;
                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));

                // Make text not clip on small buttons
                button.setPadding(0,0,0,0);

                button.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_ROW, FINAL_COL);
                    }


                });

                tablerow.addView(button);
                buttons[row][col] = button;
            } // closing for col
        } // closing for row
    } // closing for populate()

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void gridButtonClicked(int row, int col) {
        Button button = buttons[row][col];
        // Used to trigger a mine scan
        // Returns if the button is already scanning the board
        if (isClicked[row][col] == Boolean.TRUE){
            if (gameConfig.isMine(row,col) && button.getText() == "") {
                button.setText("" + gameConfig.minesInRowCol(row, col));
                gameConfig.incrementScans();
                updateScansMines();// Updates TextViews
            }
            return;
        }
        isClicked[row][col] = Boolean.TRUE;
        //Lock Button Sizes;
        lockButtonSizes();
        // Scale image to button;
        // Only works in JellyBean;
        // If the user finds a mine
        if (gameConfig.foundMine(row,col)) {
            MediaPlayer mineSound = MediaPlayer.create(this, R.raw.for_mine);
            mineSound.start();
            int newWidth = button.getWidth();
            int newHeight = button.getHeight();
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mine_image);
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
            Resources resource = getResources();
            button.setBackground(new BitmapDrawable(resource, scaleBitmap));
            button.setText("");
            updateButtons(row, col); // Updating Button Text
            checkIfLastMine(); // Function checks if the User found the last mine
        }

        updateScansMines();// Updates TextViews after every move
        // Change text on button;
        if(!gameConfig.isMine(row, col)) {
            button.setText("" + gameConfig.getBoardElement(row, col));
            MediaPlayer scanSound = MediaPlayer.create(this, R.raw.for_scan);
            scanSound.start();
        }
    }

    // Used to end the game when user finds the last mine
    // Also generates a Dialog which shows the users score, the hiScore
    // and the total number of games played
    private void checkIfLastMine() {
        if (gameConfig.getGameFinshed()) {
            // Call the Dialog
            saveScoreNumGames();
            FragmentManager manager = getSupportFragmentManager();
            victoryFragment dialog = new victoryFragment();

            dialog.show(manager, "VictoryDialog");
            Toast.makeText(getApplicationContext(), R.string.winToastMessage,Toast.LENGTH_SHORT).show();

        }
    }

    // Used to save the total games played and the HiScores of the user
    // Called at the end when user finds the last mine
    private void saveScoreNumGames() {
        SharedPreferences prefs= getSharedPreferences(getString(R.string.PREF_NAME),MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // Need gson to store array of integers
        Gson gson= new Gson();
        String scoreArray = gson.toJson(gameConfig.getUserHiScores());
        editor.putString(getString(R.string.PREF_USER_SCORE), scoreArray);
        editor.putInt(getString(R.string.PREF_GAMES_PLAYED), gameConfig.getTotalGamesPlayed());
        editor.apply();

    }

    private void lockButtonSizes() {

        for (int row =0; row < NUM_ROWS; row++){
            for (int col = 0; col < NUM_COLS; col++){
                Button button = buttons[row][col];

                int width = button.getWidth();
                button.setWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setHeight(height);
                button.setMaxHeight(height);
            }
        }

    }

    // Updates the text of all buttons that have been clicked
    private void updateButtons(int row, int col) {
        for (int i = 0; i < NUM_COLS; i++) {
            updateButtonText(row, i);

        }
        for (int i = 0; i < NUM_ROWS; i++) {
            updateButtonText(i, col);
        }
    }

    private void updateButtonText(int row, int col)  {

        Button btn = buttons[row][col];

        if(isClicked[row][col]) {
            if(!gameConfig.isMine(row,col))    {
                btn.setText("" + gameConfig.getBoardElement(row,col));
            }
            else    {
                if(btn.getText() != "") {
                    btn.setText("" + gameConfig.minesInRowCol(row,col));
                }
            }
        }
    }

}

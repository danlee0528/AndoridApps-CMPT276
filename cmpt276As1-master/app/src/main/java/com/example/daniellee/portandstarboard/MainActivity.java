package com.example.daniellee.portandstarboard;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Game daGame = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the bottom left button to be PORT
        setUp_Button(R.id.left_bot_btn, Game.Side.PORT);
        //Set the bottom right button to be STARBOARD
        setUp_Button(R.id.right_bot_btn, Game.Side.STARBOARD);
        //
        updateUI();


        // Wire up the buttons to do stuff
        // ...get the buttons
        Button left_btn = (Button) findViewById(R.id.left_btn);
        Button right_btn = (Button) findViewById(R.id.right_btn);

        // ... do some stuff when the user clicks top left button
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyApp", "Port (left) is red");
                Toast.makeText(getApplicationContext(), "Port (left) is red", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        // ... do some stuff when the user clicks top right button
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyApp", "Starboard (right) is green");
                Toast.makeText(getApplicationContext(), "Starboard (right) is green", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /*PART 2*/

    // Wire buttons at the bottom
    // Refactoring
    private void setUp_Button(int buttonID, final Game.Side newAnswer) {
        //Create a button with a button_ID
        Button button = (Button) findViewById(buttonID);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Compare the user's input to the answer
                // Display "Correct" if the user's input matches the ChosenSideName(PORT or STARBOARD)
                if (daGame.checkIfCorrect(newAnswer)) {
                    Log.i("MyApp", "User guess of " + daGame.getChosenSideName() + " was Correct!");
                    Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT)
                            .show();
                // If not, display "Incorrect"
                } else {
                    Log.i("MyApp", "User guess of " + daGame.getChosenSideName() + " was Incorrect.");
                    Toast.makeText(getApplicationContext(), "Incorrect. :(", Toast.LENGTH_SHORT)
                            .show();
                }
                // now refresh the UI using daGame.getChosenSideName()
                daGame = new Game();
                updateUI();
            }
        });
    }

    // Update a new ChosenSideName, PORT or STARBOARD, and display the new name on the screen
    private void updateUI(){
        TextView textView = (TextView) findViewById(R.id.answer);
        String answers = daGame.getChosenSideName();

        textView.setText(answers);
    }

}
package com.example.daniellee.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class CalculateActivity extends AppCompatActivity {

    public static Pot temp_pot;
    TextView pot_TEXT;
    TextView weight_empty_TEXT;
    TextView weight_of_food_TEXT;
    TextView serving_weight_TEXT;
    EditText weight_with_food_EDIT;
    EditText number_of_servings_EDIT;
    String temp_pot_name;
    int temp_pot_weight;
    int weight_with_food_int;
    int weight_of_food_int;
    int number_of_servings_int;
    int serving_weight_int;
    boolean isFirstTimeEnter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        displayPotNameAndWeight();
        displayWeightWithFood();
        displayServingWeight();
        setBackButton();
    }

    private void displayPotNameAndWeight() {
        // Get data from UI
        pot_TEXT = (TextView) findViewById(R.id.pot_UI);
        weight_empty_TEXT = (TextView) findViewById(R.id.weight_empty_UI);
        weight_with_food_EDIT = (EditText) findViewById(R.id.weight_with_food_UI);
        weight_of_food_TEXT = (TextView) findViewById(R.id.weight_of_food_UI);
        number_of_servings_EDIT = (EditText) findViewById(R.id.number_of_servings_UI);
        serving_weight_TEXT = (TextView) findViewById(R.id.serving_weight_UI);

        // Get pot name and weight
        temp_pot_name = temp_pot.getName();
        temp_pot_weight = temp_pot.getWeightInG();

        // Display pot anme and weight
        pot_TEXT.setText( temp_pot_name);
        weight_empty_TEXT.setText( "" + temp_pot_weight );
    }

    // This does change the appearance of weight food in the CalculateActivity in real time
    private void displayWeightWithFood() {
        weight_with_food_EDIT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // if the user enters a digit for the first time and nothing was entered before
                if ( isFirstTimeEnter && s.length() > 0){
                        weight_with_food_int = Integer.parseInt(s.toString());
                        weight_of_food_int = weight_with_food_int - temp_pot_weight;
                        weight_of_food_TEXT.setText("" + weight_of_food_int);
                }
                else{
                    // if the user deletes the entire value and something was entered before
                    if ( s.length() == 0) {
                        isFirstTimeEnter = true; // start over
                        weight_of_food_TEXT.setText( s.toString() ); // display default text
                    }
                    // if the user deletes previous digit(s) while entering a value,
                    // but not all previous digits
                    else {
                        isFirstTimeEnter = false;
                    }
                }
            }
        });
    }

    // This does change the appearance of serving weight in the CalculateActivity in real time
    private void displayServingWeight() {
        number_of_servings_EDIT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // if the user enters a digit for the first time and nothing was entered before
                if ( isFirstTimeEnter && s.length() > 0) {
                    number_of_servings_int = Integer.parseInt(s.toString());
                    serving_weight_int = weight_of_food_int / number_of_servings_int;
                    serving_weight_TEXT.setText("" + serving_weight_int);
                }
                else{
                    // if the user deletes the entire value and something was entered before
                    if ( s.length() == 0) {
                        isFirstTimeEnter = true; // start over
                        serving_weight_TEXT.setText( s.toString() ); // display default text
                    }
                    // if the user deletes previous digit(s) while entering a value,
                    // but not all previous digits
                    else {
                        isFirstTimeEnter = false;
                    }
                }
            }
        });
    }

    private void setBackButton() {
        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    public static Intent makeLaunchIntent(Context context, Pot pot2use){
        temp_pot = pot2use;
        return new Intent(context, CalculateActivity.class);
    }


}
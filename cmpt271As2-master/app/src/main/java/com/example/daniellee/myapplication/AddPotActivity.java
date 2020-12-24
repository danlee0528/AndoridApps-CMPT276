package com.example.daniellee.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPotActivity extends AppCompatActivity {

    private String EXTRA_NAME = "POTNAME";
    private int EXTRA_WEIGHT = 100;        
    public static final String RESULT_POT_NAME = "UI Pot Name";
    public static final String RESULT_POT_WEIGHT = "UI Pot Weight";
    String UI_pot_name;
    String UI_pot_weight;
    EditText potNameEdit;
    EditText potWeightEdit;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pot);
        setupCANCELButton();
        setupOKButton();
    }

    private void setupOKButton() {
        Button okButton = (Button) findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract data from the UI
                potNameEdit = (EditText) findViewById(R.id.name_defined_by_user);
                potWeightEdit = (EditText) findViewById(R.id.weight_defined_by_user);
                UI_pot_name = potNameEdit.getText().toString();
                UI_pot_weight = potWeightEdit.getText().toString();

        /* INPUT ERROR CHECKING ALGORITHM*/
                intent = new Intent();

                // check if pot name begins with an alphabetical letter
                if (!((UI_pot_name.charAt(0) >= 'a' && UI_pot_name.charAt(0) <= 'z') ||
                        (UI_pot_name.charAt(0) >= 'A' && UI_pot_name.charAt(0) <= 'Z')) ||
                        UI_pot_name.length() < 0) {
                    Toast.makeText(AddPotActivity.this, "Pot name must begin with a letter", Toast.LENGTH_LONG).show();
                    setResult(Activity.RESULT_CANCELED, intent); // go back to previous step
                } else {
                    // If the user enters something for the pot weight
                    if (UI_pot_weight.length() > 0) {
                        // if pot weight is a positive value greater than 0 or is equal to 0
                        if ( (UI_pot_weight.charAt(0) >= '0' && UI_pot_weight.charAt(0) <= '9') || UI_pot_weight.charAt(0) == '+') {
                            intent.putExtra(RESULT_POT_WEIGHT, UI_pot_weight);
                            intent.putExtra(RESULT_POT_NAME, UI_pot_name);
                            setResult(Activity.RESULT_OK, intent); // pass the current step
                            finish();
                        }else {
                            setResult(Activity.RESULT_CANCELED, intent);
                            Toast.makeText(AddPotActivity.this, "Pot weight is needed. (be greater than or equal to 0)", Toast.LENGTH_LONG).show();
                        }
                    }
                    // If the user attempts to add a pot without a weight, display this error message
                    else{
                        setResult(Activity.RESULT_CANCELED, intent);
                        Toast.makeText(AddPotActivity.this, "Pot weight is needed.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void setupCANCELButton() {
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish(); // Kill the AddPotActivity
            }
        });
    }

    // This encapsulates the details of how data is stored
    // in the Intent to insdie this activity
    public static Pot getPotFromIntent(Intent data){
        Pot potAdded = new Pot ( data.getStringExtra(RESULT_POT_NAME), Integer.parseInt( data.getStringExtra(RESULT_POT_WEIGHT)));
        return potAdded;
    }
}

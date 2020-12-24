package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

/**
 * AddUtility saves name, emissions, gasType, amount, the numberOfPeople in the household and a
 * start and end date as input by the user.
 * Also handles editing utilities. Passes saved utilities via singleton class: UtilitySingleton
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.kenneyw.carbonfootprinttracker.model.Calculation;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.DateSingleton;
import com.cmpt276.kenneyw.carbonfootprinttracker.R;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.UtilitySingleton;

public class AddUtility extends AppCompatActivity {
    public static final int START_DATE_CHOOSE = 3;
    public static final int END_DATE_CHOOSE = 4;
    public static final String POS_TO_EDIT = "POS";
    String utility_type;
    EditText editAmount;
    EditText editNumPeople;
    EditText editNickname;
    RadioButton electricityButton;
    RadioButton naturalgasButton;
    String startDate;
    String endDate;
    int startDay;
    int endDay;
    int startMonth;
    int endMonth;
    int startYear;
    int endYear;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_utility);

        Intent i = getIntent();
        if (i.hasExtra("POS")) {
            pos = i.getIntExtra(POS_TO_EDIT, 0);
        } else {
            pos = 0;
        }

        setupButton();
        setupRadioButtons();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
        return true;
    }

    private void setupRadioButtons() {
        RadioGroup utility_radioButton = (RadioGroup) findViewById(R.id.radioUtility);
        utility_radioButton.clearCheck();
        utility_radioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedButton = (RadioButton) findViewById(checkedId);
                Toast.makeText(getApplicationContext(), selectedButton.getText() + " selected", Toast.LENGTH_SHORT).show();
                utility_type = selectedButton.getText().toString();
            }
        });
    }

    private void setupButton() {
        ImageButton ok_button = (ImageButton) findViewById(R.id.ok_button_add_utility);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromUser();
            }
        });

        Button startDate = (Button) findViewById(R.id.startdate_btn);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddUtility2EditDate = EditDate.makeIntent(AddUtility.this);
                startActivityForResult(AddUtility2EditDate, START_DATE_CHOOSE);
            }
        });
        Button endDate = (Button) findViewById(R.id.enddate_btn);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddUtility2EditDate = EditDate.makeIntent(AddUtility.this);
                startActivityForResult(AddUtility2EditDate, END_DATE_CHOOSE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case START_DATE_CHOOSE:
                if (resultCode == RESULT_OK) {
                    DateSingleton startdate = DateSingleton.getInstance();
                    startDate = startdate.getDateString(); // string;
                    startDay = startdate.getDay();
                    startMonth = startdate.getMonth(); // int;
                    startYear = startdate.getYear();
                    TextView start_date = (TextView) findViewById(R.id.startdate_text);
                    start_date.setText(startDate);
                }
                setupButton();
                break;
            case END_DATE_CHOOSE:
                if (resultCode == RESULT_OK) {
                    DateSingleton finalDate = DateSingleton.getInstance();
                    endDate = finalDate.getDateString(); // string;
                    endDay = finalDate.getDay();
                    endMonth = finalDate.getMonth(); // int;
                    endYear = finalDate.getYear();
                    TextView end_date = (TextView) findViewById(R.id.enddate_text);
                    end_date.setText(endDate);
                }
                setupButton();
                break;
        }
    }

    private void getDataFromUser() {
        // Save a utility
        editAmount = (EditText) findViewById(R.id.amount_text);
        editNumPeople = (EditText) findViewById(R.id.num_people_text);
        editNickname = (EditText) findViewById(R.id.utility_nick_name_text);
        electricityButton = (RadioButton) findViewById(R.id.radioElectricity);
        naturalgasButton = (RadioButton) findViewById(R.id.radioNaturalGas);

        // no inputs entered
        if (editAmount.getText().toString().equals("") && editNumPeople.getText().toString().equals("") &&
                editNickname.getText().toString().equals("") && (!electricityButton.isChecked()
                && !naturalgasButton.isChecked())) {
            Toast.makeText(getApplicationContext(), "No inputs entered", Toast.LENGTH_SHORT).show();
        }
        // something was entered
        else {

            if (editAmount.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Amount can't be left empty", Toast.LENGTH_SHORT).show();
            } else if (utility_type.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please select a utility type", Toast.LENGTH_SHORT).show();
            } else if (editNickname.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Name can't be left empty", Toast.LENGTH_SHORT).show();
            } else if (editNumPeople.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter a number of poeple", Toast.LENGTH_SHORT).show();
            } // if years are equal
            else if (startYear > endYear) {
                Toast.makeText(getApplicationContext(), "StartDate can't be greater than EndDate", Toast.LENGTH_SHORT).show();
            } else if (startYear <= endYear && startMonth > endMonth) {
                Toast.makeText(getApplicationContext(), "StartDate can't be greater than EndDate", Toast.LENGTH_SHORT).show();
            } else if (startYear <= endYear && startMonth == endMonth && startDay > endDay) {
                Toast.makeText(getApplicationContext(), "StartDate can't be greater than EndDate", Toast.LENGTH_SHORT).show();
            } else {

                //String name, String gasType, double amounts, int num_people, double emission, String startDate, String endDate
                UtilitySingleton new_utility = UtilitySingleton.getInstance();
                new_utility.setAmounts(Double.parseDouble(editAmount.getText().toString()));
                new_utility.setNum_poeople(Integer.parseInt(editNumPeople.getText().toString()));
                new_utility.setStartDate(startDate);
                new_utility.setEndDate(endDate);
                new_utility.setName(editNickname.getText().toString());

                Calculation calculation = new Calculation();
                if (utility_type.equals("Electricity")) {
                    new_utility.setEmission(calculation.calculate_CO2_Emission_of_Electricity
                            (Double.parseDouble(editAmount.getText().toString())));
                } else {
                    new_utility.setEmission(calculation.calculate_CO2_Emission_of_Natural_Gas
                            (Double.parseDouble(editAmount.getText().toString())));
                }

                new_utility.setGasType(utility_type);
                Log.i("AddUtility", "Singleton = " + new_utility.getName());
                Log.i("AddUtility", "Singleton = " + new_utility.getGasType());
                Log.i("AddUtility", "Singleton = " + new_utility.getEmission());
                Intent i = new Intent();
                i.putExtra(POS_TO_EDIT, pos);
                setResult(RESULT_OK, i);
                finish();
            }
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddUtility.class);
    }
}

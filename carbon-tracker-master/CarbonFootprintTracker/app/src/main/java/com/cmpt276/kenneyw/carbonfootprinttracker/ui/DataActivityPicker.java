package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

/**
 * Provides a screen to either choose a date to view activity input by user for that date,
 * or activity for the last month,
 * or activity for the last year.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;

import java.util.Calendar;
import java.util.Locale;

public class DataActivityPicker extends AppCompatActivity {

    public static final String DATE_IN_STR = "dateInStr";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    String date_in_str;
    int Year;
    int Month;
    int Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_data);
        setupButtons();
        setupDatePicker();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
        return true;
    }


    private void setupDatePicker() {
        DatePicker dp = (DatePicker) findViewById(R.id.datePicker2);
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), onDateChanged);
    }

    private void setupButtons() {
        Button lastmonth = (Button) findViewById(R.id.last_28_days_data);
        lastmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lastmonthintent = LastMonthActivity.makeIntent(DataActivityPicker.this);
                startActivity(lastmonthintent);
            }
        });
        Button lastyear = (Button) findViewById(R.id.last_365_days_data);
        lastyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lastyearintent = LastYearActivity.makeIntent(DataActivityPicker.this);
                startActivity(lastyearintent);
            }
        });
    }

    DatePicker.OnDateChangedListener onDateChanged = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date_in_str = monthOfYear + 1 + "/" + dayOfMonth + "/" + year;
            Year = year;
            Month = monthOfYear + 1;
            Day = dayOfMonth;
            Intent i = DailyActivity.makeIntent(DataActivityPicker.this);
            i.putExtra(DATE_IN_STR, date_in_str);
            i.putExtra(DAY, Day);
            i.putExtra(MONTH, Month);
            i.putExtra(YEAR, Year);
            startActivity(i);
        }
    };

    public static Intent makeIntent(Context context) {
        return new Intent(context, DataActivityPicker.class);
    }

}

package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

/**
 * Shows the Carbon Footprint per day when selected in the datepicker on the carbon footprint screen
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.Journey;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.JourneyCollection;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.UtilitiesCollection;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.Utility;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyActivity extends AppCompatActivity {

    String date_in_str;
    int Year;
    int Month;
    int Day;
    JourneyCollection journeys = new JourneyCollection();
    UtilitiesCollection utilities = new UtilitiesCollection();

    public static final String DATE_IN_STR = "dateInStr";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";

    private static final String TAG = "CarbonFootprintTracker";
    private static final String SHAREDPREF_SET_UTIL = "CarbonFootprintTrackerUtilities";
    private static final String SHAREDPREF_ITEM_AMOUNTOFUTILITIES = "AmountOfUtilities";
    private static final String UTILNAME = "name";
    private static final String GASTYPE = "gasType";
    private static final String AMOUNT = "amount";
    private static final String NUMPEOPLE = "numofPeople";
    private static final String STARTDATE = "startDate";
    private static final String ENDDATE = "endDate";
    private static final String EMISSION = "emission";

    private static final String SHAREDPREF_SET_JOURNEY = "CarbonFootprintTrackerJournies";
    private static final String SHAREDPREF_ITEM_AMOUNTOFJOURNEYS = "AmountOfJourneys";
    public static final String NAME = "name";
    public static final String ROUTENAME = "routeName";
    public static final String CITY = "city";
    public static final String HIGHWAY = "highway";
    public static final String GASTYPE_JOURNEY = "gasType";
    public static final String MPGCITY = "mpgCity";
    public static final String MPGHIGHWAY = "mpgHighway";
    public static final String DATESTRING = "dateString";
    public static final String LITERENGINE = "literEngine";
    public static final String TOTALEMISSIONS = "totalEmissions";
    public static final String BUS = "bus";
    public static final String BIKE = "bike";
    public static final String SKYTRAIN = "skytrain";
    public static final String WALK = "walk";
    public static final String ICONID = "IconID";
    PieChart chart;
    List<PieEntry> entries;
    PieDataSet dataSet;
    PieData data;

    int utilityAmt;
    int journeyAmt;
    int entriesSize = 0;

    public static final String SETTING = "CarbonFootprintTrackerSettings";
    public static final String TREESETTING = "TreeSetting";
    boolean setting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        Intent i = getIntent();
        Day = i.getIntExtra(DAY, 1);
        Month = i.getIntExtra(MONTH, 0);
        Year = i.getIntExtra(YEAR, 2017);
        date_in_str = i.getStringExtra(DATE_IN_STR);
        utilities = loadUtilities();
        journeys = loadJourneys();

        getSetting();
        setUpArrays();
        setUpPieChart();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
        return true;
    }

    private void getSetting() {
        SharedPreferences pref = getSharedPreferences(SETTING, MODE_PRIVATE);
        setting = pref.getBoolean(TREESETTING, false);
    }

    private void setUpArrays() {
        entries = new ArrayList<>();
        for (int i = 0; i < journeyAmt; i++) {
            if (journeys.getJourney(i).getDateString().equals(date_in_str)) {
                entriesSize++;
                if (setting) {
                    entries.add(new PieEntry((float) journeys.getJourney(i).getTotalEmissions() * 2.8f,
                            journeys.getJourney(i).getName()));
                } else {
                    entries.add(new PieEntry((float) journeys.getJourney(i).getTotalEmissions(),
                            journeys.getJourney(i).getName()));
                }
            }
        }
        for (int i = 0; i < utilityAmt; i++) {
            if (isBetween(date_in_str, utilities.getUtility(i).getStartDate(), utilities.getUtility(i).getEndDate())) {
                entriesSize++;
                String[] first = utilities.getUtility(i).getStartDate().split("/");
                String[] last = utilities.getUtility(i).getEndDate().split("/");
                long numDays = countDays(first, last);
                if (setting) {
                    entries.add(new PieEntry((float) (
                            utilities.getUtility(i).getEmission() / utilities.getUtility(i).getNumofPeople() / numDays * 2.8f),
                            utilities.getUtility(i).getName()));
                } else {
                    entries.add(new PieEntry((float) (
                            utilities.getUtility(i).getEmission() / utilities.getUtility(i).getNumofPeople() / numDays),
                            utilities.getUtility(i).getName()));
                }
            }
        }
    }

    private void setUpPieChart() {
        dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueLineColor(Color.TRANSPARENT);
        dataSet.setSliceSpace(5.0f);
        dataSet.setValueTextSize(15);
        // set the data
        data = new PieData(dataSet);
        data.setValueTextSize(20);
        data.setValueTextColor(Color.BLACK);
        // Chart config
        chart = (PieChart) findViewById(R.id.chart1);
        chart.setData(data);
        chart.animateY(2000);
        chart.setCenterText("SUMMARY\nof\nCO2 EMISSION FOR:\n" + date_in_str);
        chart.setCenterTextSize(20);
        chart.setCenterTextColor(Color.DKGRAY);
        chart.setDescription(null);
        chart.getLegend().setEnabled(true);
        chart.setVisibility(View.VISIBLE);
        chart.invalidate();
    }

    private long countDays(String[] first, String[] last) {
        Date dateOne = new Date(Integer.parseInt(first[1]), Integer.parseInt(first[2]), Integer.parseInt(first[0]));
        Date dateTwo = new Date(Integer.parseInt(last[1]), Integer.parseInt(last[2]), Integer.parseInt(last[0]));
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;
        return delta;
    }

    //check if date is between 2 dates (string); returns True / False
    private boolean isBetween(String date, String firstDate, String lastDate) {
        String[] checkdate = date.split("/");
        String[] first = firstDate.split("/");
        String[] last = lastDate.split("/");
        return (
                //year
                Integer.parseInt(checkdate[2]) <= Integer.parseInt(last[2])
                        && Integer.parseInt(checkdate[2]) >= Integer.parseInt(first[2])
                        //month
                        && Integer.parseInt(checkdate[0]) <= Integer.parseInt(last[0])
                        && Integer.parseInt(checkdate[0]) >= Integer.parseInt(first[0])
                        //day
                        && Integer.parseInt(checkdate[1]) <= Integer.parseInt(last[1])
                        && Integer.parseInt(checkdate[1]) >= Integer.parseInt(first[1])
        );
    }

    private UtilitiesCollection loadUtilities() {
        UtilitiesCollection utils = new UtilitiesCollection();
        SharedPreferences pref = getSharedPreferences(SHAREDPREF_SET_UTIL, MODE_PRIVATE);
        utilityAmt = pref.getInt(SHAREDPREF_ITEM_AMOUNTOFUTILITIES, 0);
        for (int i = 0; i < utilityAmt; i++) {
            Utility newUtil = new Utility(pref.getString(i + UTILNAME, ""),
                    pref.getString(i + GASTYPE, ""), Double.longBitsToDouble(pref.getLong(i + AMOUNT, 0)), pref.getInt(i + NUMPEOPLE, 0),
                    Double.longBitsToDouble(pref.getLong(i + EMISSION, 0)), pref.getString(i + STARTDATE, ""), pref.getString(i + ENDDATE, ""));
            utils.addUtility(newUtil);
        }
        return utils;
    }

    public JourneyCollection loadJourneys() {
        JourneyCollection temp_journeys = new JourneyCollection();
        SharedPreferences pref = getSharedPreferences(SHAREDPREF_SET_JOURNEY, MODE_PRIVATE);
        journeyAmt = pref.getInt(SHAREDPREF_ITEM_AMOUNTOFJOURNEYS, 0);
        for (int i = 0; i < journeyAmt; i++) {
            //Date date=new Date(pref.getLong(i+DATEOFTRAVEL,0));
            Journey j = new Journey(
                    pref.getString(i + ROUTENAME, ""),
                    Double.longBitsToDouble(pref.getLong(i + CITY, 0)),
                    Double.longBitsToDouble(pref.getLong(i + HIGHWAY, 0)),
                    pref.getString(i + NAME, ""),
                    pref.getString(i + GASTYPE_JOURNEY, ""),
                    Double.longBitsToDouble(pref.getLong(i + MPGCITY, 0)),
                    Double.longBitsToDouble(pref.getLong(i + MPGHIGHWAY, 0)),
                    Double.longBitsToDouble(pref.getLong(i + LITERENGINE, 0)),
                    pref.getString(i + DATESTRING, ""),
                    pref.getBoolean(i + BUS, false),
                    pref.getBoolean(i + BIKE, false),
                    pref.getBoolean(i + SKYTRAIN, false),
                    pref.getBoolean(i + WALK, false),
                    pref.getInt(i + ICONID, 0));
            temp_journeys.addJourney(j);
        }
        return temp_journeys;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, DailyActivity.class);
    }
}

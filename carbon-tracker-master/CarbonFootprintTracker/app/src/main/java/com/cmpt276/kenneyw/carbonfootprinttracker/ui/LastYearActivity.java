package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

/**
 * Activity for checking User's previous year data from Journeys and Utilities.
 * Gets today's date, get's the date for exactly one month previous, and the number of days in between
 * i.e. March 27th to Feb 27th -> 31 days
 * Goes through all journeys, sees if the date of journey is between these two dates, if so adds the car name or route name
 * to the names arraylist. adds the corresponding emission data for related name in the same index in the ems arraylist
 * <p>
 * Goes through all utilities, covers 4 possibilities of start and end dates being within and outside of the two above dates.
 * if Utility is not in range, does not take data for pie chart
 * <p>
 * For Line Graph, takes all emissions of one day, utilities (daily) emission and journey emission
 * if no utility emission for day, stores average utility emission of all utilities
 * <p>
 * Does the above procedure for last 12 months.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.Journey;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.JourneyCollection;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.UtilitiesCollection;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.Utility;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LastYearActivity extends AppCompatActivity {
    //calculated as: 30% of 2005 Daily CO2 Emission in Canada divided by Population in 2005
    //<--Canada goals - 30% of 2005 level emissions-->
    public static final double PARISACCORDCO2PERCAPITA = 19.04;
    public static final String AVERAGE_CHECKER = "average-------";

    String date_in_str;
    int Year;
    int Month;
    int Day;
    String prev_date_in_str;
    int prev_Year;
    int prev_Month;
    int prev_Day;

    JourneyCollection journeys = new JourneyCollection();
    UtilitiesCollection utilities = new UtilitiesCollection();

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

    //for pie chart
    PieChart chart;
    PieDataSet dataSet;
    PieData data;
    int entrySize = 0;
    List<PieEntry> entriesForAllMonth = new ArrayList<>();
    ArrayList<Float> ems = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();

    //for line chart
    LineChart lineChart;
    ArrayList<Entry> userAxis = new ArrayList<>();
    ArrayList<Entry> averageCanadianAxis = new ArrayList<>();
    ArrayList<Entry> ParisAccordAxis = new ArrayList<>();

    //data storage
    float utilityAverage;
    int utilityAmt;
    int journeyAmt;
    double[] totalems;

    //check for route / car mode
    boolean checkOrganization = false;

    public static final String SETTING = "CarbonFootprintTrackerSettings";
    public static final String TREESETTING = "TreeSetting";
    boolean setting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_year);

        utilities = loadUtilities();
        journeys = loadJourneys();
        whatDayIsIt();
        whatDayIsThirtyDaysPrevious(0);
        getSetting();
        setUpArrays();
        setUpPieChart();
        setUpLineChart();
        setUpButtons();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getSetting() {
        SharedPreferences pref = getSharedPreferences(SETTING, MODE_PRIVATE);
        setting = pref.getBoolean(TREESETTING, false);
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

    //car mode, groups emissions with car names; utilities as normal
    private void setUpArrays() {

        entriesForAllMonth = new ArrayList<>();
        getUtilityAverage();
        whatDayIsIt();
        whatDayIsThirtyDaysPrevious(0);
        ems = new ArrayList<>();
        names = new ArrayList<>();
        entrySize = 0;
        totalems = new double[12];

        for (int counter = 0; counter < 12; counter++) {

            //number of months to loop for
            String[] firstjourn = prev_date_in_str.split("/");
            String[] lastjourn = date_in_str.split("/");
            long datesbetween = countDays(firstjourn, lastjourn);

            //array lists for names and emissions, same index has emission data for same car name
            //(transportation mode included)

            //loop for amount of journeys
            for (int i = 0; i < journeyAmt; i++) {
                //if date is in current month
                if (isBetween(journeys.getJourney(i).getDateString(), prev_date_in_str, date_in_str)) {
                    String nameOfJourney = journeys.getJourney(i).getName();
                    totalems[counter] += (float) journeys.getJourney(i).getTotalEmissions();
                    //if car is known in names arraylist
                    if (names.contains(nameOfJourney)) {
                        //add emission data on that index
                        for (int j = 0; j < entrySize; j++) {
                            if (nameOfJourney.equals(names.get(j))) {
                                float em = ems.get(j);
                                String na = names.get(j);
                                names.remove(j);
                                ems.remove(j);
                                names.add(na);
                                ems.add(em + (float) journeys.getJourney(i).getTotalEmissions());
                                //ems.add(j,(float)journeys.getJourney(i).getTotalEmissions());
                            }
                        }
                    } else {
                        //else increment size, make new index to store that car
                        entrySize++;
                        names.add(journeys.getJourney(i).getName());
                        //add emission data on that index
                        for (int j = 0; j < entrySize; j++) {
                            if (nameOfJourney.equals(names.get(j))) {
                                ems.add(j, (float) journeys.getJourney(i).getTotalEmissions());
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < utilityAmt; i++) {

                if (isBetween(date_in_str, utilities.getUtility(i).getStartDate(), utilities.getUtility(i).getEndDate())
                        ||
                        isBetween(prev_date_in_str, utilities.getUtility(i).getStartDate(), utilities.getUtility(i).getEndDate())) {

                    if (isBefore(utilities.getUtility(i).getEndDate(), date_in_str)
                            && isBefore(utilities.getUtility(i).getStartDate(), prev_date_in_str)) {

                        String[] firstU = utilities.getUtility(i).getStartDate().split("/");
                        String[] lastU = utilities.getUtility(i).getEndDate().split("/");
                        long numDaysForUtility = countDays(firstU, lastU);

                        String[] first = prev_date_in_str.split("/");
                        String[] last = utilities.getUtility(i).getEndDate().split("/");
                        long numDays = countDays(first, last);

                        entrySize++;
                        names.add(utilities.getUtility(i).getName());
                        ems.add((float) utilities.getUtility(i).getEmission() /
                                utilities.getUtility(i).getNumofPeople() /
                                numDaysForUtility * numDays);
                        totalems[counter] += (float) utilities.getUtility(i).getEmission() /
                                utilities.getUtility(i).getNumofPeople() /
                                numDaysForUtility * numDays;
                    } else if (isBefore(prev_date_in_str, utilities.getUtility(i).getStartDate())
                            && isBefore(date_in_str, utilities.getUtility(i).getEndDate())) {

                        String[] firstU = utilities.getUtility(i).getStartDate().split("/");
                        String[] lastU = utilities.getUtility(i).getEndDate().split("/");
                        long numDaysForUtility = countDays(firstU, lastU);

                        String[] first = utilities.getUtility(i).getStartDate().split("/");
                        String[] last = date_in_str.split("/");
                        long numDays = countDays(first, last);

                        entrySize++;
                        names.add(utilities.getUtility(i).getName());
                        ems.add((float) utilities.getUtility(i).getEmission() /
                                utilities.getUtility(i).getNumofPeople() /
                                numDaysForUtility * numDays);
                        totalems[counter] += (float) utilities.getUtility(i).getEmission() /
                                utilities.getUtility(i).getNumofPeople() /
                                numDaysForUtility * numDays;
                    } else {
                        String[] firstU = utilities.getUtility(i).getStartDate().split("/");
                        String[] lastU = utilities.getUtility(i).getEndDate().split("/");
                        long numDaysForUtility = countDays(firstU, lastU);

                        entrySize++;
                        names.add(utilities.getUtility(i).getName());
                        ems.add((float) utilities.getUtility(i).getEmission() /
                                utilities.getUtility(i).getNumofPeople() /
                                numDaysForUtility * datesbetween);
                        totalems[counter] += (float) utilities.getUtility(i).getEmission() /
                                utilities.getUtility(i).getNumofPeople() /
                                numDaysForUtility * datesbetween;
                    }
                } else if (isBetween(utilities.getUtility(i).getStartDate(), prev_date_in_str, date_in_str)
                        &&
                        isBetween(utilities.getUtility(i).getEndDate(), prev_date_in_str, date_in_str)) {

                    entrySize++;
                    names.add(utilities.getUtility(i).getName());
                    ems.add((float) utilities.getUtility(i).getEmission() /
                            utilities.getUtility(i).getNumofPeople());
                    totalems[counter] += (float) utilities.getUtility(i).getEmission() /
                            utilities.getUtility(i).getNumofPeople();
                } else {
                    //take avg of other dates... how?
                    entrySize++;
                    names.add(AVERAGE_CHECKER);
                    ems.add(utilityAverage);
                    totalems[counter] += utilityAverage;
                }
            }

            if (setting) {
                totalems[counter] = totalems[counter] * 2.8f;
            }
            //entries.add(new Entry((float)totalems,"Month "+(12-counter+1)));
            date_in_str = prev_date_in_str;
            whatDayIsThirtyDaysPrevious(counter + 1);

        }
        if (setting) {
            for (int i = 0; i < entrySize; i++) {
                ems.set(i, ems.get(i) * 2.8f);
            }
        }

        //For PieChart: All Months, Segregated Data according to transportation mode / name, and Utility name
        for (int i = 0; i < entrySize; i++) {
            if (!names.get(i).equals(AVERAGE_CHECKER)) {
                entriesForAllMonth.add(new PieEntry(ems.get(i), names.get(i)));
            }
        }
    }

    //route mode, groups emissions with route names; utilities as normal
    private void setUpArraysRouteWise() {

        entriesForAllMonth = new ArrayList<>();
        getUtilityAverage();
        whatDayIsIt();
        whatDayIsThirtyDaysPrevious(0);
        ems = new ArrayList<>();
        names = new ArrayList<>();
        entrySize = 0;
        totalems = new double[12];

        for (int counter = 0; counter < 12; counter++) {

            //number of months to loop for
            String[] firstjourn = prev_date_in_str.split("/");
            String[] lastjourn = date_in_str.split("/");
            long datesbetween = countDays(firstjourn, lastjourn);

            //array lists for names and emissions, same index has emission data for same car name
            //(transportation mode included)

            //loop for amount of journeys
            for (int i = 0; i < journeyAmt; i++) {
                //if date is in current month
                if (isBetween(journeys.getJourney(i).getDateString(), prev_date_in_str, date_in_str)) {
                    String nameOfJourney = journeys.getJourney(i).getRouteName();
                    totalems[counter] += (float) journeys.getJourney(i).getTotalEmissions();
                    //if car is known in names arraylist
                    if (names.contains(nameOfJourney)) {
                        //add emission data on that index
                        for (int j = 0; j < entrySize; j++) {
                            if (nameOfJourney.equals(names.get(j))) {
                                float em = ems.get(j);
                                String na = names.get(j);
                                names.remove(j);
                                ems.remove(j);
                                names.add(na);
                                ems.add(em + (float) journeys.getJourney(i).getTotalEmissions());
                                //ems.add(j,(float)journeys.getJourney(i).getTotalEmissions());
                            }
                        }
                    } else {
                        //else increment size, make new index to store that car
                        entrySize++;
                        names.add(journeys.getJourney(i).getRouteName());
                        //add emission data on that index
                        for (int j = 0; j < entrySize; j++) {
                            if (nameOfJourney.equals(names.get(j))) {
                                ems.add(j, (float) journeys.getJourney(i).getTotalEmissions());
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < utilityAmt; i++) {

                if (isBetween(date_in_str, utilities.getUtility(i).getStartDate(), utilities.getUtility(i).getEndDate())
                        ||
                        isBetween(prev_date_in_str, utilities.getUtility(i).getStartDate(), utilities.getUtility(i).getEndDate())) {

                    if (isBefore(utilities.getUtility(i).getEndDate(), date_in_str)
                            && isBefore(utilities.getUtility(i).getStartDate(), prev_date_in_str)) {

                        String[] firstU = utilities.getUtility(i).getStartDate().split("/");
                        String[] lastU = utilities.getUtility(i).getEndDate().split("/");
                        long numDaysForUtility = countDays(firstU, lastU);

                        String[] first = prev_date_in_str.split("/");
                        String[] last = utilities.getUtility(i).getEndDate().split("/");
                        long numDays = countDays(first, last);

                        entrySize++;
                        names.add(utilities.getUtility(i).getName());
                        ems.add((float) utilities.getUtility(i).getEmission() /
                                utilities.getUtility(i).getNumofPeople() /
                                numDaysForUtility * numDays);
                        totalems[counter] += (float) utilities.getUtility(i).getEmission() /
                                utilities.getUtility(i).getNumofPeople() /
                                numDaysForUtility * numDays;
                    } else if (isBefore(prev_date_in_str, utilities.getUtility(i).getStartDate())
                            && isBefore(date_in_str, utilities.getUtility(i).getEndDate())) {

                        String[] firstU = utilities.getUtility(i).getStartDate().split("/");
                        String[] lastU = utilities.getUtility(i).getEndDate().split("/");
                        long numDaysForUtility = countDays(firstU, lastU);

                        String[] first = utilities.getUtility(i).getStartDate().split("/");
                        String[] last = date_in_str.split("/");
                        long numDays = countDays(first, last);

                        entrySize++;
                        names.add(utilities.getUtility(i).getName());
                        ems.add((float) utilities.getUtility(i).getEmission() /
                                utilities.getUtility(i).getNumofPeople() /
                                numDaysForUtility * numDays);
                        totalems[counter] += (float) utilities.getUtility(i).getEmission() /
                                utilities.getUtility(i).getNumofPeople() /
                                numDaysForUtility * numDays;
                    } else {
                        String[] firstU = utilities.getUtility(i).getStartDate().split("/");
                        String[] lastU = utilities.getUtility(i).getEndDate().split("/");
                        long numDaysForUtility = countDays(firstU, lastU);

                        entrySize++;
                        names.add(utilities.getUtility(i).getName());
                        ems.add((float) utilities.getUtility(i).getEmission() /
                                utilities.getUtility(i).getNumofPeople() /
                                numDaysForUtility * datesbetween);
                        totalems[counter] += (float) utilities.getUtility(i).getEmission() /
                                utilities.getUtility(i).getNumofPeople() /
                                numDaysForUtility * datesbetween;
                    }
                } else if (isBetween(utilities.getUtility(i).getStartDate(), prev_date_in_str, date_in_str)
                        &&
                        isBetween(utilities.getUtility(i).getEndDate(), prev_date_in_str, date_in_str)) {

                    entrySize++;
                    names.add(utilities.getUtility(i).getName());
                    ems.add((float) utilities.getUtility(i).getEmission() /
                            utilities.getUtility(i).getNumofPeople());
                    totalems[counter] += (float) utilities.getUtility(i).getEmission() /
                            utilities.getUtility(i).getNumofPeople();
                } else {
                    //take avg of other dates... how?
                    entrySize++;
                    names.add(AVERAGE_CHECKER);
                    ems.add(utilityAverage);
                    totalems[counter] += utilityAverage;
                }
            }

            if (setting) {
                totalems[counter] = totalems[counter] * 2.8f;
            }
            //entries.add(new Entry((float)totalems,"Month "+(12-counter+1)));
            date_in_str = prev_date_in_str;
            whatDayIsThirtyDaysPrevious(counter + 1);

        }
        if (setting) {
            for (int i = 0; i < entrySize; i++) {
                ems.set(i, ems.get(i) * 2.8f);
            }
        }

        //For PieChart: All Months, Segregated Data according to transportation mode / name, and Utility name
        for (int i = 0; i < entrySize; i++) {
            if (!names.get(i).equals(AVERAGE_CHECKER)) {
                entriesForAllMonth.add(new PieEntry(ems.get(i), names.get(i)));
            }
        }
    }

    private void setUpLineChart() {
        lineChart = (LineChart) findViewById(R.id.lineChartY);
        userAxis = new ArrayList<>();
        ParisAccordAxis = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            userAxis.add(new Entry(i, (float) totalems[11 - i]));
        }
        if (setting) {
            for (int i = 0; i < 12; i++) {
                ParisAccordAxis.add(new Entry(i, (float) PARISACCORDCO2PERCAPITA * 2.8f));
            }
        } else {
            for (int i = 0; i < 12; i++) {
                ParisAccordAxis.add(new Entry(i, (float) PARISACCORDCO2PERCAPITA));
            }
        }

        ArrayList<ILineDataSet> lines = new ArrayList<>();
        LineDataSet lds1 = new LineDataSet(userAxis, "User Data");
        lds1.setValueTextColor(Color.WHITE);
        lds1.setColor(Color.WHITE);
        LineDataSet lds2 = new LineDataSet(ParisAccordAxis, "Paris Accord Goal");
        lds2.setValueTextColor(Color.GREEN);
        lds2.setColor(Color.GREEN);
        lines.add(lds1);
        lines.add(lds2);

        lineChart.setData(new LineData(lines));
        lineChart.setVisibility(View.INVISIBLE);
        lineChart.animateX(2000);
        lineChart.animateY(2000);
        lineChart.setHorizontalScrollBarEnabled(true);
        lineChart.invalidate();
    }

    private void setUpPieChart() {
        dataSet = new PieDataSet(entriesForAllMonth, "");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setValueLineColor(Color.TRANSPARENT);
        dataSet.setSliceSpace(1.0f);
        dataSet.setValueTextSize(15);
        // set the data
        data = new PieData(dataSet);
        data.setValueTextSize(20);
        data.setValueTextColor(Color.WHITE);
        // Chart config
        chart = (PieChart) findViewById(R.id.chart3);
        chart.setData(data);
        chart.animateY(2000);
        chart.setCenterText("SUMMARY of\nCO2 EMISSION\nFOR LAST 365 DAYS");
        chart.setCenterTextSize(20);
        chart.setCenterTextColor(Color.DKGRAY);
        chart.setDescription(null);
        chart.getLegend().setEnabled(true);
        chart.setVisibility(View.VISIBLE);
        chart.invalidate();
    }

    private void getUtilityAverage() {
        float total = 0;
        for (int i = 0; i < utilityAmt; i++) {
            String[] firstU = utilities.getUtility(i).getStartDate().split("/");
            String[] lastU = utilities.getUtility(i).getEndDate().split("/");
            long numDaysForUtility = countDays(firstU, lastU);
            total += utilities.getUtility(i).getEmission() / utilities.getUtility(i).getNumofPeople() / numDaysForUtility;
        }
        total = total / utilityAmt;
        utilityAverage = total;
    }

    //date functions to handle date-strings and Dates
    //today's date
    private void whatDayIsIt() {
        Date date = new Date();

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
        date_in_str = df.format(date);

        String[] checkdate = date_in_str.split("/");
        Month = Integer.parseInt(checkdate[0]);
        Day = Integer.parseInt(checkdate[1]);
        Year = Integer.parseInt(checkdate[2]);
    }

    //last month's date
    private void whatDayIsThirtyDaysPrevious(int i) {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
        Date date = new Date();
        date.setMonth(date.getMonth() - i - 1);
        prev_date_in_str = df.format(date);
        String[] prevcheckdate = prev_date_in_str.split("/");
        prev_Month = Integer.parseInt(prevcheckdate[0]);
        prev_Day = Integer.parseInt(prevcheckdate[1]);
        prev_Year = Integer.parseInt(prevcheckdate[2]);
    }

    //how many dates in between, need to split with dateString.split("/") before calling
    private long countDays(String[] first, String[] last) {
        Date dateOne = new Date(Integer.parseInt(first[1]), Integer.parseInt(first[2]), Integer.parseInt(first[0]));
        Date dateTwo = new Date(Integer.parseInt(last[1]), Integer.parseInt(last[2]), Integer.parseInt(last[0]));
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;
        return delta;
    }

    //Is the date between the firstdate and lastdate provided
    private boolean isBetween(String date, String firstDate, String lastDate) {
        String[] checkdate = date.split("/");
        String[] first = firstDate.split("/");
        String[] last = lastDate.split("/");

        Date date1 = new Date(Integer.parseInt(first[2]), Integer.parseInt(first[0]), Integer.parseInt(first[1]));
        Date date2 = new Date(Integer.parseInt(last[2]), Integer.parseInt(last[0]), Integer.parseInt(last[1]));
        Date datemid = new Date(Integer.parseInt(checkdate[2]), Integer.parseInt(checkdate[0]), Integer.parseInt(checkdate[1]));

        if (datemid.before(date2) && datemid.after(date1)) {
            return true;
        }
        return false;
    }

    //is the firstdate before the lastdate provided
    private boolean isBefore(String firstDate, String lastDate) {
        String[] last = lastDate.split("/");
        String[] first = firstDate.split("/");
        Date d1 = new Date(Integer.parseInt(last[2]), Integer.parseInt(last[0]), Integer.parseInt(last[1]));
        Date d2 = new Date(Integer.parseInt(first[2]), Integer.parseInt(first[0]), Integer.parseInt(first[1]));
        if (d1.before(d2)) {
            return true;
        }
        return false;
    }

    private void setUpButtons() {

        Button switchbtn = (Button) findViewById(R.id.btnSwitchViewYearly);
        switchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lineChart.getVisibility() == View.VISIBLE && chart.getVisibility() == View.INVISIBLE) {
                    chart.setVisibility(View.VISIBLE);
                    lineChart.setVisibility(View.INVISIBLE);
                } else {
                    lineChart.setVisibility(View.VISIBLE);
                    chart.setVisibility(View.INVISIBLE);
                }
            }
        });
        final Button organizeBtn = (Button) findViewById(R.id.btnOrganizeY);
        organizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkOrganization) {
                    setUpArraysRouteWise();
                    setUpPieChart();
                    setUpLineChart();
                    organizeBtn.setText(R.string.change_mode);
                    lineChart.setVisibility(View.INVISIBLE);
                    checkOrganization = true;
                } else {
                    setUpArrays();
                    setUpPieChart();
                    setUpLineChart();
                    organizeBtn.setText(R.string.change_mode);
                    lineChart.setVisibility(View.INVISIBLE);
                    checkOrganization = false;
                }
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, LastYearActivity.class);
    }
}

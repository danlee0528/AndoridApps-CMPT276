package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Activity for checking User's previous month data from Journeys and Utilities.
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
 */
public class LastMonthActivity extends AppCompatActivity {
    //calculated as: 30% of 2005 Daily CO2 Emission in Canada divided by Population in 2005
    //<--Canada goals - 30% of 2005 level emissions-->
    public static final double PARISACCORDCO2PERCAPITA = 19.04;

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
    //public static final String TOTALEMISSIONS ="totalEmissions";
    public static final String BUS = "bus";
    public static final String BIKE = "bike";
    public static final String SKYTRAIN = "skytrain";
    public static final String WALK = "walk";
    public static final String ICONID = "IconID";
    //for pie chart
    PieChart chart;
    List<PieEntry> entries;
    PieDataSet dataSet;
    PieData data;

    //for line graph
    LineChart lineChart;
    ArrayList<Float> ems = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Entry> userAxis = new ArrayList<>();
    ArrayList<Entry> averageCanadianAxis = new ArrayList<>();
    ArrayList<Entry> ParisAccordAxis = new ArrayList<>();

    //data storage
    float utilityAverage;
    int utilityAmt;
    int journeyAmt;
    boolean checkOrganization = false;

    public static final String SETTING = "CarbonFootprintTrackerSettings";
    public static final String TREESETTING = "TreeSetting";
    boolean setting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_month);

        utilities = loadUtilities();
        journeys = loadJourneys();
        getSetting();
        setUpArrays();
        setUpPieChart();
        setUpLineGraphData();
        setUpLineGraph();
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

    private void setUpButtons() {
        Button switchbtn = (Button) findViewById(R.id.btnSwitchViewMonthly);
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

        final Button organizeBtn = (Button) findViewById(R.id.btnOrganizeM);
        organizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkOrganization) {
                    setUpRouteWiseArrays();
                    setUpPieChart();
                    organizeBtn.setText(R.string.change_mode);
                    lineChart.setVisibility(View.INVISIBLE);
                    checkOrganization = true;
                } else {
                    setUpArrays();
                    setUpPieChart();
                    organizeBtn.setText(R.string.change_mode);
                    lineChart.setVisibility(View.INVISIBLE);
                    checkOrganization = false;
                }
            }
        });
    }

    private void setUpArrays() {
        entries = new ArrayList<>();
        float entrySize = 0;
        whatDayIsIt();
        whatDayIsThirtyDaysPrevious();
        ems = new ArrayList<>();
        names = new ArrayList<>();

        String[] firstjourn = prev_date_in_str.split("/");
        String[] lastjourn = date_in_str.split("/");
        long datesbetween = countDays(firstjourn, lastjourn);
        for (int i = 0; i < journeyAmt; i++) {

            if (isBetween(journeys.getJourney(i).getDateString(), prev_date_in_str, date_in_str)) {
                String nameOfJourney = journeys.getJourney(i).getName();
                //if car is known in names arraylist
                if (names.contains(nameOfJourney)) {
                    for (int j = 0; j < entrySize; j++) {
                        if (names.get(j).equals(nameOfJourney)) {
                            float em = ems.get(j);
                            String na = names.get(j);
                            names.remove(j);
                            ems.remove(j);
                            names.add(na);
                            ems.add(em + (float) journeys.getJourney(i).getTotalEmissions());
                        }
                    }
                } else {
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

                entrySize++;

                if (isBefore(utilities.getUtility(i).getEndDate(), date_in_str)
                        && isBefore(utilities.getUtility(i).getStartDate(), prev_date_in_str)) {

                    String[] firstU = utilities.getUtility(i).getStartDate().split("/");
                    String[] lastU = utilities.getUtility(i).getEndDate().split("/");
                    long numDaysForUtility = countDays(firstU, lastU);

                    String[] first = prev_date_in_str.split("/");
                    String[] last = utilities.getUtility(i).getEndDate().split("/");
                    long numDays = countDays(first, last);

                    names.add(utilities.getUtility(i).getName());
                    ems.add((float) (utilities.getUtility(i).getEmission() /
                            utilities.getUtility(i).getNumofPeople() /
                            numDaysForUtility) * numDays);
                    //
                } else if (isBefore(prev_date_in_str, utilities.getUtility(i).getStartDate())
                        && isBefore(date_in_str, utilities.getUtility(i).getEndDate())) {

                    String[] firstU = utilities.getUtility(i).getStartDate().split("/");
                    String[] lastU = utilities.getUtility(i).getEndDate().split("/");
                    long numDaysForUtility = countDays(firstU, lastU);

                    String[] first = utilities.getUtility(i).getStartDate().split("/");
                    String[] last = date_in_str.split("/");
                    long numDays = countDays(first, last);

                    names.add(utilities.getUtility(i).getName());
                    ems.add((float) utilities.getUtility(i).getEmission() /
                            utilities.getUtility(i).getNumofPeople() /
                            numDaysForUtility * numDays);
                } else {
                    String[] firstU = utilities.getUtility(i).getStartDate().split("/");
                    String[] lastU = utilities.getUtility(i).getEndDate().split("/");
                    long numDaysForUtility = countDays(firstU, lastU);

                    names.add(utilities.getUtility(i).getName());
                    ems.add((float) utilities.getUtility(i).getEmission() /
                            utilities.getUtility(i).getNumofPeople() /
                            numDaysForUtility * datesbetween);
                }
            } else if (isBetween(utilities.getUtility(i).getStartDate(), prev_date_in_str, date_in_str)
                    &&
                    isBetween(utilities.getUtility(i).getEndDate(), prev_date_in_str, date_in_str)) {
                entrySize++;

                names.add(utilities.getUtility(i).getName());
                ems.add(((float) utilities.getUtility(i).getEmission()
                        / utilities.getUtility(i).getNumofPeople()));
            }
        }

        if (setting) {
            for (int i = 0; i < entrySize; i++) {
                ems.set(i, ems.get(i) * 2.8f);
            }
        }
        for (int x = 0; x < entrySize; x++) {
            entries.add(new PieEntry(ems.get(x),
                    names.get(x)));
        }
    }

    private void setUpRouteWiseArrays() {
        entries = new ArrayList<>();
        float entrySize = 0;
        getUtilityAverage();
        ems = new ArrayList<>();
        names = new ArrayList<>();
        whatDayIsIt();
        whatDayIsThirtyDaysPrevious();

        String[] firstjourn = prev_date_in_str.split("/");
        String[] lastjourn = date_in_str.split("/");
        long datesbetween = countDays(firstjourn, lastjourn);

        for (int i = 0; i < journeyAmt; i++) {

            if (isBetween(journeys.getJourney(i).getDateString(), prev_date_in_str, date_in_str)) {
                String nameOfJourney = journeys.getJourney(i).getRouteName();
                //if car is known in names arraylist
                if (names.contains(nameOfJourney)) {
                    for (int j = 0; j < entrySize; j++) {
                        if (names.get(j).equals(nameOfJourney)) {
                            float em = ems.get(j);
                            String na = names.get(j);
                            names.remove(j);
                            ems.remove(j);
                            names.add(na);
                            ems.add(em + (float) journeys.getJourney(i).getTotalEmissions());
                        }
                    }
                } else {
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

                entrySize++;

                if (isBefore(utilities.getUtility(i).getEndDate(), date_in_str)
                        && isBefore(utilities.getUtility(i).getStartDate(), prev_date_in_str)) {

                    String[] firstU = utilities.getUtility(i).getStartDate().split("/");
                    String[] lastU = utilities.getUtility(i).getEndDate().split("/");
                    long numDaysForUtility = countDays(firstU, lastU);

                    String[] first = prev_date_in_str.split("/");
                    String[] last = utilities.getUtility(i).getEndDate().split("/");
                    long numDays = countDays(first, last);

                    names.add(utilities.getUtility(i).getName());
                    ems.add((float) (utilities.getUtility(i).getEmission() /
                            utilities.getUtility(i).getNumofPeople() /
                            numDaysForUtility) * numDays);
                    //
                } else if (isBefore(prev_date_in_str, utilities.getUtility(i).getStartDate())
                        && isBefore(date_in_str, utilities.getUtility(i).getEndDate())) {

                    String[] firstU = utilities.getUtility(i).getStartDate().split("/");
                    String[] lastU = utilities.getUtility(i).getEndDate().split("/");
                    long numDaysForUtility = countDays(firstU, lastU);

                    String[] first = utilities.getUtility(i).getStartDate().split("/");
                    String[] last = date_in_str.split("/");
                    long numDays = countDays(first, last);

                    names.add(utilities.getUtility(i).getName());
                    ems.add((float) utilities.getUtility(i).getEmission() /
                            utilities.getUtility(i).getNumofPeople() /
                            numDaysForUtility * numDays);
                } else {
                    String[] firstU = utilities.getUtility(i).getStartDate().split("/");
                    String[] lastU = utilities.getUtility(i).getEndDate().split("/");
                    long numDaysForUtility = countDays(firstU, lastU);

                    names.add(utilities.getUtility(i).getName());
                    ems.add((float) utilities.getUtility(i).getEmission() /
                            utilities.getUtility(i).getNumofPeople() /
                            numDaysForUtility * datesbetween);
                }
            } else if (isBetween(utilities.getUtility(i).getStartDate(), prev_date_in_str, date_in_str)
                    &&
                    isBetween(utilities.getUtility(i).getEndDate(), prev_date_in_str, date_in_str)) {
                entrySize++;
                names.add(utilities.getUtility(i).getName());
                ems.add(((float) utilities.getUtility(i).getEmission()
                        / utilities.getUtility(i).getNumofPeople()));
            }
        }
        for (int i = 0; i < entrySize; i++) {
            if (setting) {
                ems.set(i, ems.get(i) * 2.8f);
            }
        }
        for (int x = 0; x < entrySize; x++) {
            entries.add(new PieEntry(ems.get(x),
                    names.get(x)));
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
        chart = (PieChart) findViewById(R.id.chart2);
        chart.setData(data);
        chart.animateY(2000);
        chart.setCenterText("SUMMARY of\nCO2 EMISSION \nFOR LAST 28 DAYS");
        chart.setCenterTextSize(20);
        chart.setCenterTextColor(Color.DKGRAY);
        chart.setDescription(null);
        chart.getLegend().setEnabled(true);
        chart.setVisibility(View.VISIBLE);
        chart.invalidate();
    }

    private void setUpLineGraphData() {
        //set date of today and month previous correctly
        whatDayIsIt();
        whatDayIsThirtyDaysPrevious();
        float totalEmissions;
        userAxis = new ArrayList<>();
        //xAxis.add(0,date_in_str);
        //get number of days between these 2 dates
        String[] firstjourn = prev_date_in_str.split("/");
        String[] lastjourn = date_in_str.split("/");
        long datesbetween = countDays(firstjourn, lastjourn);
        getUtilityAverage();

        //loop for each day in month. starting from today, ending at previous month
        for (int i = 0; i < datesbetween; i++) {
            totalEmissions = 0;
            for (int j = 0; j < journeyAmt; j++) {
                if (isTheSame(journeys.getJourney(j).getDateString(), date_in_str)) {
                    totalEmissions += journeys.getJourney(j).getTotalEmissions();
                }
            }
            for (int k = 0; k < utilityAmt; k++) {
                String[] firstU = utilities.getUtility(k).getStartDate().split("/");
                String[] lastU = utilities.getUtility(k).getEndDate().split("/");
                long numDaysForUtility = countDays(firstU, lastU);

                if (isBetween(date_in_str, utilities.getUtility(k).getStartDate(), utilities.getUtility(k).getEndDate())) {
                    totalEmissions += utilities.getUtility(k).getEmission() / numDaysForUtility / utilities.getUtility(k).getNumofPeople();
                } else {
                    totalEmissions += utilityAverage;
                }
            }
            if (setting) {
                userAxis.add(new Entry(i, totalEmissions * 2.8f));
                ParisAccordAxis.add(new Entry(i, (float) PARISACCORDCO2PERCAPITA * 2.8f));
            } else {
                userAxis.add(new Entry(i, totalEmissions));
                ParisAccordAxis.add(new Entry(i, (float) PARISACCORDCO2PERCAPITA));
            }

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
            Date myDate = null;
            try {
                myDate = df.parse(date_in_str);
            } catch (ParseException e) {
                Log.wtf(TAG, "Parse failed, should never happen");
                e.printStackTrace();
            }
            // Use the Calendar class to subtract one day
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            // Use the date formatter to produce a formatted date string
            Date previousDate = calendar.getTime();
            date_in_str = df.format(previousDate);
        }
    }

    private void setUpLineGraph() {
        lineChart = (LineChart) findViewById(R.id.lineChartM);
        ArrayList<ILineDataSet> dataSet = new ArrayList<>();

        LineDataSet lds1 = new LineDataSet(userAxis, "Your Data");
        lds1.setDrawCircles(false);
        lds1.setColor(Color.WHITE);
        lds1.setValueTextSize(9f);

        LineDataSet lds2 = new LineDataSet(ParisAccordAxis, "Canada paris accord goal");
        lds2.setDrawCircles(false);
        lds2.setColor(Color.GREEN);
        lds2.setValueTextSize(9f);


        dataSet.add(lds1);
        dataSet.add(lds2);
        lineChart.setData(new LineData(dataSet));
        lineChart.setVisibleXRangeMaximum(31f);
        lineChart.setVisibility(View.INVISIBLE);
        lineChart.invalidate();
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

    private void whatDayIsIt() {
        Date date = new Date();

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
        date_in_str = df.format(date);
        String[] checkdate = date_in_str.split("/");
        Month = Integer.parseInt(checkdate[0]);
        Day = Integer.parseInt(checkdate[1]);
        Year = Integer.parseInt(checkdate[2]);
    }

    private void whatDayIsThirtyDaysPrevious() {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
        Date date = new Date();
        date.setMonth(date.getMonth() - 1);
        prev_date_in_str = df.format(date);
        String[] prevcheckdate = prev_date_in_str.split("/");
        prev_Month = Integer.parseInt(prevcheckdate[0]);
        prev_Day = Integer.parseInt(prevcheckdate[1]);
        prev_Year = Integer.parseInt(prevcheckdate[2]);
    }

    private long countDays(String[] first, String[] last) {
        Date dateOne = new Date(Integer.parseInt(first[2]), Integer.parseInt(first[0]), Integer.parseInt(first[1]));
        Date dateTwo = new Date(Integer.parseInt(last[2]), Integer.parseInt(last[0]), Integer.parseInt(last[1]));
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        //1000 ms in s, 60 s in minute, 60 minutes in hour, 24 hours in day
        return (timeTwo - timeOne) / oneDay;
    }

    private boolean isBetween(String date, String firstDate, String lastDate) {
        String[] checkdate = date.split("/");
        String[] first = firstDate.split("/");
        String[] last = lastDate.split("/");

        Date date1 = new Date(Integer.parseInt(first[2]), Integer.parseInt(first[0]), Integer.parseInt(first[1]));
        Date date2 = new Date(Integer.parseInt(last[2]), Integer.parseInt(last[0]), Integer.parseInt(last[1]));
        Date datemid = new Date(Integer.parseInt(checkdate[2]), Integer.parseInt(checkdate[0]), Integer.parseInt(checkdate[1]));

        return datemid.before(date2) && datemid.after(date1);
    }

    private boolean isBefore(String firstDate, String lastDate) {
        String[] last = lastDate.split("/");
        String[] first = firstDate.split("/");
        Date d1 = new Date(Integer.parseInt(last[2]), Integer.parseInt(last[0]), Integer.parseInt(last[1]));
        Date d2 = new Date(Integer.parseInt(first[2]), Integer.parseInt(first[0]), Integer.parseInt(first[1]));
        return d1.before(d2);
    }

    public boolean isTheSame(String date1, String date2) {
        int check = 0;
        String onedate[] = date1.split("/");
        String twodate[] = date2.split("/");
        for (int i = 0; i < 3; i++) {
            if (Integer.parseInt(onedate[i]) == Integer.parseInt(twodate[i])) {
                check += 1;
            }
        }
        return check == 3;
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
        return new Intent(context, LastMonthActivity.class);
    }
}

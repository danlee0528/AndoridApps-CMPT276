package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

/**
 * Add Car saves nickname,make,model and year of a car user selects from cardb.db.zip
 * Also handles editing cars. Passes saved car via singleton class: carSingleton
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.kenneyw.carbonfootprinttracker.model.Car;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.CarCollection;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.CarSingleton;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.DatabaseAccess;
import com.cmpt276.kenneyw.carbonfootprinttracker.R;

import java.util.List;

public class AddCar extends AppCompatActivity {

    public static final String POS_EDIT = "posEdit";
    DatabaseAccess databaseAccess;
    List<String> makeList;
    List<String> yearList;
    String selectedYear;
    String selectedMake;
    String selectedModel;
    int selectIconID;
    CarCollection cars = new CarCollection();
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        Intent i = getIntent();
        if (i.hasExtra(POS_EDIT)) {
            pos = i.getIntExtra(POS_EDIT, 0);
        } else {
            pos = 0;
        }

        // Code was retrieve from: http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
        // this hiades soft-keyboard after user enters a nickname for the car
        final EditText inputName = (EditText) findViewById(R.id.nick_name_from_user);
        inputName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    // Do stuff when user presses enter
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
                return false;
            }
        });
        // Hide keyboard on start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        openDatabase();
        setupYearSpinner();
        setupIconSpinner();
        populateListView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {//Last function to be called, is called before the Activity is closed.
        super.onDestroy();
        closeDatabase();//Closes the database.
    }

    private void setupIconSpinner() {
        Spinner iconSpinner = (Spinner) findViewById(R.id.iconSpinner);
        String[] iconTypes = {"Regular Car", "Modern Car", "Classic Car", "Sport Car", "Truck"};

        ArrayAdapter<String> iconAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, iconTypes);
        iconSpinner.setAdapter(iconAdapter);
        iconSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //Toast.makeText(getApplicationContext(), "Car selected", Toast.LENGTH_SHORT).show();
                    selectIconID = R.drawable.car_icon_2;
                } else if (position == 1) {
                    //Toast.makeText(getApplicationContext(), "Modern selected", Toast.LENGTH_SHORT).show();
                    selectIconID = R.drawable.modern;
                } else if (position == 2) {
                    //Toast.makeText(getApplicationContext(), "Classic selected", Toast.LENGTH_SHORT).show();
                    selectIconID = R.drawable.classic;
                } else if (position == 3) {
                    //Toast.makeText(getApplicationContext(), "Sport selected", Toast.LENGTH_SHORT).show();
                    selectIconID = R.drawable.sport;
                } else if (position == 4) {
                    //Toast.makeText(getApplicationContext(), "Truck selected", Toast.LENGTH_SHORT).show();
                    selectIconID = R.drawable.truck;
                } else {
                    // do nothing
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setupYearSpinner() { //Sets up the year spinner. Changes the values of make spinner when user selects a year.
        Spinner yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        yearList = databaseAccess.getYears();
        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setOnItemSelectedListener(new yearSpinner());
        yearSpinner.setAdapter(yearAdapter);
    }

    private class yearSpinner implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedYear = parent.getItemAtPosition(position).toString();
            setupMakeSpinner(selectedYear);
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private void setupMakeSpinner(String year) {//Sets up the make spinner. Changes the values of model spinner when user selects a year.
        Spinner makeSpinner = (Spinner) findViewById(R.id.make_spinner);
        makeList = databaseAccess.getMakes(year);
        ArrayAdapter<CharSequence> makeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, makeList);
        makeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        makeSpinner.setOnItemSelectedListener(new makeSpinner());
        makeSpinner.setAdapter(makeAdapter);
    }

    private class makeSpinner implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedMake = parent.getItemAtPosition(position).toString();
            setupModelSpinner(selectedYear, selectedMake);
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private void setupModelSpinner(String year, String make) {//Sets up the model spinner. Populates the listview when user selects an item.
        Spinner modelSpinner = (Spinner) findViewById(R.id.model_spinner);
        List<String> modelList = databaseAccess.getModels(year, make);
        ArrayAdapter<CharSequence> modelAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, modelList);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setOnItemSelectedListener(new modelSpinner());
        modelSpinner.setAdapter(modelAdapter);
    }

    private class modelSpinner implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedModel = parent.getItemAtPosition(position).toString();
            Log.i("This", selectedModel);
            makeCarList(selectedYear, selectedMake, selectedModel);
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    //Makes an instance of the DatabaseAccess class and queries the Database. This is the meat and potatoes of the whole class.
    //Calls the getTempCarList function in DatabaseAccess, which in turn returns all the values of matching database queries
    //if a List<String[]>, so that you can .get() and then reference like an array.
    private void makeCarList(String selectedYear, String selectedMake, String selectedModel) {
        cars = new CarCollection();
        List<String[]> tempCarList = databaseAccess.getTempCarList(selectedYear, selectedMake, selectedModel);
        for (int i = 0; i < tempCarList.size(); i++) {
            String[] carData = tempCarList.get(i);
            Car car = new Car();
            car.setMake(carData[2]);
            car.setModel(carData[3]);
            car.setYear(Integer.parseInt(carData[21]));
            car.setYear(Integer.parseInt(carData[21]));
            car.setTransmission(carData[19]);
            car.setGasType(carData[17]);
            car.setLiterEngine(roundToTwoDecimals(Double.parseDouble(carData[16])));
            if (carData[4].matches("0")) {
                if (carData[7].matches("0")) {
                    car.setCityEmissions(Double.parseDouble(carData[10]));
                    car.setHighwayEmissions(Double.parseDouble(carData[11]));
                } else {
                    car.setCityEmissions(Double.parseDouble(carData[7]));
                    car.setHighwayEmissions(Double.parseDouble(carData[8]));
                }
            } else {
                car.setCityEmissions(Double.parseDouble(carData[4]));
                car.setHighwayEmissions(Double.parseDouble(carData[5]));
            }
            cars.setIconID(selectIconID);
            cars.addCar(car);
        }
        populateListView();
    }

    //Populates the listView with the values returned from makeCarList.
    private void populateListView() {
        ListView listView = (ListView) findViewById(R.id.car_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_for_list, cars.getCarsDescriptions());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText inputName = (EditText) findViewById(R.id.nick_name_from_user);
                String userInputName = inputName.getText().toString();
                if (userInputName.matches("")) {
                    Toast.makeText(AddCar.this, R.string.error_toast, Toast.LENGTH_SHORT).show();
                } else {
                    CarSingleton masterCar = CarSingleton.getInstance();
                    Car tempCar = cars.getCar(position);
                    masterCar.setName(userInputName);
                    masterCar.setYear(tempCar.getYear());
                    masterCar.setCityEmissions(tempCar.getCityEmissions());
                    masterCar.setHighwayEmissions(tempCar.getHighwayEmissions());
                    masterCar.setModel(tempCar.getModel());
                    masterCar.setGasType(tempCar.getGasType());
                    masterCar.setLiterEngine(tempCar.getLiterEngine());
                    masterCar.setTransmission(tempCar.getTransmission());
                    masterCar.setMake(tempCar.getMake());
                    masterCar.setIconID(selectIconID);

                    Intent i = new Intent();
                    i.putExtra(POS_EDIT, pos);
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });
    }

    //Rounding for the engine displacement. I was going to put this in Calculate, but it made calling it way more of a headache
    //than I was ready for.
    private double roundToTwoDecimals(double value) {
        int factor = 100;
        value = value * factor;
        long tempValue = Math.round(value);
        return (double) tempValue / factor;
    }

    //Simple database close function.
    private void closeDatabase() {
        databaseAccess.close();
    }

    //gets and instance of the database and opens it.
    private void openDatabase() {
        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddCar.class);
    }
}
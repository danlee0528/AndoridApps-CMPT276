package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

/**
 * This class shows the user a list of cars (with engine displacement in L and transmission)
 * When user clicks an item on the list, it directs the user to "Select Route" screen
 * User is allowed to add/edit/delete. Saves saved cars with Shared Prefs, might transfar db handling
 * to SQL. Handles Journey editing via smart intent passing.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.Car;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.CarCollection;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.CarSingleton;

import java.util.List;

public class SelectCar extends AppCompatActivity {
    public static final int ROUTE_SELECTED = 4;
    public static final int CAR_ADDED = 5;
    public static final int EDIT_CAR = 6;
    private static final String SHAREDPREF_SET = "CarbonFootprintTrackerCars";
    private static final String SHAREDPREF_ITEM_AMOUNTOFCARS = "AmountOfCars";
    public static final String NAME = "name";
    public static final String GASTYPE = "gasType";
    public static final String MPGCITY = "mpgCity";
    public static final String MPGHIGHWAY = "mpgHighway";
    public static final String TRANSMISSION = "transmission";
    public static final String LITERENGINE = "literEngine";
    public static final String MAKE = "Make";
    public static final String MODEL = "Model";
    public static final String YEAR = "Year";
    public static final String ICONID = "IconID";
    public static final String POS_FOR_EDIT_CAR = "posEdit";
    public static final String POSITION_FOR_EDIT_JOURNEY = "pos";
    CarCollection myCars = new CarCollection();
    public int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);
        myCars = loadCars();
        Intent i = getIntent();
        if (i.hasExtra(POSITION_FOR_EDIT_JOURNEY)) {
            pos = i.getIntExtra(POSITION_FOR_EDIT_JOURNEY, 0);
        } else {
            pos = 0;
        }
        setupAddCarButton();
        setCarList();
        setUpAlternateTransportationBtns();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        saveCars();
        finish();
        return true;
    }

    private void setUpAlternateTransportationBtns() {
        Button btnSkytrain = (Button) findViewById(R.id.btnSkytrain);
        Button btnBus = (Button) findViewById(R.id.btnBus);
        Button btnWalk = (Button) findViewById(R.id.btnWalk);
        Button btnBike = (Button) findViewById(R.id.bike_button);
        btnSkytrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarSingleton finalCar = CarSingleton.getInstance();
                finalCar.setMake("");
                finalCar.setModel("");
                finalCar.setName("Skytrain");
                finalCar.setYear(0);
                finalCar.setCityEmissions(0);
                finalCar.setHighwayEmissions(0);
                finalCar.setLiterEngine(0);
                finalCar.setGasType("");
                finalCar.setTransmission("");
                finalCar.setSkytrain(true);
                finalCar.setBus(false);
                finalCar.setWalk(false);
                finalCar.setBike(false);
                finalCar.setIconID(R.drawable.train_icon);
                Intent SelectCar2SelectRoute = SelectRoute.makeIntent(SelectCar.this);
                startActivityForResult(SelectCar2SelectRoute, ROUTE_SELECTED);
            }
        });
        btnBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarSingleton finalCar = CarSingleton.getInstance();
                finalCar.setMake("");
                finalCar.setModel("");
                finalCar.setName("Bus");
                finalCar.setYear(0);
                finalCar.setCityEmissions(0);
                finalCar.setHighwayEmissions(0);
                finalCar.setLiterEngine(0);
                finalCar.setGasType("");
                finalCar.setTransmission("");
                finalCar.setSkytrain(false);
                finalCar.setBus(true);
                finalCar.setWalk(false);
                finalCar.setIconID(R.drawable.bus_icon);
                Intent SelectCar2SelectRoute = SelectRoute.makeIntent(SelectCar.this);
                startActivityForResult(SelectCar2SelectRoute, ROUTE_SELECTED);
            }
        });
        btnWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarSingleton finalCar = CarSingleton.getInstance();
                finalCar.setMake("");
                finalCar.setModel("");
                finalCar.setName("Walk");
                finalCar.setYear(0);
                finalCar.setCityEmissions(0);
                finalCar.setHighwayEmissions(0);
                finalCar.setLiterEngine(0);
                finalCar.setGasType("");
                finalCar.setTransmission("");
                finalCar.setSkytrain(false);
                finalCar.setBus(false);
                finalCar.setWalk(true);
                finalCar.setBike(false);
                finalCar.setIconID(R.drawable.walk_icon);
                Intent SelectCar2SelectRoute = SelectRoute.makeIntent(SelectCar.this);
                startActivityForResult(SelectCar2SelectRoute, ROUTE_SELECTED);
            }
        });
        btnBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarSingleton finalCar = CarSingleton.getInstance();
                finalCar.setMake("");
                finalCar.setModel("");
                finalCar.setName("Bike");
                finalCar.setYear(0);
                finalCar.setCityEmissions(0);
                finalCar.setHighwayEmissions(0);
                finalCar.setLiterEngine(0);
                finalCar.setGasType("");
                finalCar.setTransmission("");
                finalCar.setSkytrain(false);
                finalCar.setBus(false);
                finalCar.setWalk(true);
                finalCar.setBike(true);
                finalCar.setIconID(R.drawable.bike_icon);
                Intent SelectCar2SelectRoute = SelectRoute.makeIntent(SelectCar.this);
                startActivityForResult(SelectCar2SelectRoute, ROUTE_SELECTED);
            }
        });


    }

    public CarCollection loadCars() {
        CarCollection cars = new CarCollection();
        SharedPreferences pref = getSharedPreferences(SHAREDPREF_SET, MODE_PRIVATE);
        int carAmt = pref.getInt(SHAREDPREF_ITEM_AMOUNTOFCARS, 0);
        for (int i = 0; i < carAmt; i++) {
            Car car = new Car(pref.getString(i + NAME, ""), pref.getString(i + MAKE, ""), pref.getString(i + MODEL, ""),
                    Double.longBitsToDouble(pref.getLong(i + MPGHIGHWAY, 0)),
                    Double.longBitsToDouble(pref.getLong(i + MPGCITY, 0)), pref.getInt(i + YEAR, 0),
                    pref.getString(i + TRANSMISSION, ""),
                    Double.longBitsToDouble(pref.getLong(i + LITERENGINE, 0)), pref.getString(i + GASTYPE, ""),
                    pref.getInt(i + ICONID, 0));
            cars.addCar(car);
        }
        return cars;
    }

    private void saveCars() {
        SharedPreferences pref = getSharedPreferences(SHAREDPREF_SET, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        int carAmt = myCars.countCars();
        for (int i = 0; i < carAmt; i++) {
            editor.putString(i + NAME, myCars.getCar(i).getName());
            editor.putString(i + MAKE, myCars.getCar(i).getMake());
            editor.putString(i + MODEL, myCars.getCar(i).getModel());
            editor.putString(i + TRANSMISSION, myCars.getCar(i).getTransmission());
            editor.putString(i + GASTYPE, myCars.getCar(i).getGasType());
            editor.putInt(i + YEAR, myCars.getCar(i).getYear());
            editor.putLong(i + MPGCITY, Double.doubleToRawLongBits(myCars.getCar(i).getCityEmissions()));
            editor.putLong(i + MPGHIGHWAY, Double.doubleToRawLongBits(myCars.getCar(i).getHighwayEmissions()));
            editor.putLong(i + LITERENGINE, Double.doubleToRawLongBits(myCars.getCar(i).getLiterEngine()));
            editor.putInt(i + ICONID, myCars.getCar(i).getIconID());
        }
        editor.putInt(SHAREDPREF_ITEM_AMOUNTOFCARS, carAmt);
        editor.apply();
    }

    private void setupAddCarButton() {
        // Direct to "AddCar" screen
        Button add_button = (Button) findViewById(R.id.add_car_button_select_car);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SelectCar2AddCar = AddCar.makeIntent(SelectCar.this);
                startActivityForResult(SelectCar2AddCar, CAR_ADDED);
                //need this
            }
        });
    }

    private void setCarList() {
        // Build Adapter
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_for_list, myCars.getCarsDescriptionsWithName());
        ArrayAdapter<Car> adapter = new MyListAdapter();
        // Configure the list view
        ListView list = (ListView) findViewById(R.id.carlist);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarSingleton finalCar = CarSingleton.getInstance();
                finalCar.setMake(myCars.getCar(position).getMake());
                finalCar.setModel(myCars.getCar(position).getModel());
                finalCar.setName(myCars.getCar(position).getName());
                finalCar.setYear(myCars.getCar(position).getYear());
                finalCar.setCityEmissions(myCars.getCar(position).getCityEmissions());
                finalCar.setHighwayEmissions(myCars.getCar(position).getCityEmissions());
                finalCar.setLiterEngine(myCars.getCar(position).getLiterEngine());
                finalCar.setGasType(myCars.getCar(position).getGasType());
                finalCar.setTransmission(myCars.getCar(position).getTransmission());
                finalCar.setWalk(false);
                finalCar.setBus(false);
                finalCar.setSkytrain(false);
                finalCar.setBike(false);
                finalCar.setIconID(myCars.getCar(position).getIconID());
                Intent SelectCar2SelectRoute = SelectRoute.makeIntent(SelectCar.this);
                startActivityForResult(SelectCar2SelectRoute, ROUTE_SELECTED);
            }
        });
        registerForContextMenu(list);
    }

    // Custom Adapter for carlist with icons
    private class MyListAdapter extends ArrayAdapter<Car> {

        public MyListAdapter() {
            super(SelectCar.this, R.layout.layout_for_carlist_with_icons, myCars.returnCarArrays());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.layout_for_carlist_with_icons, parent, false);
            }

            Log.i("TEST", "SIZE OF myCars = " + myCars.countCars());
            Log.i("TEST", "ICONID = " + myCars.getCar(position).getIconID());

            // Find the car to work with
            Car currentCar = myCars.getCar(position);
            Log.i("TEST", "CurrentCar = " + currentCar.getName());

            // Set icon and info
            ImageView iconView = (ImageView) itemView.findViewById(R.id.icon_imageView);
            iconView.setImageResource(currentCar.getIconID());

            TextView carName = (TextView) itemView.findViewById(R.id.carlist_name);
            carName.setText(currentCar.getName());

            TextView carMake = (TextView) itemView.findViewById(R.id.carlist_make);
            carMake.setText(currentCar.getMake());

            TextView carModel = (TextView) itemView.findViewById(R.id.carlist_model);
            carModel.setText(currentCar.getModel());

            TextView carYear = (TextView) itemView.findViewById(R.id.carlist_year);
            carYear.setText("" + currentCar.getYear());

            return itemView;
        }
    }

    //Context Menu Code taken and modified from:
    //https://www.mikeplate.com/2010/01/21/show-a-context-menu-for-long-clicks-in-an-android-listview/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.carlist) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(myCars.getCar(info.position).getName());
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.menu);
        String menuItemName = menuItems[menuItemIndex];
        switch (menuItemName) {
            case "Edit":
                Intent i = AddCar.makeIntent(SelectCar.this);
                i.putExtra(POS_FOR_EDIT_CAR, info.position);
                startActivityForResult(i, EDIT_CAR);
                break;
            case "Delete":
                myCars.deleteCar(info.position);
                setCarList();
                break;
        }
        return true;
    }

    //need this
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ROUTE_SELECTED:
                if (resultCode == RESULT_OK) {
                    Intent i = new Intent();
                    i.putExtra(POSITION_FOR_EDIT_JOURNEY, pos);
                    setResult(RESULT_OK, i);
                    saveCars();
                    finish();
                } else {
                    setupAddCarButton();
                    setCarList();
                }
                break;
            case CAR_ADDED:
                if (resultCode == RESULT_OK) {
                    CarSingleton masterCar = CarSingleton.getInstance();
                    Car tempCar = new Car(masterCar.getName(),
                            masterCar.getMake(),
                            masterCar.getModel(),
                            masterCar.getHighwayEmissions(),
                            masterCar.getCityEmissions(),
                            masterCar.getYear(),
                            masterCar.getTransmission(),
                            masterCar.getLiterEngine(),
                            masterCar.getGasType(),
                            masterCar.getIconID()
                    );
                    myCars.addCar(tempCar);
                }
                saveCars();
                setupAddCarButton();
                setCarList();
                break;
            case EDIT_CAR:
                if (resultCode == RESULT_OK) {
                    CarSingleton masterCar = CarSingleton.getInstance();
                    Car tempCar = new Car(masterCar.getName(),
                            masterCar.getMake(),
                            masterCar.getModel(),
                            masterCar.getHighwayEmissions(),
                            masterCar.getCityEmissions(),
                            masterCar.getYear(),
                            masterCar.getTransmission(),
                            masterCar.getLiterEngine(),
                            masterCar.getGasType(),
                            masterCar.getIconID()
                    );
                    int position = data.getIntExtra(POS_FOR_EDIT_CAR, 0);
                    myCars.changeCar(tempCar, position);
                    saveCars();
                    setCarList();
                }
                break;
        }
    }

    public void onBackPressed() {
        saveCars();
        finish();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, SelectCar.class);
    }
}
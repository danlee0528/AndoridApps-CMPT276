package com.cmpt276.kenneyw.carbonfootprinttracker.ui;
/**
 * This Class shows the user a list of saved journeys, and can add delete and edit journeys.
 * User can also see CO2 emitted for chosen journey in a dialog. Saves journeys from route and car singletons, via shared preference.
 * might transfer database mgmt. to SQL
 */
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import android.widget.TextView;
import com.cmpt276.kenneyw.carbonfootprinttracker.R;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.Car;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.TipHelperSingleton;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.CarSingleton;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.DateSingleton;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.Journey;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.JourneyCollection;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.RouteSingleton;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.UtilitySingleton;
import java.util.Date;
public class SelectJourney extends AppCompatActivity {
    private static final String TAG = "CarbonFootprintTracker";
    private static final int CAR_AND_ROUTE_SELECTED = 1;
    private static final int EDIT_JOURNEY = 2;
    private static final String SHAREDPREF_SET = "CarbonFootprintTrackerJournies";
    private static final String SHAREDPREF_SET2 = "CarbonFootprintTrackerUtilities";
    private static final String SHAREDPREF_SET3 = "CarbonFootprintTrackerTips";
    private static final String SHAREDPREF_ITEM_AMOUNTOFJOURNEYS = "AmountOfJourneys";
    private static final String SHAREDPREF_ITEM_AMOUNTOFUTILITIES = "AmountOfUtilities";
    public static final String NAME = "name";
    public static final String ROUTENAME = "routeName";
    public static final String CITY = "city";
    public static final String HIGHWAY = "highway";
    public static final String GASTYPE = "gasType";
    public static final String MPGCITY = "mpgCity";
    public static final String MPGHIGHWAY = "mpgHighway";
    public static final String DATESTRING = "dateString";
    public static final String LITERENGINE = "literEngine";
    public static final String TOTALEMISSIONS = "totalEmissions";
    public static final String BUS = "bus";
    public static final String BIKE = "bike";
    public static final String SKYTRAIN = "skytrain";
    public static final String WALK = "walk";
    public static final String POSITION_FOR_EDIT_JOURNEY = "pos";
    public static final String ICONID = "IconID";
    public static final String tJEmission = "JEmission";
    public static final String tJDist = "JDist";
    public static final String tNGasAmount = "NGasAmount";
    public static final String tNGasEmission = "NGasEmission";
    public static final String tElecAmount = "ElecAmount";
    public static final String tElecEmission = "ElecEmission";
    public static final String tLastUtil = "LastUtil";
    String tipString;
    int tipData;
    int properTipIndex;
    String[] tipArray;
    public static final String SETTING = "CarbonFootprintTrackerSettings";
    public static final String TREESETTING = "TreeSetting";
    boolean setting=false;
    JourneyCollection journeys = new JourneyCollection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_journey);
        getSetting();
        journeys = loadJourneys();
        tipArray = getResources().getStringArray(R.array.tips_array);
        setupAddJourneyButton();
        setJourneyList();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    private void getSetting() {
        SharedPreferences pref=getSharedPreferences(SETTING,MODE_PRIVATE);
        setting=pref.getBoolean(TREESETTING,false);
    }
    public JourneyCollection loadJourneys() {
        JourneyCollection temp_journeys = new JourneyCollection();
        SharedPreferences pref = getSharedPreferences(SHAREDPREF_SET, MODE_PRIVATE);
        int journeyAmt = pref.getInt(SHAREDPREF_ITEM_AMOUNTOFJOURNEYS, 0);
        for (int i = 0; i < journeyAmt; i++) {
            //Date date=new Date(pref.getLong(i+DATEOFTRAVEL,0));
            Journey j = new Journey(
                    pref.getString(i + ROUTENAME, ""),
                    Double.longBitsToDouble(pref.getLong(i + CITY, 0)),
                    Double.longBitsToDouble(pref.getLong(i + HIGHWAY, 0)),
                    pref.getString(i + NAME, ""),
                    pref.getString(i + GASTYPE, ""),
                    Double.longBitsToDouble(pref.getLong(i + MPGCITY, 0)),
                    Double.longBitsToDouble(pref.getLong(i + MPGHIGHWAY, 0)),
                    Double.longBitsToDouble(pref.getLong(i + LITERENGINE, 0)),
                    pref.getString(i + DATESTRING, ""),
                    pref.getBoolean(i + BUS, false),
                    pref.getBoolean(i + BIKE, false),
                    pref.getBoolean(i + SKYTRAIN, false),
                    pref.getBoolean(i + WALK, false),
                    pref.getInt(i + ICONID, 0)
            );
            temp_journeys.addJourney(j);
        }
        return temp_journeys;
    }
    private String dateToString(Date date) {
        SimpleDateFormat dateformatter = new SimpleDateFormat("MMMM dd, yyyy");
        return dateformatter.format(date);
    }
    private void saveJourneys() {
        SharedPreferences pref = getSharedPreferences(SHAREDPREF_SET, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        int journeyAmt = journeys.countJourneys();
        for (int i = 0; i < journeyAmt; i++) {
            editor.putString(i + NAME, journeys.getJourney(i).getName());
            editor.putString(i + ROUTENAME, journeys.getJourney(i).getRouteName());
            editor.putString(i + GASTYPE, journeys.getJourney(i).getGasType());
            editor.putLong(i + MPGCITY, Double.doubleToRawLongBits(journeys.getJourney(i).getMpgCity()));
            editor.putLong(i + MPGHIGHWAY, Double.doubleToRawLongBits(journeys.getJourney(i).getMpgHighway()));
            editor.putLong(i + CITY, Double.doubleToRawLongBits(journeys.getJourney(i).getCityDistance()));
            editor.putLong(i + HIGHWAY, Double.doubleToRawLongBits(journeys.getJourney(i).getHighwayDistance()));
            editor.putLong(i + LITERENGINE, Double.doubleToRawLongBits(journeys.getJourney(i).getLiterEngine()));
            editor.putString(i + DATESTRING, journeys.getJourney(i).getDateString());
            editor.putLong(i + TOTALEMISSIONS, Double.doubleToRawLongBits(journeys.getJourney(i).getTotalEmissions()));
            editor.putBoolean(i + BUS, journeys.getJourney(i).isBus());
            editor.putBoolean(i + BIKE, journeys.getJourney(i).isBike());
            editor.putBoolean(i + SKYTRAIN, journeys.getJourney(i).isSkytrain());
            editor.putBoolean(i + WALK, journeys.getJourney(i).isWalk());
            editor.putInt(i + ICONID, journeys.getJourney(i).getIconID());
        }
        editor.putInt(SHAREDPREF_ITEM_AMOUNTOFJOURNEYS, journeyAmt);
        editor.apply();
    }
    private void saveTips() {
        SharedPreferences kprefsave = getSharedPreferences(SHAREDPREF_SET3, MODE_PRIVATE);
        SharedPreferences.Editor editor = kprefsave.edit();
        TipHelperSingleton tipHelper = TipHelperSingleton.getInstance();
        editor.clear();
        editor.putLong(tJEmission, Double.doubleToRawLongBits(tipHelper.getJourneyEmission()));
        editor.putLong(tJDist, Double.doubleToRawLongBits(tipHelper.getJourneyDist()));
        editor.putLong(tNGasAmount, Double.doubleToRawLongBits(tipHelper.getnGasAmount()));
        editor.putLong(tNGasEmission, Double.doubleToRawLongBits(tipHelper.getnGasEmission()));
        editor.putLong(tElecAmount, Double.doubleToRawLongBits(tipHelper.getElecAmount()));
        editor.putLong(tElecEmission, Double.doubleToRawLongBits(tipHelper.getElecEmission()));
        editor.putString(tLastUtil, tipHelper.getLastUtil());
        editor.apply();
    }

    private void setupAddJourneyButton() {
        // Directs to "Select Car" Screen
        Button journey_button = (Button) findViewById(R.id.add_a_new_journey_button);
        journey_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SelectJourney2SelectCar = SelectCar.makeIntent(SelectJourney.this);
                startActivityForResult(SelectJourney2SelectCar, CAR_AND_ROUTE_SELECTED);
            }
        });
    }
    private void setJourneyList() {
        ArrayAdapter<Journey> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.journeyList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Opens a dialog with the calculation result of CO2 emission
                FragmentManager manager = getSupportFragmentManager();
                CalculationDialog dialog = new CalculationDialog();
                Bundle bundle = new Bundle();
                bundle.putBoolean(TREESETTING,setting);
                bundle.putDouble("CO2", journeys.getJourney(position).getTotalEmissions());
                dialog.setArguments(bundle);
                dialog.show(manager, "CalculateDialog");
            }
        });
        registerForContextMenu(list);
    }
    private class MyListAdapter extends ArrayAdapter<Journey> {
        public MyListAdapter() {
            super(SelectJourney.this, R.layout.layout_for_carlist_with_icons, journeys.returnJourneyList());
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.layout_for_carlist_with_icons, parent, false);
            }
            // Find the journey to work with
            Journey currentJourney = journeys.getJourney(position);
            Log.i("TEST", "currentJourney = " + currentJourney.getName());
            // Set icon and info
            ImageView iconView = (ImageView) itemView.findViewById(R.id.icon_imageView);
            iconView.setImageResource(currentJourney.getIconID());
            // Custom setting for Journey, car name = route name
            TextView carName = (TextView) itemView.findViewById(R.id.carlist_name);
            carName.setText(currentJourney.getRouteName());
            // Custom setting for Journey, car name = total distance travelled
            TextView carMake = (TextView) itemView.findViewById(R.id.carlist_make);
            carMake.setText(currentJourney.getTotalDistanceToString());
            if ( currentJourney.isBus() || currentJourney.isWalk() || currentJourney.isBike() || currentJourney.isSkytrain()){
                // Custom setting for Journey, carModel = gas type
                TextView carModel = (TextView) itemView.findViewById(R.id.carlist_model);
                carModel.setText(currentJourney.getName());
            }else {
                // Custom setting for Journey, carModel = gas type
                TextView carModel = (TextView) itemView.findViewById(R.id.carlist_model);
                carModel.setText(currentJourney.getGasType());
            }
            // Custom setting for Journey, car year = journey date
            TextView carYear = (TextView) itemView.findViewById(R.id.carlist_year);
            carYear.setText(currentJourney.getDateString());
            return itemView;
        }
    }
    //Context Menu Code taken and modified from:
    //https://www.mikeplate.com/2010/01/21/show-a-context-menu-for-long-clicks-in-an-android-listview/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.journeyList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            if (journeys.getJourney(info.position).isBike()) {
                menu.setHeaderTitle("Transportation method: " + "Bike" + "\n" + "Route Name: " + journeys.getJourney(info.position).getRouteName());
            } else if (journeys.getJourney(info.position).isBus()) {
                menu.setHeaderTitle("Transportation method: " + "Bus" + "\n" + "Route Name: " + journeys.getJourney(info.position).getRouteName());
            } else if (journeys.getJourney(info.position).isSkytrain()) {
                menu.setHeaderTitle("Transportation method: " + "Skytrain" + "\n" + "Route Name: " + journeys.getJourney(info.position).getRouteName());
            } else if (journeys.getJourney(info.position).isWalk()) {
                menu.setHeaderTitle("Transportation method: " + "Walk" + "\n" + "Route Name: " + journeys.getJourney(info.position).getRouteName());
            } else {
                menu.setHeaderTitle("Transportation method: Car" + "\n" + "Route Name: " + journeys.getJourney(info.position).getRouteName());
            }
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
        int pos = info.position;
        if (menuItemName.equals("Edit")) {
            Intent i = SelectCar.makeIntent(SelectJourney.this);
            i.putExtra(POSITION_FOR_EDIT_JOURNEY, pos);
            startActivityForResult(i, EDIT_JOURNEY);
        } else if (menuItemName.equals("Delete")) {
            Journey j = journeys.getJourney(pos);
            Toast.makeText(SelectJourney.this, "Removed Journey " + j.toString(), Toast.LENGTH_SHORT).show();
            journeys.deleteJourney(pos);
            setJourneyList();
        }
        return true;
    }
    //Creates an Alert Dialog using a the custom layout activity_tip_dialog
    //Clicking next tip will create a new Alert Dialog with a new message
    //The message is set by running the tipTextSelector method, which avoids repeats and selects
    //a relevant tip (if available)
    private void tipMaker() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View tipView = inflater.inflate(R.layout.activity_tip_dialog, null);
        TextView tipText = (TextView) tipView.findViewById(R.id.tip_text);
        tipText.setGravity(Gravity.CENTER);
        tipText.setText(tipTextSelector());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tips!");
        builder.setView(tipView);
        builder.setPositiveButton("Next Tip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tipMaker();
            }
        });
        builder.setNegativeButton("Ok", null);
        AlertDialog tipDialog = builder.create();
        tipDialog.show();
    }
    //Avoids tips that have been shown in the last 7
    //Picks relevant tips, using userdata
    private String tipTextSelector() {
        TipHelperSingleton tipHelper = TipHelperSingleton.getInstance();
        SharedPreferences kpref = getSharedPreferences(SHAREDPREF_SET, MODE_PRIVATE);
        int journeyNum = kpref.getInt(SHAREDPREF_ITEM_AMOUNTOFJOURNEYS, 0);
        SharedPreferences kpref2 = getSharedPreferences(SHAREDPREF_SET2, MODE_PRIVATE);
        int utilityNum = kpref2.getInt(SHAREDPREF_ITEM_AMOUNTOFUTILITIES, 0);
        getSetting();
        tipHelper.setTipIndexTravel();
        if (utilityNum > 0) {
            if (tipHelper.spiceTimer() == 1) {
                if (tipHelper.getLastUtil().equals("Natural Gas")) {
                    tipHelper.setTipIndexUtil();
                }
                if (tipHelper.getLastUtil().equals("Electricity")) {
                    tipHelper.setTipIndexElec();
                }
                properTipIndex = tipHelper.checkRepeatTracker(tipHelper.getTipIndex());
                tipData = tipHelper.tipDataFetcher(properTipIndex);
                if (setting) {
                    tipData = tipData*2;
                }
                tipString = String.format(tipArray[properTipIndex], tipData);
                return tipString;
            }
        }
        properTipIndex = tipHelper.checkRepeatTracker(tipHelper.getTipIndex());
        Journey jTip = journeys.getJourney(journeyNum-1);
        tipHelper.setJourneyEmission(jTip.getTotalEmissions());
        tipHelper.setJourneyDist(jTip.getCityDistance() + jTip.getHighwayDistance());
        tipData = tipHelper.tipDataFetcher(properTipIndex);
        if (setting) {
            tipData = tipData*2;
        }
        tipString = String.format(tipArray[properTipIndex], tipData);
        saveTips();
        return tipString;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAR_AND_ROUTE_SELECTED:
                if (resultCode == RESULT_OK) {
                    DateSingleton finalDate = DateSingleton.getInstance();
                    RouteSingleton finalRoute = RouteSingleton.getInstance();
                    CarSingleton finalCar = CarSingleton.getInstance();
                    Journey finalJourney = new Journey(
                            finalRoute.getRouteName(),
                            finalRoute.getCityDistance(),
                            finalRoute.getHighwayDistance(),
                            finalCar.getName(), finalCar.getGasType(),
                            finalCar.getCityEmissions(), finalCar.getHighwayEmissions(),
                            finalCar.getLiterEngine(), finalDate.getDateString(),
                            finalCar.getBus(),
                            finalCar.getBike(),
                            finalCar.getSkytrain(),
                            finalCar.getWalk(),
                            finalCar.getIconID()
                    );
                    //finalJourney.setTotalEmissions(finalJourney.CalculateTotalEmissions());
                    journeys.addJourney(finalJourney);
                    setupAddJourneyButton();
                    setJourneyList();
                    saveJourneys();
                    tipMaker();
                } else {
                    Log.i(TAG, "User Cancelled");
                    setupAddJourneyButton();
                    setJourneyList();
                }
                break;
            case EDIT_JOURNEY:
                if (resultCode == RESULT_OK) {
                    int pos = data.getIntExtra(POSITION_FOR_EDIT_JOURNEY, 0);
                    Journey j = journeys.getJourney(pos);
                    DateSingleton finalDate = DateSingleton.getInstance();
                    RouteSingleton finalRoute = RouteSingleton.getInstance();
                    CarSingleton finalCar = CarSingleton.getInstance();
                    j.setDateString(finalDate.getDateString());
                    j.setRouteName(finalRoute.getRouteName());
                    j.setCityDistance(finalRoute.getCityDistance());
                    j.setHighwayDistance(finalRoute.getHighwayDistance());
                    j.setName(finalCar.getName());
                    j.setGasType(finalCar.getGasType());
                    j.setMpgCity(finalCar.getCityEmissions());
                    j.setMpgHighway(finalCar.getHighwayEmissions());
                    j.setLiterEngine(finalCar.getLiterEngine());
                    j.setBike(finalCar.getBike());
                    j.setBus(finalCar.getBus());
                    j.setSkytrain(finalCar.getSkytrain());
                    j.setWalk(finalCar.getWalk());
                    j.setIconID(finalCar.getIconID());
                    j.setTotalEmissions(j.CalculateTotalEmissions());
                    setupAddJourneyButton();
                    setJourneyList();
                    saveJourneys();
                } else {
                    setupAddJourneyButton();
                    setJourneyList();
                }
                break;
        }
    }
    public void onBackPressed() {
        saveJourneys();
        finish();
    }
    public static Intent makeIntent(Context context) {
        return new Intent(context, SelectJourney.class);
    }
}
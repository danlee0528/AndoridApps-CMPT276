package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

/**
 * This Class stores a list of utilities for user to see. Can add, edit and delete saved utilities.
 * Includes error checking of input, and user can go back to main menu.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.Journey;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.JourneyCollection;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.TipHelperSingleton;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.UtilitiesCollection;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.Utility;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.UtilitySingleton;

public class SelectUtilities extends AppCompatActivity {

    public static final int ADD_UTILITY = 1;
    private static final String TAG = "CarbonFootprintTracker";
    private static final String SHAREDPREF_SET = "CarbonFootprintTrackerUtilities";
    private static final String SHAREDPREF_SET2 = "CarbonFootprintTrackerJournies";
    private static final String SHAREDPREF_SET3 = "CarbonFootprintTrackerTips";
    private static final String SHAREDPREF_ITEM_AMOUNTOFJOURNEYS = "AmountOfJourneys";
    private static final String SHAREDPREF_ITEM_AMOUNTOFUTILITIES = "AmountOfUtilities";
    private static final String NAME = "name";
    private static final String GASTYPE = "gasType";
    private static final String AMOUNT = "amount";
    private static final String NUMPEOPLE = "numofPeople";
    private static final String STARTDATE = "startDate";
    private static final String ENDDATE = "endDate";
    private static final String EMISSION = "emission";
    public static final int EDIT_UTILITY = 4;
    public static final String POS_TO_EDIT = "POS";

    UtilitiesCollection utilities = new UtilitiesCollection();
    Intent intent;

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
    boolean setting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_utilities);
        getSetting();
        utilities = loadUtilities();
        setupList();
        tipArray = getResources().getStringArray(R.array.tips_array);
        setupButtons();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        saveUtilities();
        finish();
        return true;
    }

    private void getSetting() {
        SharedPreferences pref = getSharedPreferences(SETTING, MODE_PRIVATE);
        setting = pref.getBoolean(TREESETTING, false);
    }

    private UtilitiesCollection loadUtilities() {
        UtilitiesCollection utils = new UtilitiesCollection();
        SharedPreferences pref = getSharedPreferences(SHAREDPREF_SET, MODE_PRIVATE);
        int utilityAmt = pref.getInt(SHAREDPREF_ITEM_AMOUNTOFUTILITIES, 0);
        for (int i = 0; i < utilityAmt; i++) {
            Utility newUtil = new Utility(pref.getString(i + NAME, ""),
                    pref.getString(i + GASTYPE, ""), Double.longBitsToDouble(pref.getLong(i + AMOUNT, 0)), pref.getInt(i + NUMPEOPLE, 0),
                    Double.longBitsToDouble(pref.getLong(i + EMISSION, 0)), pref.getString(i + STARTDATE, ""), pref.getString(i + ENDDATE, ""));
            utils.addUtility(newUtil);
        }
        return utils;
    }

    private void saveUtilities() {
        SharedPreferences pref = getSharedPreferences(SHAREDPREF_SET, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        int utilityAmt = utilities.countUtility();
        for (int i = 0; i < utilityAmt; i++) {
            editor.putString(i + NAME, utilities.getUtility(i).getName());
            editor.putString(i + GASTYPE, utilities.getUtility(i).getGasType());
            editor.putString(i + STARTDATE, utilities.getUtility(i).getStartDate());
            editor.putString(i + ENDDATE, utilities.getUtility(i).getEndDate());
            editor.putInt(i + NUMPEOPLE, utilities.getUtility(i).getNumofPeople());
            editor.putLong(i + EMISSION, Double.doubleToRawLongBits(utilities.getUtility(i).getEmission()));
            editor.putLong(i + AMOUNT, Double.doubleToRawLongBits(utilities.getUtility(i).getAmount()));
        }
        editor.putInt(SHAREDPREF_ITEM_AMOUNTOFUTILITIES, utilityAmt);
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

    private void setupList() {

        final ListView utilityList = (ListView) findViewById(R.id.utilities_listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_for_list, utilities.getUtilitiesDescriptionsWithName());
        utilityList.setAdapter(adapter);
        utilityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager manager = getSupportFragmentManager();
                CalculationDialog dialog = new CalculationDialog();
                Bundle bundle = new Bundle();
                bundle.putBoolean("treeSetting", setting);
                bundle.putDouble("CO2", utilities.getUtility(position).getEmission());
                dialog.setArguments(bundle);
                dialog.show(manager, "CalculateDialog");
            }
        });

        registerForContextMenu(utilityList);
    }

    //Context Menu Code taken and modified from:
    //https://www.mikeplate.com/2010/01/21/show-a-context-menu-for-long-clicks-in-an-android-listview/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.utilities_listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            menu.setHeaderTitle("Type: " + utilities.getUtility(info.position).getGasType() + "\n" +
                    "Period: " + utilities.getUtility(info.position).getStartDate() + " - " + utilities.getUtility(info.position).getEndDate());


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
            Intent i = AddUtility.makeIntent(SelectUtilities.this);
            i.putExtra(POS_TO_EDIT, pos);
            startActivityForResult(i, EDIT_UTILITY);
        } else if (menuItemName.equals("Delete")) {
            Utility u = utilities.getUtility(pos);
            Toast.makeText(SelectUtilities.this, "Removed Utility " + u.getName(), Toast.LENGTH_SHORT).show();
            utilities.deleteUtility(pos);
            setupList();
        }
        return true;
    }

    private void setupButtons() {
        ImageButton addButton = (ImageButton) findViewById(R.id.add_utility);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Utilities2Add = AddUtility.makeIntent(SelectUtilities.this);
                startActivityForResult(Utilities2Add, ADD_UTILITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_UTILITY:
                if (resultCode == RESULT_OK) {
                    UtilitySingleton utility2Load = UtilitySingleton.getInstance();
                    Utility temp_utility = new Utility(
                            utility2Load.getName(),
                            utility2Load.getGasType(),
                            utility2Load.getAmounts(),
                            utility2Load.getNum_poeople(),
                            utility2Load.getEmission(),
                            utility2Load.getStartDate(),
                            utility2Load.getEndDate());
                    utilities.addUtility(temp_utility);
                    setupButtons();
                    saveUtilities();
                    setupList();
                    saveUtilities();
                    tipMaker();
                } else {
                    setupButtons();
                    setupList();
                }
                break;
            case EDIT_UTILITY:
                if (resultCode == RESULT_OK) {
                    int pos = data.getIntExtra(POS_TO_EDIT, 0);
                    Utility utilToEdit = utilities.getUtility(pos);
                    UtilitySingleton utili = UtilitySingleton.getInstance();
                    utilToEdit.setNumofPeople(utili.getNum_poeople());
                    utilToEdit.setAmount(utili.getAmounts());
                    utilToEdit.setGasType(utili.getGasType());
                    utilToEdit.setName(utili.getName());
                    utilToEdit.setEmission(utili.getEmission());
                    utilToEdit.setStartDate(utili.getStartDate());
                    utilToEdit.setEndDate(utili.getEndDate());
                    saveUtilities();
                    setupList();
                    setupButtons();
                    saveUtilities();
                }
                break;
        }
    }

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
        UtilitySingleton utilHelper = UtilitySingleton.getInstance();
        getSetting();
        if (utilHelper.getGasType().equals("Natural Gas")) {
            tipHelper.setTipIndexUtil();
            if (tipHelper.spiceTimerUtility() == 1) {
                SharedPreferences kpref = getSharedPreferences(SHAREDPREF_SET2, MODE_PRIVATE);
                int journeyNum = kpref.getInt(SHAREDPREF_ITEM_AMOUNTOFJOURNEYS, 0);
                Log.i("nojourneys", "whelp" + journeyNum);
                if (journeyNum > 0) {
                    tipHelper.setTipIndexTravel();
                    properTipIndex = tipHelper.checkRepeatTracker(tipHelper.getTipIndex());
                    tipHelper.tipDataFetcher(properTipIndex);
                    tipString = String.format(tipArray[properTipIndex], tipData);
                    return tipString;
                } else {
                    tipString = getString(R.string.noTips);
                    return tipString;
                }
            }
            properTipIndex = tipHelper.checkRepeatTracker(tipHelper.getTipIndex());
            tipHelper.setnGasAmount(utilHelper.getAmounts());
            tipHelper.setnGasEmission(utilHelper.getEmission());
            tipData = tipHelper.tipDataFetcher(properTipIndex);
            if (setting) {
                tipData = tipData * 2;
            }
            tipString = String.format(tipArray[properTipIndex], tipData);
        }

        if (utilHelper.getGasType().equals("Electricity")) {
            tipHelper.setTipIndexElec();
            if (tipHelper.spiceTimerElectric() == 1) {
                SharedPreferences kpref = getSharedPreferences(SHAREDPREF_SET2, MODE_PRIVATE);
                int journeyNum = kpref.getInt(SHAREDPREF_ITEM_AMOUNTOFJOURNEYS, 0);
                Log.i("nojourneys", "whelp" + journeyNum);
                if (journeyNum > 0) {
                    tipHelper.setTipIndexTravel();
                    properTipIndex = tipHelper.checkRepeatTracker(tipHelper.getTipIndex());
                    tipData = tipHelper.tipDataFetcher(properTipIndex);
                    tipString = String.format(tipArray[properTipIndex], tipData);
                    return tipString;
                } else {
                    tipString = getString(R.string.noTips);
                    return tipString;
                }
            }
            properTipIndex = tipHelper.checkRepeatTracker(tipHelper.getTipIndex());
            tipHelper.setElecAmount(utilHelper.getAmounts());
            tipHelper.setElecEmission(utilHelper.getEmission());
            tipData = tipHelper.tipDataFetcher(properTipIndex);
            if (setting) {
                tipData = tipData * 2;
            }
            tipString = String.format(tipArray[properTipIndex], tipData);
        }

        tipHelper.setLastUtil(utilHelper.getGasType());
        saveTips();

        return tipString;
    }

    @Override
    public void onBackPressed() {
        saveUtilities();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, SelectUtilities.class);
    }
}
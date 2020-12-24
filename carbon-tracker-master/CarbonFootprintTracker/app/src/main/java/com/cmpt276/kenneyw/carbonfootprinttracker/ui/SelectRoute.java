package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

/**
 * This Class stores a list of routes for perusal in a journey. Can add, edit and delete saved routes.
 * Includes error checking of input, and user can go back to select car screen. Saving routes via shared prefs and
 * transferring to journey to save via singleton class: routeSingleton
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.Route;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.RouteCollection;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.RouteSingleton;

public class SelectRoute extends AppCompatActivity {
    private static final String TAG = "CarbonFootprintTracker";
    public static final String NAME = "name";
    public static final String CITY = "city";
    public static final String HIGHWAY = "highway";
    public static final int REQUEST_ADD_ROUTE = 1;
    public static final int DATE_REQUESTED = 2;
    private static final String SHAREDPREF_SET = "CarbonFootprintTrackerRoutes";
    private static final String SHAREDPREF_ITEM_AMOUNTOFROUTES = "AmountOfRoutes";
    RouteCollection routes = new RouteCollection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routes = LoadRoutes();
        setContentView(R.layout.activity_select_route);
        setUpListView();
        setupAddRouteButton();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        saveRoutes();
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
        return true;
    }

    private void setupAddRouteButton() {
        Button btn = (Button) findViewById(R.id.btn_add_route);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddRoute.makeIntent(SelectRoute.this);
                startActivityForResult(intent, REQUEST_ADD_ROUTE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_ROUTE:
                if (resultCode == RESULT_CANCELED) {
                    Log.i(TAG, "User Cancelled Add Route");
                    break;
                } else {
                    String nameToAdd = data.getStringExtra(NAME);
                    double cityToAdd = data.getDoubleExtra(CITY, 0);
                    double highwayToAdd = data.getDoubleExtra(HIGHWAY, 0);
                    Route r = new Route(nameToAdd, cityToAdd, highwayToAdd);
                    routes.addRoute(r);
                    saveRoutes();
                    setUpListView();
                    break;
                }
            case DATE_REQUESTED:
                if (resultCode == RESULT_CANCELED) {
                    Log.i(TAG, "User Cancelled Date picker");
                    break;
                } else {
                    Intent i = new Intent();
                    setResult(RESULT_OK, i);
                    finish();
                    break;
                }
        }
    }

    private RouteCollection LoadRoutes() {
        RouteCollection temp_routes = new RouteCollection();
        SharedPreferences pref = getSharedPreferences(SHAREDPREF_SET, MODE_PRIVATE);
        int routeAmt = pref.getInt(SHAREDPREF_ITEM_AMOUNTOFROUTES, 0);
        String routeName;
        double routeCity;
        double routeHighway;
        for (int i = 0; i < routeAmt; i++) {
            routeCity = Double.longBitsToDouble(pref.getLong(i + CITY, 0));
            routeHighway = Double.longBitsToDouble(pref.getLong(i + HIGHWAY, 0));
            routeName = pref.getString(i + NAME, "No Name");
            Route newRoute = new Route(routeName, routeCity, routeHighway);
            temp_routes.addRoute(newRoute);
        }
        return temp_routes;
    }

    public void setUpListView() {
        ListView listForRoutes = (ListView) findViewById(R.id.listViewRoutes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_for_list, routes.getRoutesDescriptionsWithName());
        listForRoutes.setAdapter(adapter);
        listForRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, routes.getRoute(position).getRouteName() + " selected.");
                Route r = routes.getRoute(position);
                String nameToPass = r.getRouteName();
                double cityToPass = r.getCityDistance();
                double highwayToPass = r.getHighwayDistance();
                RouteSingleton routeselected = RouteSingleton.getInstance();
                routeselected.setRouteName(nameToPass);
                routeselected.setCityDistance(cityToPass);
                routeselected.setHighwayDistance(highwayToPass);
                saveRoutes();
                // Call Calendar Activity
                Intent SelectRoute2EditDate = EditDate.makeIntent(SelectRoute.this);
                startActivityForResult(SelectRoute2EditDate, DATE_REQUESTED);
            }
        });
        registerForContextMenu(listForRoutes);
    }

    //Context Menu Code taken and modified from:
    //https://www.mikeplate.com/2010/01/21/show-a-context-menu-for-long-clicks-in-an-android-listview/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listViewRoutes) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(routes.getRoute(info.position).getRouteName());
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
            launchEditFragment(pos);
        } else if (menuItemName.equals("Delete")) {
            Route r = routes.getRoute(pos);
            Toast.makeText(SelectRoute.this, "Removed Route " + r.getRouteName(), Toast.LENGTH_SHORT).show();
            routes.deleteRoute(pos);
            setUpListView();
        }
        return true;
    }

    private void launchEditFragment(int pos) {
        Route r = routes.getRoute(pos);
        Toast.makeText(SelectRoute.this, "Enter new values for Route " + r.getRouteName(), Toast.LENGTH_SHORT).show();
        String nameToEdit = r.getRouteName();
        double cityToEdit = r.getCityDistance();
        double highwayToEdit = r.getHighwayDistance();
        Bundle bundle = new Bundle();
        bundle.putString("name", nameToEdit);
        bundle.putDouble("city", cityToEdit);
        bundle.putDouble("highway", highwayToEdit);
        bundle.putInt("pos", pos);
        FragmentManager manager = getSupportFragmentManager();
        EditRouteFragment dialog = new EditRouteFragment();
        dialog.setArguments(bundle);
        dialog.show(manager, "EditRouteDialog");
        Log.i(TAG, "Launched Dialog Fragment");
    }

    public void saveRoutes() {
        SharedPreferences pref = getSharedPreferences(SHAREDPREF_SET, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        int routeAmt = routes.countRoutes();
        for (int i = 0; i < routeAmt; i++) {
            editor.putString(i + NAME, routes.getRoute(i).getRouteName());
            editor.putLong(i + CITY, Double.doubleToRawLongBits(routes.getRoute(i).getCityDistance()));
            editor.putLong(i + HIGHWAY, Double.doubleToRawLongBits(routes.getRoute(i).getHighwayDistance()));
        }
        editor.putInt(SHAREDPREF_ITEM_AMOUNTOFROUTES, routeAmt);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        saveRoutes();
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, SelectRoute.class);
    }

    public void changeRoute(int pos, String name, double city, double highway) {
        Log.i(TAG, "change Route got: " + name + " " + city + " " + highway);
        routes.getRoute(pos).setRouteName(name);
        routes.getRoute(pos).setCityDistance(city);
        routes.getRoute(pos).setHighwayDistance(highway);
    }
}
package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

/**
 * The Fragment used to edit a route. Uses the RouteSingleton to return the values entered by the
 * user to edit the route they've selected, and returns an intent to SelectRoute for it to detect
 * how to respond in the case of a cancelled or successful edit.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;

public class EditRouteFragment extends AppCompatDialogFragment {
    private static final String TAG = "CarbonFootprintTracker";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final int pos = getArguments().getInt("pos");

        //create view
        View viewer = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_edit_route, null);

        final EditText editName = (EditText) viewer.findViewById(R.id.editName);
        final EditText editCity = (EditText) viewer.findViewById(R.id.editCity);
        final EditText editHighway = (EditText) viewer.findViewById(R.id.editHighway);

        String nameToEdit = getArguments().getString("name");
        double cityToEdit = getArguments().getDouble("city");
        double highwayToEdit = getArguments().getDouble("highway");

        editName.setText(nameToEdit);
        editCity.setText("" + cityToEdit);
        editHighway.setText("" + highwayToEdit);
        //create button listeners
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        String name = editName.getText().toString();
                        String city = editCity.getText().toString();
                        String highway = editHighway.getText().toString();

                        if (name.equals("")) {
                            Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                        } else if (city.equals("")) {
                            if (highway.equals("")) {
                                Toast.makeText(getActivity(), "Both Length fields cannot be empty", Toast.LENGTH_SHORT).show();
                            } else if (Double.parseDouble(highway) < 0) {
                                Toast.makeText(getActivity(), "Highway Length cannot be negative", Toast.LENGTH_SHORT).show();
                            } else {
                                city = "0";
                                ((SelectRoute) getActivity()).changeRoute(pos, name, Double.parseDouble(city), Double.parseDouble(highway));
                                ((SelectRoute) getActivity()).setUpListView();
                            }
                        } else if (highway.equals("")) {
                            if (Double.parseDouble(city) < 0) {
                                Toast.makeText(getActivity(), "City Length cannot be negative", Toast.LENGTH_SHORT).show();
                            } else {
                                highway = "0";
                                ((SelectRoute) getActivity()).changeRoute(pos, name, Double.parseDouble(city), Double.parseDouble(highway));
                                ((SelectRoute) getActivity()).setUpListView();
                            }
                        } else if (Double.parseDouble(city) < 0 && Double.parseDouble(highway) < 0) {
                            Toast.makeText(getActivity(), "Length cannot be negative", Toast.LENGTH_SHORT).show();
                        } else {
                            ((SelectRoute) getActivity()).changeRoute(pos, name, Double.parseDouble(city), Double.parseDouble(highway));
                            ((SelectRoute) getActivity()).saveRoutes();
                            ((SelectRoute) getActivity()).setUpListView();
                        }
                        Log.i(TAG, "" + name + " " + city + " " + highway);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        //build alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("Edit Route")
                .setView(viewer)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .create();
    }
}

package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

/**
 * This class displays the calculation result of CO2 emission of a selected journey
 * Units must be in Time taken for 10 Trees to absorb the KG of CO2 or in KG
 * 13 kg CO2 absorbed by 1 tree in 1 year,
 * Time taken for 1 tree to absorb 1kg CO2 = 28 days
 * Time taken for 10 trees to absorb 1kg CO2 = 2.8 days
 * total time taken = kg CO2 * 2.8 Days
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;

import org.w3c.dom.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CalculationDialog extends AppCompatDialogFragment {

    public static final double TIMEFOR100TREES = 2.8;
    public static final String TREESETTING = "TreeSetting";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //create view
        View viewer = LayoutInflater.from(getActivity()).inflate(R.layout.activity_calculation_dialog, null);
        TextView txtCO2 = (TextView) viewer.findViewById(R.id.calculation_result_dialog);
        Double CO2 = getArguments().getDouble("CO2");
        boolean setting = getArguments().getBoolean(TREESETTING, false);
        if (setting) {
            txtCO2.setText("It will take " + CO2 * TIMEFOR100TREES + " days for 100 trees to absorb");
        } else {
            txtCO2.setText("    " + CO2 + getString(R.string.kgco2));
        }

        txtCO2.setTextSize(20);
        txtCO2.setTextColor(Color.rgb(255, 178, 102));
        //create button listeners
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                }
            }
        };

        //build alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.co2emissionresult)
                .setView(viewer)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }

}
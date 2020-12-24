package com.example.daniellee.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class HelpAcitivty extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Create the view to show
        View v= LayoutInflater.from(getActivity()).inflate(R.layout.activity_help_acitivty,null);

        // Creat ea button Listner
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("MyApp", "You clicked the dialog button. ");
            }
        };


        // Build the alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("HELP")
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }
}


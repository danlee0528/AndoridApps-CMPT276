package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;

import org.w3c.dom.Text;

/**
*  This class creates "About" dialog which showing the following:
*  the group’s name (like “Orange”, or “Brass”),
*  year the app was created,
*  a version number (like 1.1, pulled from a resource file),
*  link to SFU CS home page, and
*  links to all sources for your images, icons, and other resources.
*  optionally list each group member’s name (but no private data like student number or email address)
*/

public class AboutFragment extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //create view
        View viewer = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_about, null);

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
                .setTitle("About")
                .setView(viewer)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }
}
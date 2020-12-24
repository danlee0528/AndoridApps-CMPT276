package com.example.daniellee.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ahad on 2017-02-22.
 */

public class victoryFragment extends AppCompatDialogFragment {

    private final GameConfiguration gameConfig = GameConfiguration.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create the view to show

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.victory_dialog, null);
        updateTextViews(v);
        // Create a button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()    {
            @Override
            public void onClick(DialogInterface dialog, int which)  {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        getActivity().finish();
                        break;
                }
                Log.i("TAG","YOU CLICKED THE DIALOG BUTTON");
                Toast.makeText(getActivity(),getString(R.string.winToastMessage),Toast.LENGTH_SHORT).show();
            }
        };
        // Build alert Dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.congratulationsDialog)
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }

    private void updateTextViews(View v) {
        // Updates text views in the dialog with Users Score
        TextView tvCurrent = (TextView) v.findViewById(R.id.scansScore);
        TextView tvHiScore= (TextView) v.findViewById(R.id.hiScoreText);
        TextView tvGamePlayed = (TextView) v.findViewById(R.id.gamesPlayedText);

        String textCurrent = tvCurrent.getText().toString();
        String textHiScore = tvHiScore.getText().toString();
        String textGamePlayed = tvGamePlayed.getText().toString();

        tvCurrent.setText(textCurrent + gameConfig.getScans());
        tvHiScore.setText(textHiScore + gameConfig.getUserHiScore());
        tvGamePlayed.setText(textGamePlayed + gameConfig.getTotalGamesPlayed());
    }

}

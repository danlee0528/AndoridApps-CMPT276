package com.example.daniellee.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// Array of options ---> ArrayAdapter ---> ListView
// List view: {view: items.xml}

public class PotListActivity extends AppCompatActivity {;

    public static final int REQUEST_CODE_MESSAGE = 111;
    public static final String SEND_POT_NAME = "Pot Name ";
    public static final String SEND_POT_WEIGHT = "Pot Weight";
    public static Pot clickedPot;
    PotCollection myPots = new PotCollection();
    private String potInfo = "Pot Info";
    ArrayAdapter<String> adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populatePotList();
        populateListView();
        setupAddPotButton();
        registerClickCallback();

    }

    private void populatePotList() {
        myPots.addPot(new Pot("Big Fry Pan", 206)); // Default Pots
        myPots.addPot(new Pot("Huge Pot",1002)); // Default Pots
    }

    //Set an arrayAdapter for the PotList and fill the view
    private void populateListView() {
        adapter = new MyListAdapter();
        list = (ListView) findViewById(R.id.ListMain);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<String>{
        public MyListAdapter(){
            super(PotListActivity.this, R.layout.items, myPots.getPotDescriptions());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.items, parent, false);

            // Fill the view - Pot Info
            TextView potInfoText = (TextView) itemView.findViewById(R.id.potinfo);
            potInfoText.setText(myPots.getPotDescriptionsWithIndex(position));

            return itemView;
        }
    }

    /* FOR ADD POT ACTIVITY */
    // When ADD POT button is invoked, popluate ADD POT acitivity
    private void setupAddPotButton() {
        Button addPotButton = (Button) findViewById(R.id.add_pot_button);
        addPotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launche the AddPotActivity
                Intent intent4AddPotActivity = new Intent(getApplicationContext(), AddPotActivity.class);
                startActivityForResult(intent4AddPotActivity, REQUEST_CODE_MESSAGE);
            }
        });
    }

    /* This function is shared by ADD POT ACTIVITY & CALCULATE ACTIVITY*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_CODE_MESSAGE:
                if (resultCode == Activity.RESULT_OK){
                    /* FOR ADD POT ACITIVITY*/
                    Pot newPot = AddPotActivity.getPotFromIntent(data);// Get the newly added pot from AddPotActivity
                    myPots.addPot(newPot); // add the new pot to the Pot Collection
                    populateListView(); // update the list
                    Log.i("MyApp", "Result Pot Name is " + newPot.getName());// Do something with it
                    Log.i("MyApp", "Result Pot Weight is " + newPot.getWeightInG());// Do something with it
                }
                else{
                    Log.i("MyApp", "Activity cancelled");
                }
        }
    }

    /* FOR CALCULATE ACTIVITY*/
    // When an item is clicked, pop up the Caculate Acitivity
    private void registerClickCallback() {
        list = (ListView) findViewById(R.id.ListMain);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                clickedPot = myPots.getPot(position);
                String message = "You Clicked position " + position
                        + " Which is " + clickedPot.getName() + " with " + clickedPot.getWeightInG() + " g";
                Toast.makeText(PotListActivity.this, message, Toast.LENGTH_LONG).show();;
                Log.i("MyApp", "Result Context is " + myPots.getPotDescriptionsWithIndex(position));

                Intent intent4CalculateActivity = CalculateActivity.makeLaunchIntent(PotListActivity.this, clickedPot);
                startActivityForResult(intent4CalculateActivity, REQUEST_CODE_MESSAGE);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Remove: " + myPots.getPotDescriptionsWithIndex(position), Toast.LENGTH_SHORT).show();
                // remove selected item from list view

                return true;
            }
        });
    }


}

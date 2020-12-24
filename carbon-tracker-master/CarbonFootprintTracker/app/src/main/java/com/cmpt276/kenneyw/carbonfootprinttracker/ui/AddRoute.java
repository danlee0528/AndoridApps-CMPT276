package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

/**
 *  AddRoute saves cityKM, highwayKM and name of a route input by the user
 *  Also handles editing routes. Passes saved routes via singleton class: RouteSingleton
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;

public class AddRoute extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        setupOKButton();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent=new Intent();
        setResult(RESULT_CANCELED,intent);
        finish();
        return true;
    }

    private void setupOKButton() {
        ImageButton okBtn=(ImageButton)findViewById(R.id.btn_ok_addroute);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editName=(EditText)findViewById(R.id.editText_name_addroute);
                EditText editCity=(EditText)findViewById(R.id.editText_city_addroute);
                EditText editHighway=(EditText)findViewById(R.id.editText_highway_addroute);

                String nameToAdd=editName.getText().toString();
                String cityToAdd=editCity.getText().toString();
                String highwayToAdd=editHighway.getText().toString();

                if(nameToAdd.equals("")){
                    Toast.makeText(AddRoute.this,"Name cannot be empty",Toast.LENGTH_SHORT).show();
                }

                else if(cityToAdd.equals("")) {
                    if(highwayToAdd.equals("")) {
                        Toast.makeText(AddRoute.this, "Both Length fields cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    else if(Double.parseDouble(highwayToAdd)<0){
                        Toast.makeText(AddRoute.this,"Highway Length cannot be negative",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent intent=new Intent();
                        editCity.setText("0");
                        String cit=editCity.getText().toString();
                        intent.putExtra("name",nameToAdd);
                        intent.putExtra("city",Double.parseDouble(cit));
                        intent.putExtra("highway",Double.parseDouble(highwayToAdd));
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }
                else if(highwayToAdd.equals("")){
                    if(cityToAdd.equals("")){
                        Toast.makeText(AddRoute.this, "Both Length fields cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    else if( Double.parseDouble(cityToAdd)<0) {
                        Toast.makeText(AddRoute.this, "City Length cannot be negative", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent intent=new Intent();
                        editHighway.setText("0");
                        String high=editHighway.getText().toString();
                        intent.putExtra("name",nameToAdd);
                        intent.putExtra("city",Double.parseDouble(cityToAdd));
                        intent.putExtra("highway",Double.parseDouble(high));
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }
                else if(Double.parseDouble(cityToAdd)<0&&Double.parseDouble(highwayToAdd)<0){
                    Toast.makeText(AddRoute.this,"Length cannot be negative",Toast.LENGTH_SHORT).show();
                }

                else{
                    Intent intent=new Intent();
                    intent.putExtra("name",nameToAdd);
                    intent.putExtra("city",Double.parseDouble(cityToAdd));
                    intent.putExtra("highway",Double.parseDouble(highwayToAdd));
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddRoute.class);
    }
}

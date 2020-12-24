package com.cmpt276.kenneyw.carbonfootprinttracker.model;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;

/**
 * Creates a Car from the CSV values and user input. If user inputs an empty name, tells user to try again
 * This class is extended by CarCollection and CarSingleton. Classes must be public to be used from the other package.
 */

public class Car{

    public Car(String name_temp, String make_temp, String model_temp, double highway_temp, double city_temp, int year_temp,
               String transmission_temp, double literEngine_temp,String gasType_temp, int iconID_temp){
        name = name_temp;
        make = make_temp;
        model = model_temp;
        Emissions[0] = highway_temp;
        Emissions[1] = city_temp;
        year = year_temp;
        transmission = transmission_temp;
        literEngine = literEngine_temp;
        gasType=gasType_temp;
        iconID = iconID_temp;
    }

    public Car(){
        name = "";
        make = "";
        model = "";
        Emissions[0] = 0;
        Emissions[1] = 0;
        year = 0;
        transmission = "";
        literEngine = 0;
        iconID = R.drawable.car_icon_2; // default icon
    }

    public double getLiterEngine() {
        return literEngine;
    }
    public void setLiterEngine(double literEngine) {
        this.literEngine = literEngine;
    }

    public String getTransmission() {
        return transmission;
    }
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getGasType(){
        return gasType;
    }
    public void setGasType(String gasType){
        this.gasType = gasType;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getMake(){
        return make;
    }
    public void setMake(String make){
        this.make = make;
    }

    public String getModel(){
        return model;
    }
    public void setModel(String model){
        this.model = model;
    }

    public int getYear(){
        return year;
    }
    public void setYear(int year){
        this.year = year;
    }

    public double getHighwayEmissions(){
        return Emissions[0];
    }
    public void setHighwayEmissions(double highway){
        this.Emissions[0] = highway;
    }

    public double getCityEmissions(){
        return Emissions[1];
    }
    public void setCityEmissions(double city){
        this.Emissions[1] = city;
    }

    public int getIconID(){return iconID;}

    public void setIconID(int iconId){
        this.iconID = iconId;
    }

    // Variables Declaration
    private String name;
    private String make;
    private String gasType;
    private int year;
    private String model;
    private double[] Emissions =  new double[2];
    private String transmission;
    private double literEngine;
    private int iconID;
}
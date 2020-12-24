package com.cmpt276.kenneyw.carbonfootprinttracker.model;
import android.support.v7.app.AppCompatActivity;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.Calculation;

/**
 *Stores a Journey, and calculates CO2 emitted via this Journey in constructor and in separate function
 *which is called when editing a pre-existing Journey.
 */
public class Journey extends AppCompatActivity {
    public static final int PEOPLE_PER_KM_SKYTRAIN = 360;   //CALCULATED AS:
                                                            //average num. people per day / 24 hours * 1/average KPH
                                                            //390600/24/45 = 360
    //<--average KPH from: https://en.wikipedia.org/wiki/SkyTrain_(Vancouver)-->
    //<--num people per day from: http://www.apta.com/resources/statistics/Documents/Ridership/2014-q4-ridership-APTA.pdf -->

    private int iconID;
    private String routeName;
    private double cityDistance;
    private double highwayDistance;
    private String name;
    private String gasType;
    private double mpgCity;
    private double mpgHighway;
    private double literEngine;
    private String dateString;
    private double totalEmissions;
    private boolean Bike;
    private boolean Bus;
    private boolean Walk;
    private boolean Skytrain;
    public static final double KWH_PER_KM = 6.848;
    public static final int CO2_PER_KWH = 9;
    public Journey(String routeName, double cityDistance, double highwayDistance,
                   String carName, String gasType, double mpgCity, double mpgHighway, double literEngine,
                   String dateString,boolean bus,boolean bike,boolean skytrain, boolean walk, int iconID){
        this.routeName=routeName;
        this.cityDistance = cityDistance;
        this.highwayDistance = highwayDistance;
        this.name=carName;
        this.gasType=gasType;
        this.mpgCity=mpgCity;
        this.mpgHighway=mpgHighway;
        this.literEngine=literEngine;
        this.dateString=dateString;
        this.Bus=bus;
        this.Bike=bike;
        this.Walk=walk;
        this.Skytrain=skytrain;
        this.totalEmissions=CalculateTotalEmissions();
        this.iconID = iconID;
    }
    public double CalculateTotalEmissions(){
        Calculation c=new Calculation();
        double totalEmissions=0;
        if(!this.Bus && !this.Bike && !this.Skytrain && !this.Walk )
            switch(this.gasType) {
                case "Premium":
                    totalEmissions += c.calculateCO2Diesel(mpgCity,cityDistance);
                    totalEmissions += c.calculateCO2Diesel(mpgHighway,highwayDistance);
                    break;
                case "Regular":
                    totalEmissions += c.calculateCO2Gasoline(mpgCity,cityDistance);
                    totalEmissions += c.calculateCO2Gasoline(mpgHighway,highwayDistance);
                    break;
                default:
                    totalEmissions=0;
                    break;
            }
        //89 grams of CO2 per km
        if(this.Bus){
            totalEmissions=0.089*(this.cityDistance+this.highwayDistance);
        }
        //<--From: http://ctrf.ca/wp-content/uploads/2015/05/CTRF2015NguyenSangOramPerlTransportationEnvironment.pdf-->
        else if(this.Skytrain){
            totalEmissions= KWH_PER_KM * (this.cityDistance+this.highwayDistance) * CO2_PER_KWH / PEOPLE_PER_KM_SKYTRAIN; //divide by number of ppl
        }
        return doubleToTwoPlaces(totalEmissions);
    }
    // Return with 2 decimal places
    private double doubleToTwoPlaces(double result_in_kg_CO2) {
        return Math.round(result_in_kg_CO2 * 100) /100 ;
    }
    public String getDateString() {return dateString;}
    public void setDateString(String dateString) {this.dateString = dateString;}
    public double getCityDistance() {
        return cityDistance;
    }
    public void setCityDistance(double cityDistance) {
        this.cityDistance = cityDistance;
    }
    public String getGasType() {
        return gasType;
    }
    public void setGasType(String gasType) {
        this.gasType = gasType;
    }
    public double getHighwayDistance() {
        return highwayDistance;
    }
    public void setHighwayDistance(double highwayDistance) {
        this.highwayDistance = highwayDistance;
    }
    public double getLiterEngine() {
        return literEngine;
    }
    public String getTotalDistanceToString(){
        return "Total Distance: " + Math.round( (highwayDistance + cityDistance) *100)/100 + " km";
    }
    public void setLiterEngine(double literEngine) {
        this.literEngine = literEngine;
    }
    public double getMpgCity() {
        return mpgCity;
    }
    public void setMpgCity(double mpgCity) {
        this.mpgCity = mpgCity;
    }
    public double getMpgHighway() {
        return mpgHighway;
    }
    public void setMpgHighway(double mpgHighway) {
        this.mpgHighway = mpgHighway;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRouteName() {
        return routeName;
    }
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
    public double getTotalEmissions() {
        return totalEmissions;
    }
    public void setTotalEmissions(double totalEmissions) {
        this.totalEmissions = totalEmissions;
    }
    public boolean isBike() {
        return Bike;
    }
    public void setBike(boolean bike) {
        Bike = bike;
    }
    public boolean isBus() {
        return Bus;
    }
    public void setBus(boolean bus) {
        Bus = bus;
    }
    public boolean isSkytrain() {
        return Skytrain;
    }
    public void setSkytrain(boolean skytrain) {
        Skytrain = skytrain;
    }

    public boolean isWalk(){return Walk;}
    public void setWalk(boolean walk) {
        Walk = walk;
    }

    public int getIconID(){
        return this.iconID;
    }
    public void setIconID(int iconId){
        if (iconId == R.drawable.car_icon_2)
            this.iconID = R.drawable.car_icon_2;
        else if (iconId == R.drawable.truck)
            this.iconID = R.drawable.truck;
        else if (iconId == R.drawable.modern)
            this.iconID = R.drawable.modern;
        else if (iconId == R.drawable.sport)
            this.iconID = R.drawable.sport;
        else if (iconId == R.drawable.classic)
            this.iconID = R.drawable.classic;

        else if (iconId == R.drawable.bike_icon)
            this.iconID = R.drawable.bike_icon;
        else if (iconId == R.drawable.bus_icon)
            this.iconID = R.drawable.bus_icon;
        else if (iconId == R.drawable.train_icon)
            this.iconID = R.drawable.train_icon;
        else{
            this.iconID = R.drawable.walk_icon;
        }
    }

    public String toString() {
        return
                name+
                        " - "
                        +routeName+
                        " - "
                        +cityDistance+ " km City, "
                        +highwayDistance+"km Highway"
                        + " - Date: "+dateString;
    }
}
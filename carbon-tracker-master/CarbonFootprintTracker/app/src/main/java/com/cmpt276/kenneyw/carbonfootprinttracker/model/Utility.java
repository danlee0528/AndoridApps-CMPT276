package com.cmpt276.kenneyw.carbonfootprinttracker.model;

/**
 * DESCRIPTION:
 * Creates a Utility user input. If user inputs an empty name, tells user to try again
 * This class is extended by UtilityCollection and UtilitySingleton.
 * Classes must be public to be used from the other package.
 */

import android.support.v7.app.AppCompatActivity;

import java.util.Date;

public class Utility extends AppCompatActivity {

    private String name;
    private String gasType;
    private double amount;
    private double emission;
    private int numofPeople;
    private String startDate;
    private String endDate;

    public Utility() {
        name = "";
        gasType = "";
        amount = 0.0;
        emission = 0.0;
        startDate = "Default StartDate";
        endDate = "Default EndDate";
        numofPeople = 0;
    }

    public Utility(String name, String gasType, double amounts, int num_people, double emission, String startDate, String endDate) {
        this.name = name;
        this.gasType = gasType;
        this.amount = amounts;
        this.emission = emission;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numofPeople = num_people;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGasType() {
        return gasType;
    }

    public void setGasType(String gasType) {
        this.gasType = gasType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getEmission() {
        return emission;
    }

    public void setEmission(double emission) {
        this.emission = emission;
    }

    public int getNumofPeople() {
        return numofPeople;
    }

    public void setNumofPeople(int numofPeople) {
        this.numofPeople = numofPeople;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String toString() {
        return name +", "+ gasType +", " + startDate + ", " + endDate;
    }

}
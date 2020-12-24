package com.cmpt276.kenneyw.carbonfootprinttracker.model;

/**
 * Creates a UtilitySingleton. This is just like a normal utility, expect there can be only one.
 * The singleton will keep it's values somewhat persistently, which is useful for later referencing
 * of it's values
 */

import android.support.v7.app.AppCompatActivity;

public class UtilitySingleton extends AppCompatActivity {
    private static UtilitySingleton utilityInstance = null;
    private String name;
    private String gasType;
    private double amounts;
    private int num_poeople;
    private double emission;
    private String startDate;
    private String endDate;

    public static UtilitySingleton getInstance() {
        if (utilityInstance == null) {
            utilityInstance = new UtilitySingleton();
        }
        return utilityInstance;
    }

    private UtilitySingleton() {
        name = "";
        gasType = "Default GasType";
        amounts = 0.0;
        num_poeople = 0;
        startDate = "Default startDate";
        endDate = "Default endDate";
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

    public double getAmounts() {
        return amounts;
    }

    public void setAmounts(double amounts) {
        this.amounts = amounts;
    }

    public int getNum_poeople() {
        return num_poeople;
    }

    public void setNum_poeople(int num_poeople) {
        this.num_poeople = num_poeople;
    }

    public double getEmission() {
        return emission;
    }

    public void setEmission(double emission) {
        this.emission = emission;
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

}
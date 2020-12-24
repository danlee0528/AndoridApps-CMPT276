package com.cmpt276.kenneyw.carbonfootprinttracker.model;

/**
 * Creates a TipHelperSingleton. There can be only one.
 * The singleton will keep it's values somewhat persistently, which is useful for future uses of the
 * singleton
 */

import android.util.Log;

import static java.lang.Math.ceil;

public class TipHelperSingleton {

    private int[] repeatTracker;
    private int tipIndex;
    private int noCycleCounter;
    private int moreThanFourUtil;
    private int moreThanFourElec;
    private static TipHelperSingleton helperInstance = null;
    private String currentMode;
    private int tipData;
    private double journeyEmission;
    private double journeyDist;
    private double nGasAmount;
    private double nGasEmission;
    private double elecAmount;
    private double elecEmission;
    private String lastUtil;


    private TipHelperSingleton() {

        tipIndex = 0;
        currentMode = "Travel";
        repeatTracker = new int[16];
        noCycleCounter = 0;
        moreThanFourUtil = 0;
        moreThanFourElec = 0;
        tipData = 0;
        journeyEmission = 0;
        journeyDist = 0;
        nGasAmount = 0;
        nGasEmission = 0;
        elecAmount = 0;
        elecEmission = 0;
        lastUtil = "";

    }

    public void setJourneyEmission(double emission) {
        this.journeyEmission = emission;
    }

    public double getJourneyEmission() {
        return journeyEmission;
    }

    public void setJourneyDist(double dist) {
        this.journeyDist = dist;
    }

    public double getJourneyDist() {
        return journeyDist;
    }

    public void setnGasAmount(double nGas) {
        this.nGasAmount = nGas;
    }

    public double getnGasAmount() {
        return nGasAmount;
    }

    public void setnGasEmission(double nGas) {
        this.nGasEmission = nGas;
    }

    public double getnGasEmission() {
        return nGasEmission;
    }

    public void setElecAmount(double elecAmount) {
        this.elecAmount = elecAmount;
    }

    public double getElecAmount() {
        return elecAmount;
    }

    public void setElecEmission(double elec) {
        this.elecEmission = elec;
    }

    public double getElecEmission() {
        return elecEmission;
    }

    public void setLastUtil(String lastUtil) {
        this.lastUtil = lastUtil;
    }

    public String getLastUtil() {
        return lastUtil;
    }

    public void setTipIndexTravel() {
        if (currentMode.equals("Utility") || currentMode.equals("Electric")) {
            this.tipIndex = 0;
            currentMode = "Travel";
        }
        else {
            Log.i("TIPS", "Already showing travel tips");
        }
    }

    public void setTipIndexUtil() {
        if (currentMode.equals("Travel") || currentMode.equals("Electric"))  {
            this.tipIndex = 8;
            currentMode = "Utility";
        }
        else {
            Log.i("TIPS", "Already showing utility tips");
        }
    }

    public void setTipIndexElec() {
        if (currentMode.equals("Travel") || currentMode.equals("Utility"))  {
            this.tipIndex = 12;
            currentMode = "Electric";
        }
        else {
            Log.i("TIPS", "Already showing electric tips");
        }
    }

    public int getTipIndex() {
        return tipIndex;
    }

    public int checkRepeatTracker(int tipIndex) {
        if (repeatTracker[tipIndex] == 0) {
            for (int i=0; i<16; i++) {
                if (repeatTracker[i] != 0) {
                    repeatTracker[i]++;
                }
                if (repeatTracker[i] > 9) {
                    repeatTracker[i] = 0;
                }
            }
            repeatTracker[tipIndex]++;
        }
        else if (repeatTracker[tipIndex] > 0) {
            tipIndex++;
            tipIndex = checkRepeatTracker(tipIndex);
        }
        return tipIndex;
    }

    public int spiceTimer() {
        if (noCycleCounter < 4) {
            noCycleCounter++;
        }
        if (noCycleCounter == 3) {
            noCycleCounter = 0;
            Log.i("Spicy!", ""+noCycleCounter);
            return 1;
        }
        Log.i("Spicy?", ""+noCycleCounter);
        return 0;
    }

    public int spiceTimerUtility() {
        moreThanFourUtil = moreThanFourUtil+1;
        if (moreThanFourUtil > 4) {
            return 1;
        }
        return 0;
    }

    public int spiceTimerElectric() {
        moreThanFourElec = moreThanFourElec+1;
        if (moreThanFourElec > 4) {
            return 1;
        }
        return 0;
    }

    public int tipDataFetcher(int index) {
        if (index == 0 || index == 1 || index == 2 || index == 3 || index == 4 || index == 6 || index == 7) {
            tipData = (int) ceil(journeyEmission);
            return tipData;
        }
        if (index == 5) {
            tipData = (int) ceil(journeyDist);
            return tipData;
        }

        if (index == 8 || index == 10) {
            tipData = (int) ceil(nGasAmount);
            return tipData;
        }
        if (index == 9 || index == 11) {
            tipData = (int) ceil(nGasEmission);
            return tipData;
        }

        if (index == 12 || index == 14) {
            tipData = (int) ceil(elecEmission);
            return tipData;
        }
        if (index == 13 || index == 15) {
            tipData = (int) ceil(elecAmount);
            return tipData;
        }

        return tipData;
    }

    public static TipHelperSingleton getInstance(){
        if(helperInstance == null){
            helperInstance = new TipHelperSingleton();
        }
        return helperInstance;
    }

}

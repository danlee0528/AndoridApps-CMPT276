package com.example.daniellee.myapplication;
/**
 * Store information about a single pot
 */
public class Pot {

    private String Pot_name;
    private int Pot_Weight;
    //private int iconID; // Added to identify pot list icons

    // Set member data based on parameters.
    public Pot(String name, int weightInG) {
        Pot_name = name;
        Pot_Weight = weightInG;
       // iconID = ID;
    }

    // Return the weight
    public int getWeightInG() {
        return Pot_Weight;
    }

    // Set the weight. Throws IllegalArgumentException if weight is less than 0.
    public void setWeightInG(int weightInG) {
        if (Pot_Weight < 0){
            throw new IllegalArgumentException();
        }else{
            Pot_Weight = weightInG;
        }
    }

    // Return the name.
    public String getName() {
        return Pot_name;
    }

    // Set the name. Throws IllegalArgumentException if name is an empty string (length 0),
    // or if name is a null-reference.
    public void setName(String name) {
        if ( Pot_name.isEmpty() ) {
            throw new IllegalArgumentException();
        }else{
            Pot_name = name;
        }
    }
/*
    public int getIconID(){
        return iconID;
    }

    // Set the iconID. Throws IllegalArgumentException if weight is less than 0.
    public void setIconID(int ID) {
        if (ID < 0){
            throw new IllegalArgumentException();
        }else{
            iconID = ID;
        }
    }
*/
}

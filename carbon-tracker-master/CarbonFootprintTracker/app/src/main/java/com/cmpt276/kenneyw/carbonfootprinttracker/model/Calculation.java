package com.cmpt276.kenneyw.carbonfootprinttracker.model;

import android.content.SharedPreferences;

/**
* CLASS DESCRIPTION:
*  Compute CO2 generated on a journey by how much gas was used during the trip for the city and highway portion.
*  Use the city08 and highway08 for miles per gallon of fuel. Only work with the primary fuel (ignore secondary fuels).
*
* REFERENCE:
*  CO2 emitted by fuel (https://www.eia.gov/environment/emissions/co2_vol_mass.cfm):
*  Gasoline (all grades): 8.89 Kilograms of CO2 per gallon [done]
*  Diesel:      10.16 Kilograms  of CO2 per gallon [done]
*  Electric:    0 (our electricity is mainly hydro) [done]
*  Natural Gas: Ignore (don't even list these in the app) [done]
*
*  Methood:
*
*   ____ Km  *  ___ miles/km * ___ miles/gallon * ___ kg/gallon = ___ kg/L (Kg CO2 per litre)
* (from user)                      (from CSV)                     (Result)
*
*   User interface is metric: Litres and Kilometers
*   city08 and higway08 are used for miles per gallon of fuel. Only focus on the primary fuel.
*
*
* CONSTRAINT:
*  Negative values are not allowed.
*
* NEW CONSTRAINT:
* Units must be in Time taken for 10 Trees to absorb the KG of CO2
 * 13 kg CO2 absorbed by 1 tree in 1 year,
* Time taken for 1 tree to absorb 1kg CO2 = 28 days
* Time taken for 10 trees to absorb 1kg CO2 = 2.8 days
* total time taken = kg CO2 * 2.8 Days
*
*/

public class Calculation {

    final private static double gasoline_volume_in_kg_per_gallon = 8.89;
    final private static double diesel_volumne_in_kg_per_gallon = 10.16;
    final private static double mile_per_km = 0.621371;
    final private static double gwh_per_kwh = 1000000;
    final private static double kgCO2_per_Gwh = 9000;
    final private static double kgCO2_per_GJ = 56.1;

    /*Calculate CO2 Emission of Gasoline with a given distance*/
    private double calculate_CO2_Emission_of_Gasoline(double distance_in_km_from_user,double miles_per_gallon){
        double result_in_kg_CO2;
        result_in_kg_CO2 = (distance_in_km_from_user) *
                                     (mile_per_km) *
                                     (1/miles_per_gallon) *
                                     (gasoline_volume_in_kg_per_gallon);
        return doubleToTwoPlaces(result_in_kg_CO2);
    }

    /*Calculate CO2 Emission of Diesel with a given distance*/
    private double calculate_C02_Emission_of_Diesel(double distance_in_km_from_user, double miles_per_gallon){
        double result_in_kg_CO2;
        result_in_kg_CO2 = (distance_in_km_from_user) *
                                     (mile_per_km) *
                                     (1/miles_per_gallon) *
                                     (diesel_volumne_in_kg_per_gallon);
        return doubleToTwoPlaces(result_in_kg_CO2);
    }

    /*Calculate CO2 Emission of Natural gas*/
    // Assume 9000Kg CO2 per GWh
    public double calculate_CO2_Emission_of_Natural_Gas(double kilowatts_per_hour_from_user ){
        double result_in_kg_CO2 = kgCO2_per_Gwh / gwh_per_kwh * kilowatts_per_hour_from_user;
       return doubleToTwoPlaces(result_in_kg_CO2);
    }

    // Assume 56.1 kg CO2 / GJ
    public double calculate_CO2_Emission_of_Electricity (double gigajouls_from_user ){
        double result_in_kg_CO2 = kgCO2_per_GJ * gigajouls_from_user;
        return doubleToTwoPlaces(result_in_kg_CO2);
    }


    private double doubleToTwoPlaces(double result_in_kg_CO2) {
        result_in_kg_CO2 = Math.round(result_in_kg_CO2 * 100)/100;
        return result_in_kg_CO2;
    }

    public double calculateCO2Diesel(double mpg, double distance)
    {
        if(distance==0||Double.isInfinite(distance)){return 0;}
        return calculate_C02_Emission_of_Diesel(mpg,distance);
    }
    public double calculateCO2Gasoline(double mpg, double distance)
    {
        if(distance==0||Double.isInfinite(distance)){return 0;}
        return calculate_CO2_Emission_of_Gasoline(mpg,distance);
    }
}
package com.cmpt276.kenneyw.carbonfootprinttracker.model;

/**
 * This Class makes an Individual Route and has it's getters and setters and constructor
 */

public class Route {

    public Route() {
        this.routeName = "";
        this.cityDistance = 0;
        this.highwayDistance = 0;
        this.totalDistance = cityDistance + highwayDistance;
    }

    public Route(String routeName, double cityDistance, double highwayDistance) {
        this.routeName = routeName;
        this.cityDistance = cityDistance;
        this.highwayDistance = highwayDistance;
        this.totalDistance = cityDistance + highwayDistance;
    }

    public String toString() {
        return routeName + ", " + cityDistance + " City, " + highwayDistance + " Highway";
    }

    public String getRouteName() {
        return routeName;
    }

    public double getCityDistance() {
        return cityDistance;
    }

    public double getHighwayDistance() {
        return highwayDistance;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;

    }

    public void setCityDistance(double cityDistance) {
        this.cityDistance = cityDistance;
        this.totalDistance = this.getCityDistance() + this.getHighwayDistance();
    }

    public void setHighwayDistance(double highwayDistance) {
        this.highwayDistance = highwayDistance;
        this.totalDistance = this.getCityDistance() + this.getHighwayDistance();
    }

    // Varaibles Declaration
    private String routeName;
    private double highwayDistance;
    private double cityDistance;
    private double totalDistance;

}
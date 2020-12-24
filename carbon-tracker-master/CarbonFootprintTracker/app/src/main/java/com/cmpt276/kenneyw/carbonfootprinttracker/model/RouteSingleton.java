package com.cmpt276.kenneyw.carbonfootprinttracker.model;

/**
 * Creates a RouteSingleton. This is just like a normal route, expect there can be only one.
 * The singleton will keep it's values somewhat persistently.
 */

public class RouteSingleton {

    private static RouteSingleton routeinstance = null;

    private RouteSingleton(){
        this.routeName= "";
        this.cityDistance = 0;
        this.highwayDistance = 0;
        this.totalDistance = cityDistance+highwayDistance;
    }

    public static RouteSingleton getInstance(){
        if(routeinstance == null){
            routeinstance = new RouteSingleton();
        }
        return routeinstance;
    }

    public String toString() {
        return routeName;
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
        this.totalDistance=this.getCityDistance()+this.getHighwayDistance();
    }

    public void setHighwayDistance(double highwayDistance) {
        this.highwayDistance = highwayDistance;
        this.totalDistance=this.getCityDistance()+this.getHighwayDistance();
    }

    // Variables Declaration
    private String routeName;
    private double highwayDistance;
    private double cityDistance;
    private double totalDistance;

}

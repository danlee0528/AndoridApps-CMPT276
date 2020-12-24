package com.cmpt276.kenneyw.carbonfootprinttracker.model;

/**
 * DESCRIPTION:
 *  - Create a list of car and functions to access to cars in the list
 */

import java.util.ArrayList;
import java.util.List;

public class RouteCollection {
    private List<Route> routes = new ArrayList<>();

    public void addRoute(Route route){
        routes.add(route);
    }

    public void changeRoute(Route route, int indexOfRouteToEdit) {
        validateIndexWithException(indexOfRouteToEdit);
        routes.remove(indexOfRouteToEdit);
        routes.add(indexOfRouteToEdit, route);
    }

    public void deleteRoute(int indexToDelete){
        validateIndexWithException(indexToDelete);
        routes.remove(indexToDelete);
    }

    public int countRoutes() {
        return routes.size();
    }

    public Route getRoute(int index) {
        validateIndexWithException(index);
        return routes.get(index);
    }

    public String[] getRoutesNames() {
        String[] descriptions = new String[countRoutes()];
        for (int i = 0; i < countRoutes(); i++) {
            Route route = getRoute(i);
            descriptions[i] = route.getRouteName();
        }
        return descriptions;
    }

    public String[] getRoutesDescriptionsWithName() {
        String[] descriptions = new String[countRoutes()];
        for (int i = 0; i < countRoutes(); i++) {
            Route route = getRoute(i);
            descriptions[i] = route.getRouteName() + ", City:" + route.getCityDistance() +
                    ", Highway:" + route.getHighwayDistance() + ", Total: " + route.getTotalDistance();
        }
        return descriptions;
    }

    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countRoutes()) {
            throw new IllegalArgumentException();
        }
    }

}

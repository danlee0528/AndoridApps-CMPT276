package com.cmpt276.kenneyw.carbonfootprinttracker.model;

/**
 * DESCRIPTION:
 *  - Create a list of Journeys and functions to access to journeys in the list
 */

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class JourneyCollection extends AppCompatActivity{
    private List<Journey> journeys = new ArrayList<>();

    public void addJourney(Journey journey){
        journeys.add(journey);
    }

    public void changeJourney(Journey journey, int indexOfJourneyToEdit) {
        validateIndexWithException(indexOfJourneyToEdit);
        journeys.remove(indexOfJourneyToEdit);
        journeys.add(indexOfJourneyToEdit, journey);
    }

    public void deleteJourney(int indexToDelete){
        validateIndexWithException(indexToDelete);
        journeys.remove(indexToDelete);
    }

    public int countJourneys() {
        return journeys.size();
    }

    public Journey getJourney(int index) {
        validateIndexWithException(index);
        return journeys.get(index);
    }

    public String[] getJourneysNames() {
        String[] descriptions = new String[countJourneys()];
        for (int i = 0; i < countJourneys(); i++) {
            Journey journey = getJourney(i);
            descriptions[i] = journey.getName();
        }
        return descriptions;
    }

    public String[] getJourneysDescriptionsWithName() {
        String[] descriptions = new String[countJourneys()];
        for (int i = 0; i < countJourneys(); i++) {
            Journey journey = getJourney(i);
            descriptions[i] = "" + journey.getName() +" - " + journey.getRouteName() + " - "+
                    journey.getDateString() ;
        }
        return descriptions;
    }

    public List<Journey> returnJourneyList(){
        return journeys;
    }

    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countJourneys()) {
            throw new IllegalArgumentException();
        }
    }
}

package com.cmpt276.kenneyw.carbonfootprinttracker.model;

/**
 * DESCRIPTION:
 *  - Create a list of utilities and functions to access the utilities in the list
 *
 *  This class contains all the functions necessary to manage collections of utilities.
 */

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class UtilitiesCollection extends AppCompatActivity {
    private List<Utility> utilities = new ArrayList<>();

    public void addUtility(Utility utility) {
        utilities.add(utility);
    }

    public void changeUtility(Utility utility, int indexOfUtilityToEdit) {
        validateIndexWithException(indexOfUtilityToEdit);
        utilities.remove(indexOfUtilityToEdit);
        utilities.add(indexOfUtilityToEdit, utility);
    }

    public void deleteUtility(int indexToDelete) {
        validateIndexWithException(indexToDelete);
        utilities.remove(indexToDelete);
    }

    public int countUtility() {
        return utilities.size();
    }

    public Utility getUtility(int index) {
        validateIndexWithException(index);
        return utilities.get(index);
    }

    // utility.toString prints everything about the utility
    public String[] getUtilitiesDescriptionsWithName() {
        String[] descriptions = new String[countUtility()];
        for (int i = 0; i < countUtility(); i++) {
            Utility utility = getUtility(i);
            descriptions[i] = "Utility [" + utility.getName() + "] " + "between " + utility.getStartDate() + " and " + utility.getEndDate();
        }
        return descriptions;
    }

    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countUtility()) {
            throw new IllegalArgumentException();
        }
    }
}
package com.example.daniellee.portandstarboard;

import java.util.Arrays;
import java.util.Collections;

/**
 * Immutable game class which randomly picks a side of the ship (port or starboard)
 * for the user to respond with an answer.
 * <p>
 * Supports getting the name of the chosen side, and checking if an answer is correct.
 * <p>
 * Revision of Dr. Fraser's code
 * <p>
 * Link: http://www2.cs.sfu.ca/CourseCentral/276/bfraser/assignments/files/as1/Game.java
 * <p>
 * <Summary of Revision>
 * - "A Redundant semi-colon removed"
 * - "Unnecessary private" fixed
 * - "Access can be package local error" fixed
 */


class Game {
    enum Side {
        PORT("Port"),
        STARBOARD("Starboard");

        private String name;

        Side(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


    private Side winner = Side.PORT;

    Game() {
        //Pick a winnder:
        Side buttons[] = {Side.PORT, Side.STARBOARD};
        Collections.shuffle(Arrays.asList(buttons));
        winner = buttons[0];
    }


    String getChosenSideName() {
        return winner.getName();
    }

    // Check if the user chooses the right winner
    boolean checkIfCorrect(Side side) {
        return (winner == side);
    }


}

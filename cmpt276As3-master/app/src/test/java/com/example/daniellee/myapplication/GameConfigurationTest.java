package com.example.daniellee.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;
/** Used to test Game Configuration Class
 * Checks the getter, setter methods and constructor.
 * Further tests the InitializeBoard, addRow and addCol functions
 * Created by ahad on 2017-02-20.
 */

public class GameConfigurationTest {


    // All Board tests done with 3 Mines on 4x6 Board
    @Test
    public void InitializeBoardTest()  {

        GameConfiguration GC= new GameConfiguration();
        GC.InitializeBoard4Testing();
        GC.placeMine4Testing(1,1);
        GC.placeMine4Testing(2,2);
        GC.placeMine4Testing(3,3);
        int[][] Board = GC.getBoard();

        int[] row0 = { 0, 1, 1, 1, 0, 0};
        int[] row1 = { 1, 99, 2, 2, 1, 1};
        int[] row2 = { 1, 2, 99, 2, 1, 1};
        int[] row3 = { 1, 2, 2, 99, 1, 1};

        assertArrayEquals(row0,Board[0]);
        assertArrayEquals(row1,Board[1]);
        assertArrayEquals(row2,Board[2]);
        assertArrayEquals(row3,Board[3]);
    }

    @Test
    public void isMineInGameTest()  {

        GameConfiguration GC= new GameConfiguration();
        GC.InitializeBoard4Testing();
        GC.placeMine4Testing(1,1);
        GC.placeMine4Testing(2,2);
        GC.placeMine4Testing(3,3);
        GC.foundMine(1,1);

        int Board[][] = GC.getBoard();

        int[] row0 = { 0, 0, 1, 1, 0, 0};
        int[] row1 = { 0, 100, 1, 1, 0, 0};
        int[] row2 = { 1, 1, 99, 2, 1, 1};
        int[] row3 = { 1, 1, 2, 99, 1, 1};

        assertArrayEquals(row0,Board[0]);
        assertArrayEquals(row1,Board[1]);
        assertArrayEquals(row2,Board[2]);
        assertArrayEquals(row3,Board[3]);
    }
    @Test
    public void isMineMultipleTest()  {

        GameConfiguration GC= new GameConfiguration();
        GC.InitializeBoard4Testing();
        GC.placeMine4Testing(1,1);
        GC.placeMine4Testing(2,2);
        GC.placeMine4Testing(3,3);
        GC.foundMine(1,1);
        GC.foundMine(2,2);
        GC.foundMine(3,3);

        int Board[][] = GC.getBoard();

        int[] row0 = { 0, 0, 0, 0, 0, 0};
        int[] row1 = { 0, 100, 0, 0, 0, 0};
        int[] row2 = { 0, 0, 100, 0, 0, 0};
        int[] row3 = { 0, 0, 0, 100, 0, 0};

        assertArrayEquals(row0,Board[0]);
        assertArrayEquals(row1,Board[1]);
        assertArrayEquals(row2,Board[2]);
        assertArrayEquals(row3,Board[3]);
    }



}

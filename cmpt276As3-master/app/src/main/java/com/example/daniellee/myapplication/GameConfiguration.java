package com.example.daniellee.myapplication;

import java.util.Random;


/**
 *
 * REQUIREMENTS
 *
 *  // BoardSize
 *  // 4 rows by 6 columns
 *  // 5 rows by 10 columns
 *  // 6 rows by 15 columns
 *
 *  // # of mines
 *  // 6 mines
 *  // 10 mines
 *  // 15 mines
 *  // 20 mines
 *
 *  The game size and number of mines are saved between application runs
 *
 *  May allow user to reset number of times game has been played, and best scores for each game configuration
 */

class GameConfiguration {

    private final static int MINE = 99;
    private final static int MINEFOUND = 100;
    private final static int ADD = 1;
    private final static int SUBTRACT = -1;
    private int NUM_COL;
    private int NUM_ROW;
    private int NUM_MINES;
    private int Board[][];
    private int scans=0;
    private int minesFound=0;
    private Boolean isGameFinshed = Boolean.TRUE;
    private int userHiScore[] = new int[12];
    private int totalGamesPlayed = 0;
    private static final GameConfiguration gameConfiguration = new GameConfiguration();

    private GameConfiguration() {
        // Default game size
        NUM_COL = 6;
        NUM_ROW = 4;
        NUM_MINES = 6;
    }

     static GameConfiguration getInstance() {
        return gameConfiguration;
    }

     int getMinesFound() {
        return minesFound;
    }

     int getNUM_COL() {
        return NUM_COL;
    }

     int getNUM_ROW() {
        return NUM_ROW;
    }

     int getNUM_MINES() {
        return NUM_MINES;
    }

     void setNUM_MINES(int NUM_MINES) {
        this.NUM_MINES = NUM_MINES;
    }

     int getScans() {
        return scans;
    }

     void set_boardSize(int row, int col){
        NUM_COL = col;
        NUM_ROW = row;
    }

     int[][] getBoard() {
        return this.Board;
    }

     int getBoardElement(int row, int col) {
        return this.Board[row][col];
    }

     int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    // Used when resetting the number of games played to zero
     void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    // Used when performing a mine scan
     void incrementScans(){
        scans++;
    }


    // Used to initialize board for playing
    // Calls the placeMine function N times where N is the number of Mines
    // Resets the value of scans and mines found
     void InitializeBoard() {
        Board = new int[NUM_ROW][NUM_COL];
        scans = 0;
        minesFound = 0;
        isGameFinshed = Boolean.FALSE;
        for (int i = 0; i < NUM_MINES; i++) {
            placeMine();
        }
    }

    // Places a mine in a random position in the board
    // Ensures that two mines are not placed on top of each other
    private void placeMine() {
        int row;
        int col;
        Random rand = new Random ();
        boolean isUnique = Boolean.FALSE;

        row = rand.nextInt(NUM_ROW);
        col = rand.nextInt(NUM_COL);

        while(!isUnique) {
            if(isMine(row,col)) {
                row = rand.nextInt(NUM_ROW);
                col = rand.nextInt(NUM_COL);
            }
            else isUnique =  Boolean.TRUE;
        }

        Board[row][col] = MINE;
        updateBoard(row, col, ADD);
    }

    // Used to increment the row and column when placing mine
    // Also used to decrement when user finds a mine
    private void updateBoard(int row, int col, int operation) {
        for (int i = 0; i < NUM_COL; i++) {
            if (!isMine(row, i)) Board[row][i] += operation;
        }
        for (int i = 0; i < NUM_ROW; i++) {
            if (!isMine(i,col)) Board[i][col] += operation;
        }
    }

    // Checks if the board element is either a mine or found mine
     Boolean isMine(int row, int col)   {
        if (Board[row][col] >= MINE) return Boolean.TRUE;
        else return Boolean.FALSE;
    }

    // Used for the mine scan function and in foundMine
    private Boolean isUndiscoveredMine(int row, int col) {
        if (Board[row][col] == MINE) return Boolean.TRUE;
        else return Boolean.FALSE;
    }

    // When user clicks a button it checks whether the board element is
    // an undiscovered Mine
     Boolean foundMine(int row, int col)   {

        if (isUndiscoveredMine(row,col)) {
            updateBoard(row,col,SUBTRACT);
            Board[row][col] = MINEFOUND;
            minesFound++;
            return Boolean.TRUE;
        }
        else {
            scans++;
            return Boolean.FALSE;
        }

    }

    // Used for the MineScan function
    // Used to calculate the number of undiscovered mines in
    // the row and column
     int minesInRowCol(int row, int col){

        int numberOfMines=0;

        for (int i = 0; i < NUM_COL; i++) {
            if (isUndiscoveredMine(row, i) && i != col) numberOfMines++ ;
        }
        for (int i = 0; i < NUM_ROW; i++) {
            if (isUndiscoveredMine(i,col) && i != row) numberOfMines++;
        }
        return numberOfMines;
    }

    // Called when determining whether the game is finished or not
    // Increments number of games played
    // Also updates score
     Boolean getGameFinshed() {
        if (minesFound == NUM_MINES)    {
            isGameFinshed = Boolean.TRUE;
            int prev_score = getUserHiScore();
            if (scans < prev_score ||  prev_score == 0)   {
                setUserHiScore(scans);
            }
            totalGamesPlayed++;
        }
        return isGameFinshed;
    }

    // Gets the User's HiScore for their current configuration
    // Used when user beats his old score
     int getUserHiScore() {
        int configuration = identifyConfiguration4Score();
        return userHiScore[configuration];
    }

    // Sets the User's HiScore for their current configuration
    private void setUserHiScore(int newScore) {

        int configuration = identifyConfiguration4Score();
        userHiScore[configuration] = newScore;
    }

    // userHiScore [0-3] -- 4*6 6,10,15,120
    // userHiScore [4-7] -- 5*10 6,10,15,120
    // userHiScore [7-11] -- 6*15 6,10,15,120
    // Used to determine the configuration the user is currently using
    private int identifyConfiguration4Score()   {

        if (NUM_ROW == 4 && NUM_COL ==6){
            switch(NUM_MINES){
                case 6: return 0;
                case 10:return 1;
                case 15:return 2;
                case 20:return 3;
                default:
                    break;
            }
        }
        else if (NUM_ROW == 5 && NUM_COL== 10){
            switch(NUM_MINES){
                case 6: return 4;
                case 10:return 5;
                case 15:return 6;
                case 20:return 7;
                default:
                    break;
            }
        }
        else if (NUM_ROW == 6 && NUM_COL == 15){
            switch(NUM_MINES){
                case 6: return 8;
                case 10:return 9;
                case 15:return 10;
                case 20:return 11;
                default:
                    break;
            }
        }
        return -1;
    }

    // Returns the array of User Scores for all configurations
     int[] getUserHiScores() {
        return userHiScore;
    }
    // Sets the array of User Scores , used when getting data from shared preferences
     void setUserHiScores(int[] newData) {
        userHiScore = newData;
    }

     int[] resetUserHighScores()  {
        for(int i = 0; i < 12; i++) {
            userHiScore[i]=0;
        }
        return userHiScore;
    }

    // Following functions were used for testing
     void InitializeBoard4Testing() {
        Board = new int[NUM_ROW][NUM_COL];
    }

     void placeMine4Testing(int row, int col) {
        Board[row][col] = MINE;
        updateBoard(row, col, ADD);
    }

}

package com.cmpt276.kenneyw.carbonfootprinttracker.model;

/**
 *Code taken from user Alex Jolig from stackoverflow post:
 *http://stackoverflow.com/questions/513084/ship-an-application-with-a-database
 *Code is used as-is with small modifications to fit personal needs.
 *This code is to access the distributed database.
 *
 * All functions have be customized for custom use, with comments outlining what each function does
 *
*/

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
public class DatabaseAccess extends AppCompatActivity {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }
    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DatabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }
    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }
    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }
    //Query that gets all the makes in the CarDB. Takes in a year, returns a List<String> of makes.
    public List<String> getMakes(String year) { //
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT DISTINCT make FROM 'mainTable' WHERE year = " + year + " ORDER BY make ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    //Query that gets all the models in the CarDB. Takes in a year and make, returns a List<String> of models.
    public List<String> getModels(String year, String make) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT DISTINCT model FROM 'mainTable' WHERE year = " + year + " AND make = \"" + make + "\"  ORDER BY model ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    //Query that gets all the years in the CarDB. Takes in no params, returns a List<String> of years.
    public List<String> getYears() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT DISTINCT year FROM 'mainTable' ORDER BY year ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    //Query that gets all the Car info in the CarDB. Takes in a year and make, returns a List<String[]> of all info in the car row.
    public List<String[]> getTempCarList(String year, String make, String model) {
        List<String[]> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT DISTINCT * FROM 'mainTable' WHERE year = " + year + " AND make = \"" + make + "\" AND model = \"" + model + "\"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] row = new String[cursor.getColumnCount()];
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                row[i] = cursor.getString(i);
            }
            list.add(row);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    //This is a dummy class, more for template reasons. Please do not change for the time being.
    public List<String> getUserCars() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM quotes", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
package com.cmpt276.kenneyw.carbonfootprinttracker.model;


/**
 *Code taken from user Alex Jolig from stackoverflow post:
 *http://stackoverflow.com/questions/513084/ship-an-application-with-a-database
 *Code is used as-is with small modifications to fit personal needs.
 *This code is to access the distributed database.
 *
 * This DatabaseHelper interfaces between the built-in SQLite class and the database.
*/

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "cardb.db";
    private static final int DATABASE_VERSION = 3;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
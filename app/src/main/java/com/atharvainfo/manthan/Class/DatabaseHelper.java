package com.atharvainfo.manthan.Class;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String TAG = "DatabaseHelper";
    public static String DB_NAME = "manthan.db";
    private final Context mcontext;
    //public static String DB_PATH ="";// "/data/data/com.atharvainfosolutions.myleader/databases/";
    public static String DB_PATH ="/data/data/com.atharvainfo.manthan/databases/manthan.db";
    public static String mPath = DB_PATH+DB_NAME;
    private static final String DATABASE_NAME = "manthan.db";

    private static final int DATABASE_VERSION = 1;

    public String pathToSaveDBFile;
    public DatabaseErrorHandler err;
    public String path;
    String backupDBPath = "/data/data/databases/";
    File sd = Environment.getExternalStorageDirectory();
    File backupDB = new File(sd, backupDBPath);
    private SQLiteDatabase mDataBase;

    private static final int DB_VERSION = 1;



    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mcontext = context;
        Log.d("DBVERSION","The Database Version (as hard coded) is " + String.valueOf(DB_VERSION));

        int dbversion = DatabaseAssetHandler.getVersionFromDBFile(context,DB_NAME);
        Log.d("DBVERSION","The Database Version (as per the database file) is " + String.valueOf(dbversion));

        // Copy the Database if no database exists
        if (!DatabaseAssetHandler.checkDataBase(context,DB_NAME)) {
            DatabaseAssetHandler.copyDataBase(context,DB_NAME,true,DB_VERSION);
        } else {
            // Copy the database if DB_VERSION is greater then the version stored in the database (user_version value in the db header)
            if (DB_VERSION > dbversion && DatabaseAssetHandler.checkDataBase(context, DB_NAME)) {
                DatabaseAssetHandler.copyDataBase(context, DB_NAME, true,DB_VERSION);
               // DatabaseAssetHandler.restoreTable(context,DB_NAME,????THE_TABLE_NAME????); // Example of restoring a table (note ????THE_TABLE_NAME???? must be changed accordingly)
                DatabaseAssetHandler.clearForceBackups(context, DB_NAME); // Clear the backups
            }
        }
        mDataBase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    // onUpgrade should not be used for the copy as may be issues due to db being opened
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}

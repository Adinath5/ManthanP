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
    private final Context myContext;
    public static String DB_PATH ="";// "/data/data/com.atharvainfosolutions.myleader/databases/";
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

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME ,null, DATABASE_VERSION);
        this.myContext = context;
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
        //pathToSaveDBFile = filePath.toString();
        boolean dbexist = checkDataBase();
        if (dbexist) {
            openDatabase();
        } else {
            System.out.println("Database doesn't exist");
            try {
                createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createDataBase() throws IOException {
        boolean dbexist = checkDataBase();
        if(!dbexist) {
            this.getReadableDatabase();
            Log.d("CopingDatabase", "Create Database");

            try {
                copyDataBase();
            } catch(IOException e) {
                throw new Error("Error copying database");
            }

        }
    }

    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            File file = new File(DB_PATH);
            checkDB = file.exists();
            Log.d("Database", "Database Exist");
        } catch(SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
        return checkDB;

        // File DbFile = new File(DB_PATH + DB_NAME);
        // Log.d("Database", "Database Exist");
        // return DbFile.exists();
    }

    private void copyDataBase() throws IOException {
        String outfileName = DB_PATH;
        OutputStream os = new FileOutputStream(outfileName);
        InputStream is = myContext.getAssets().open(DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.flush();
        os.close();
    }


    public void deleteDB(){
        File file = new File(pathToSaveDBFile);

        if (file.exists()){
            file.delete();
            Log.d(TAG, "Database Deleted");
        }
    }
    public boolean openDatabase() throws SQLiteException
    {
        Log.d(TAG, "Database open");
        Log.d(TAG, DB_PATH.toString());
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;

    }
    public synchronized void close(){
        if(mDataBase != null)
            mDataBase.close();
        SQLiteDatabase.releaseMemory();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db){

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    private int getVersionId() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT version_id FROM dbVersion";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int v =  cursor.getInt(0);
        db.close();
        return v;
    }
}

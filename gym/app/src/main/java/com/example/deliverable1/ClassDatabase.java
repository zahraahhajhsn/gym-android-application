package com.example.deliverable1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassDatabase extends SQLiteOpenHelper {
    private static final String TAG = "ClassDatabase.db";
    public static final String DATABASE_NAME = "ClassDatabase";
    public static final String KEY_ID = "ID";
    public static final String INSTRUCTOR_CLASSES = "instructorClasses";
    public static final String COLUMN_INSTRUCTOR_NAME = "instructorName";
    public static final String COLUMN_CLASS_TYPE = "classType";
    public static final String COLUMN_CLASS_DAYS = "classDays";
    public static final String COLUMN_CLASS_HOURS = "classHours";
    public static final String COLUMN_CLASS_DIFFICULTY = "classDiff";
    public static final String COLUMN_CLASS_CAPACITY = "classCap";
    public static final String COLUMN_CLASS_START_TIME = "startTime";

    public ClassDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists classes(className TEXT primary key, classDesc TEXT)");

        //For debugging
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + INSTRUCTOR_CLASSES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_INSTRUCTOR_NAME + " TEXT, " + COLUMN_CLASS_TYPE
                + " TEXT, " + COLUMN_CLASS_DAYS + " TEXT, " + COLUMN_CLASS_HOURS + " TEXT, " + COLUMN_CLASS_DIFFICULTY + " TEXT, " + COLUMN_CLASS_CAPACITY + " TEXT, " + COLUMN_CLASS_START_TIME + " TEXT)";

        db.execSQL(createTableStatement);

    }

    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("drop Table if exists classes");

        db.execSQL("DROP TABLE IF EXISTS " + INSTRUCTOR_CLASSES);

    }

    public List<String> getAllClassTypes(){

        List<String> classTypes = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select className from classes", null);

        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {

                classTypes.add(cursor.getString(0));

                cursor.moveToNext();
            }

        }

        cursor.close();

        return classTypes;
    }

    //Check if a class already exists at a day.
    public String itemExists(String className, String day){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + INSTRUCTOR_CLASSES, null);

        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {

                if (cursor.getString(2).equals(className) && cursor.getString(3).equals(day)){
                    String instructor = cursor.getString(1);
                    cursor.close();

                    return instructor;
                }

                cursor.moveToNext();
            }
        }

        cursor.close();

        return null;
    }

}
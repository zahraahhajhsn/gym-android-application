package com.example.deliverable1;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

@RunWith(AndroidJUnit4.class)
public class ClassDatabaseTest {

    private ClassDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = new ClassDatabase(context);
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeClassandRead() {
        // tests writing to the database and conflict detection (itemExists() method)
        SQLiteDatabase write = db.getWritableDatabase();

        ContentValues content = new ContentValues();

        content.put("instructorName", "George");
        content.put("classType", "Yoga");
        content.put("classDays", "Wednesday");
        content.put("classHours", "2 hours");
        content.put("classDiff", "Advanced");
        content.put("classCap", "54");
        content.put("startTime", "11:00");

        write.insert("instructorClasses", null, content);

        String user = db.itemExists("Yoga", "Wednesday");
        assertEquals("George", user);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ClassDatabaseTest");
    }
}

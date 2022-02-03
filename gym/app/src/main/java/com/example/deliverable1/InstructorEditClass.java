package com.example.deliverable1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InstructorEditClass extends AppCompatActivity implements EditInstructorClassDialog.EditInstructorDialogListener {
    Bundle extras;

    ListView listview;
    HashMap<String, String> list;
    ArrayList<HashMap<String, String>> arrayHashes;
    SimpleAdapter simpleAdapter;

    ClassDatabase classDatabase;

    String username;

    static String className;
    static String instructorName;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructor_edit);

        extras = getIntent().getExtras();
        listview = findViewById(R.id.listview_instrclasses);
        username = extras.getString("username");

        classDatabase = MainActivity.getClassDatabase();

        SQLiteDatabase database = classDatabase.getWritableDatabase();

        arrayHashes = new ArrayList<HashMap<String, String>>();

        simpleAdapter = new SimpleAdapter(this, arrayHashes, R.layout.list_item3, new String[] {"classType", "classDays"}, new int[]{R.id.text111, R.id.text222});

        Cursor cursor = database.rawQuery("select * from instructorClasses WHERE instructorName = ?", new String[] {username});

        String IName;
        String cName;
        String day;

        if (cursor.moveToFirst()) {
            // Populates listView with all classes currently in Class Database
            while (!cursor.isAfterLast()) {
                IName = cursor.getString(1);
                cName = cursor.getString(2);
                day = cursor.getString(3);

                //list.put(cName, cDesc);
                HashMap<String,String> resultMap = new HashMap<>();
                resultMap.put("classType", cName);
                resultMap.put("instructorName", IName);
                resultMap.put("classDays", day);
                arrayHashes.add(resultMap);

                cursor.moveToNext();
            }

            listview.setAdapter(simpleAdapter);
        }
        cursor.close();


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                index = i;
                classDatabase = MainActivity.getClassDatabase();

                SQLiteDatabase db2 = classDatabase.getWritableDatabase();

                String cType2 = new String(arrayHashes.get(index).get("classType").toString());
                String iName2 = new String(arrayHashes.get(index).get("instructorName").toString());
                String day2 = new String(arrayHashes.get(index).get("classDays").toString());

                Cursor cursor = db2.rawQuery("select * from instructorClasses WHERE instructorName = ? AND classType = ? AND classDays = ?", new String[] {iName2, cType2, day2});
                cursor.moveToFirst(); // if this fails, we have an issue where our click isn't corresponding correctly to the arrayHashes array


                String sName = new String(cursor.getString(1));
                String sType = new String(cursor.getString(2));
                String sDay = new String(cursor.getString(3));
                String sDuration = new String(cursor.getString(4));
                String sDifficulty = new String(cursor.getString(5));
                String sCapacity = new String(cursor.getString(6));
                String sTime = new String(cursor.getString(7));

                cursor.close();

                openEditClassDialog(sName, sType, sDay, sDuration, sDifficulty, sCapacity, sTime);

                simpleAdapter.notifyDataSetChanged();

            }
        });

    }

    public void openEditClassDialog(String name, String type, String day, String dur, String diff, String cap, String time){
        EditInstructorClassDialog editClass = new EditInstructorClassDialog(name, type, day, dur, diff, cap, time);
        editClass.show(getSupportFragmentManager(), "Instructor Edit Class Dialog");
    }

    public void editData(String day, String hours, String difficulty, String cap, String time , String orgDay){

        classDatabase = MainActivity.getClassDatabase();

        SQLiteDatabase db = classDatabase.getWritableDatabase();

        String tempClassType = arrayHashes.get(index).get("classType").toString();
        String tempInstructorName = arrayHashes.get(index).get("instructorName").toString();

        ContentValues values = new ContentValues();
        values.put("classType", tempClassType);
        values.put("instructorName", tempInstructorName);
        values.put("classDays", day);
        values.put("classHours", hours);
        values.put("classDiff", difficulty);
        values.put("classCap", cap);
        values.put("startTime", time);
        String[] nameArgs = { tempClassType, tempInstructorName, orgDay};

        db.update("instructorClasses", values, "classType = ? AND instructorName = ? AND classDays = ?", nameArgs);

        arrayHashes.remove(index);
        list = new HashMap<>();

        simpleAdapter.notifyDataSetChanged();

        list.put(tempClassType, tempInstructorName);

        HashMap<String, String> resultsMap = new HashMap<>();
        resultsMap.put("classType", tempClassType);
        resultsMap.put("instructorName", tempInstructorName);
        resultsMap.put("classDays", day);
        resultsMap.put("classHours", hours);
        resultsMap.put("classDiff", difficulty);
        resultsMap.put("classCap", cap);
        resultsMap.put("startTime", time);

        arrayHashes.add(resultsMap);

        listview.setAdapter(simpleAdapter);

        Toast.makeText(InstructorEditClass.this, "Edited Class", Toast.LENGTH_SHORT).show();

        simpleAdapter.notifyDataSetChanged();

    }

    public void deleteClass() {
        classDatabase = MainActivity.getClassDatabase();
        SQLiteDatabase db = classDatabase.getWritableDatabase();
        String tempName = arrayHashes.get(index).get("classType").toString();
        String tempInstr = arrayHashes.get(index).get("instructorName").toString();
        String tempDay = arrayHashes.get(index).get("classDays").toString();
        db.delete("instructorClasses", "classType = ? AND instructorName = ? AND classDays = ?", new String[] {tempName, tempInstr, tempDay});
        arrayHashes.remove(index);
        simpleAdapter.notifyDataSetChanged();
        listview.setAdapter(simpleAdapter);
        Toast.makeText(InstructorEditClass.this, "Removed Class", Toast.LENGTH_SHORT).show();
    }

}

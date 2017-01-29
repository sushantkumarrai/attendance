package com.example.pkpra_000.attendence;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {

    private  static  final String DB_NAME="Attendence";
    private static final int DB_VERSION=1;

    DatabaseHandler(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE SUBJECT ("+"_id INTEGER PRIMARY KEY AUTOINCREMENT,"+"SUBJECT_NAME TEXT );");
            db.execSQL("CREATE TABLE ATTENDENCE("+"SUB_ID INTEGER,"+"DATE TEXT,"+"PRESENT INTEGER,"+"TOTAL INTEGER,"+"FOREIGN KEY(SUB_ID) REFERENCES SUBJECT(_id));");



        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}


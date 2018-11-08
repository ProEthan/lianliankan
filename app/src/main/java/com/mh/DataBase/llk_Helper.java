package com.mh.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class llk_Helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "llk.db";

    private static final int DATABASE_VERSION = 1;

    public llk_Helper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS score"+
                    "(name VARCHAR PRIMARY KEY ,time INT, sc INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE score ADD COLUMN other STRING");
    }
}

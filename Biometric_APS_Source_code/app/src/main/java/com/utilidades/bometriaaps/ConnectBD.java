package com.utilidades.bometriaaps;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ConnectBD extends SQLiteOpenHelper {
    private static final String bdName = "infoaps.db";
    private static final int vs = 1;
    public ConnectBD(Context context) {
        super(context, bdName, null, vs);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table user(id integer primary key autoincrement," +
            "name varchar(50), pass varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

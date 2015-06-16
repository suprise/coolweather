package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aeolia on 2015/6/16.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_PROVINCE = "" +
            "create table Province(" +
            "id integer primary key autoincrement, " +
            "province_name text);";
    public static final String CREATE_CITY = "" +
            "create table City (" +
            "id integer primary key autoincrement, " +
            "city_name text," +
            "city_pinyin text," +
            "province_id integer);";

    public CoolWeatherOpenHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version) {
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {

    }
}

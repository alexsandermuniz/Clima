package com.clima.alexsander.clima.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.clima.alexsander.clima.sqlite.models.City;
import com.clima.alexsander.clima.sqlite.models.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexs on 30/03/2018.
 */



public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "city_weather_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create table
        db.execSQL(City.CREATE_TABLE);
        db.execSQL(Weather.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + City.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Weather.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertCity(City city) {

        long weather_id = insertWeather(city.getWeather());

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // timestamp will be inserted automatically.
        values.put(City.COLUMN_ID,city.getId());
        values.put(City.COLUMN_NAME,city.getName());
        values.put(City.COLUMN_WEATHER_ID,weather_id);

        long id = -1;
        try {
            // insert row
            db.insert(City.TABLE_NAME, null, values);
        }catch (SQLiteConstraintException ex){
            //case id already exists
            ex.printStackTrace();
        }

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public City getCity(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(City.TABLE_NAME,
                new String[]{City.COLUMN_ID, City.COLUMN_NAME,City.COLUMN_WEATHER_ID, City.COLUMN_TIMESTAMP},
                City.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare object
        City city = null;

        try{
            city = new City(
                    cursor.getLong(cursor.getColumnIndex(City.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(City.COLUMN_NAME)),
                    cursor.getLong(cursor.getColumnIndex(City.COLUMN_WEATHER_ID)),
                    cursor.getString(cursor.getColumnIndex(City.COLUMN_TIMESTAMP)));
        }catch (CursorIndexOutOfBoundsException ex)
        {
            //Case
        }

        // close the db connection
        cursor.close();

        return city;
    }
    public List<City> getAllCities() {
        List<City> cities = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + City.TABLE_NAME + " ORDER BY " +
                City.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex(City.COLUMN_ID)));
                city.setName(cursor.getString(cursor.getColumnIndex(City.COLUMN_NAME)));
                city.setIdWeather(cursor.getLong(cursor.getColumnIndex(City.COLUMN_WEATHER_ID)));
                city.setTimestamp(cursor.getString(cursor.getColumnIndex(City.COLUMN_TIMESTAMP)));
                cities.add(city);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        for(City city: cities)
        {
            city.setWeather(getWeather(city.getIdWeather()));
        }

        // return list
        return cities;
    }
    //Delete the city and the weather associated
    public void deleteCity(City city)
    {
        deleteCity(city.getId());
        deleteWeather(city.getIdWeather());
    }
    private void deleteCity(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //delete the row
        db.delete(City.TABLE_NAME, City.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    private void deleteWeather(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //delete the row
        db.delete(Weather.TABLE_NAME, Weather.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public long insertWeather(Weather weather) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //id and timestamp will be inserted automatically.
        values.put(Weather.COLUMN_TEMP,weather.getTemp());
        values.put(Weather.COLUMN_TEMP_MIN,weather.getMinTemp());
        values.put(Weather.COLUMN_TEMP_MAX,weather.getMaxTemp());
        values.put(Weather.COLUMN_DESC,weather.getDesc());
        values.put(Weather.COLUMN_STATUS,weather.getStatus());


        long id = -1;
        try {
            // insert row
            id = db.insert(Weather.TABLE_NAME, null, values);
            Log.d("costa","id = "+id);
        }catch (SQLiteConstraintException ex){
            //case id already exists
            ex.printStackTrace();
        }

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public Weather getWeather(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Weather.TABLE_NAME,
                new String[]{Weather.COLUMN_ID, Weather.COLUMN_TEMP, Weather.COLUMN_TEMP_MIN,
                        Weather.COLUMN_TEMP_MAX,Weather.COLUMN_DESC,Weather.COLUMN_STATUS,Weather.COLUMN_TIMESTAMP},
                City.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare object
        Weather weather = new Weather(
                cursor.getLong(cursor.getColumnIndex(Weather.COLUMN_ID)),
                cursor.getDouble(cursor.getColumnIndex(Weather.COLUMN_TEMP)),
                cursor.getDouble(cursor.getColumnIndex(Weather.COLUMN_TEMP_MIN)),
                cursor.getDouble(cursor.getColumnIndex(Weather.COLUMN_TEMP_MAX)),
                cursor.getString(cursor.getColumnIndex(Weather.COLUMN_DESC)),
                cursor.getString(cursor.getColumnIndex(Weather.COLUMN_STATUS)),
                cursor.getString(cursor.getColumnIndex(Weather.COLUMN_TIMESTAMP))
        );

        // close the db connection
        cursor.close();

        return weather;
    }
    public int updateWeather(Weather weather) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Weather.COLUMN_TEMP,weather.getTemp());
        values.put(Weather.COLUMN_TEMP_MIN,weather.getMinTemp());
        values.put(Weather.COLUMN_TEMP_MAX,weather.getMaxTemp());
        values.put(Weather.COLUMN_DESC,weather.getDesc());
        values.put(Weather.COLUMN_STATUS,weather.getStatus());

        // updating row
        return db.update(Weather.TABLE_NAME, values, Weather.COLUMN_ID + " = ?",
                new String[]{String.valueOf(weather.getId())});
    }
}



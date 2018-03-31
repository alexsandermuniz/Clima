package com.clima.alexsander.clima.sqlite.models;

/**
 * Created by alexs on 29/03/2018.
 */

public class City {

    public static final String TABLE_NAME = "cities";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_WEATHER_ID = "weather_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private String name;
    private long id, idWeather;
    private String timestamp;
    private Weather weather;
    public City(){}
    public City(long id,String name) {
        this.id = id;
        this.name = name;
    }
    public City(long id,String name,long weather_id,String timestamp) {
        this.id = id;
        this.idWeather = weather_id;
        this.name = name;
        this.timestamp = timestamp;
    }

    public long getIdWeather() {
        return idWeather;
    }

    public void setIdWeather(long idWeather) {
        this.idWeather = idWeather;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_WEATHER_ID + " INTEGER,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY("+COLUMN_WEATHER_ID+")REFERENCES weather(id)"
                    + ")";

}

package com.clima.alexsander.clima.sqlite.models;

import com.clima.alexsander.clima.R;

/**
 * Created by alexs on 29/03/2018.
 */

public class Weather {

    public static final String TABLE_NAME = "weather";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TEMP = "temperature";
    public static final String COLUMN_TEMP_MIN = "temperature_min";
    public static final String COLUMN_TEMP_MAX = "temperature_max";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TIMESTAMP = "timestamp";


    private long id;
    private double temp,minTemp,maxTemp;
    private String desc,status,timeStamp;

    public Weather(){}
    public Weather(long id, double temp, double minTemp, double maxTemp, String desc, String status, String timeStamp) {
        this.id = id;
        this.temp = temp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.desc = desc;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatusTranslated()
    {
        String translated = "";
        switch (getStatus())
        {
            case "Clear":
                translated = "Ensolarado";
                break;
            case "Rain":
                translated = "Chuvoso";
                break;
            case "Clouds":
                translated = "Nublado";
                break;
            case "Thunderstorm":
                translated = "Trovejoso";
                break;

        }
        return  translated;
    }
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TEMP + " REAL,"
                    + COLUMN_TEMP_MIN + " REAL,"
                    + COLUMN_TEMP_MAX + " REAL,"
                    + COLUMN_DESC + " TEXT,"
                    + COLUMN_STATUS + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";


}

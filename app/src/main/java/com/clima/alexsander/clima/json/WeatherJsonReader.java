package com.clima.alexsander.clima.json;
import com.clima.alexsander.clima.sqlite.models.Weather;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexs on 30/03/2018.
 */

public class WeatherJsonReader {
    public static Weather read(JSONObject response)
    {

        try {
            Weather weather = new Weather();
            //Get JSONObject named 'main' and get some of its fields
            JSONObject mObject = response.getJSONObject("main");
            weather.setTemp(mObject.getDouble("temp"));
            weather.setMaxTemp(mObject.getDouble("temp_max"));
            weather.setMinTemp(mObject.getDouble("temp_min"));

            //Although 'weather' field is an array, just the first position gives enough info.
            JSONArray mArray = response.getJSONArray("weather");
            if(mArray.length()>0)
            {
                mObject = mArray.getJSONObject(0);
                weather.setStatus(mObject.getString("main"));
                weather.setDesc(mObject.getString("description"));
                weather.setDesc(weather.getDesc().replace("quebrados","quebradas"));
            }
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}

package com.clima.alexsander.clima.json;

import android.content.Context;

import com.clima.alexsander.clima.sqlite.models.CityList;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by alexs on 29/03/2018.
 */

public class GsonReader {
    public static CityList decodeJson(Context context, String fileName)
    {
        Gson gson = new Gson();
        String result = readFile(context,fileName);
        //CityList is used to transform the json content in a List of City and has to be a class.
        CityList data = gson.fromJson(result, CityList.class);
        return data;

    }
    public static String readFile(Context context,String fileName)
    {
        try {
            //read a file from assets folder.
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName), "UTF-8"));
            String content = "";
            String line;
            //while line!=null concatenates to result content.
            while ((line = reader.readLine()) != null)
            {
                content += line;
            }
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";


    }


}

package com.clima.alexsander.clima.sqlite.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexs on 29/03/2018.
 */

//This class only is necessary to read the 'data' from JSON file using GSON
public class CityList {
    @SerializedName("data")
    private List<City> cities;

    public CityList(List<City> cities) {
        this.cities = cities;
    }

    public List<City> getCities() {

        return cities;
    }
    //Returns an Array with the names of the cities in cities List
    public ArrayList<String> citiesNames()
    {
        ArrayList<String> names = new ArrayList<>();
        for(City c:getCities())
        {
            names.add(c.getName());
        }
        return names;
    }
}

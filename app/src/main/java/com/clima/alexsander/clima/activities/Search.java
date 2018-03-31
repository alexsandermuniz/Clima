package com.clima.alexsander.clima.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.clima.alexsander.clima.json.GsonReader;
import com.clima.alexsander.clima.R;
import com.clima.alexsander.clima.sqlite.models.City;
import com.clima.alexsander.clima.sqlite.models.CityList;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.select_city);
        setContentView(R.layout.search);

        ListView listView = findViewById(R.id.listView);

        //Get the list of city from the JSON file
        final CityList cityList = GsonReader.decodeJson(this,"city.list.json");


        //Get the list of cities names
        ArrayList<String> citiesNames = cityList.citiesNames();

        //Simple adapter to display the cities names
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                citiesNames );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Show the details for this item
                goToDetails(cityList.getCities().get(i));
            }
        });

        //Display and enable the back arrow in the toolbar
        enableHomeButton();

    }
    public void enableHomeButton(){
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToDetails(City city)
    {
        Intent it = new Intent(Search.this,Details.class);
        //Create a bundle to hold the params
        Bundle extra = new Bundle();
        //Set the params to send to another activity
        extra.putString("name",city.getName());
        extra.putLong("id",city.getId());
        //Set the params to the intent
        it.putExtras(extra);
        startActivity(it);
    }


}

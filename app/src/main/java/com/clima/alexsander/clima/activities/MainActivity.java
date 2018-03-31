package com.clima.alexsander.clima.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.clima.alexsander.clima.R;
import com.clima.alexsander.clima.adapters.ArrayAdapterMain;
import com.clima.alexsander.clima.sqlite.DatabaseHelper;
import com.clima.alexsander.clima.sqlite.models.City;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private ListView listView;
    private List<City> cities;
    private LinearLayout warningLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        //Layout to be displayed if there's no city in favorites list
        warningLayout = findViewById(R.id.warning_layout);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //Manager to acess the database
        db = new DatabaseHelper(this);
        //Get all cities from the database
        cities = db.getAllCities();
        if(cities.size() == 0)
        {
            //Display warning layout to inform about the empty list of city
            warningLayout.setVisibility(View.VISIBLE);
        }
        else{
            //Hide warning
            warningLayout.setVisibility(View.GONE);
            populateListView(cities);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            //Show the search activity
            goToSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    public void populateListView(final List<City> cities)
    {
        //Custom adapter created to display city and weather info
        ArrayAdapterMain arrayAdapter = new ArrayAdapterMain(this,cities);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Show the details for this item
                goToDetails(cities.get(i));
            }
        });
    }
    public void goToDetails(City city)
    {
        Intent it = new Intent(MainActivity.this,Details.class);
        //Create a bundle to hold the params
        Bundle extra = new Bundle();
        //Set the params to send to another activity
        extra.putString("name",city.getName());
        extra.putLong("id",city.getId());
        //Set the params to the intent
        it.putExtras(extra);
        startActivity(it);
    }
    public void goToSearch()
    {
        Intent it = new Intent(MainActivity.this,Search.class);
        startActivity(it);
    }


}

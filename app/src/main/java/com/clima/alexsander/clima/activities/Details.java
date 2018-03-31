package com.clima.alexsander.clima.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.clima.alexsander.clima.json.ApiConnect;
import com.clima.alexsander.clima.R;
import com.clima.alexsander.clima.json.WeatherJsonReader;
import com.clima.alexsander.clima.sqlite.DatabaseHelper;
import com.clima.alexsander.clima.sqlite.models.City;
import com.clima.alexsander.clima.sqlite.models.Weather;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by alexs on 29/03/2018.
 */

public class Details extends AppCompatActivity {

    private DatabaseHelper db;
    private ProgressDialog progressDialog;
    private City city;
    private long idWeather;
    private boolean favorite;
    private LinearLayout warningLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.details);
        setContentView(R.layout.details);
        //Layout to be displayed if there's no city in favorites list
        warningLayout = findViewById(R.id.warning_layout);
        //Manager to acess the database
        db = new DatabaseHelper(this);
        //Get params from the previous activity
        Bundle params = getIntent().getExtras();
        if(params!=null)
        {
            city = createCityFromParams(params);
            //If city has weather information then is in the favorite list.
            if(city.getIdWeather()!=-1)
            {
                favorite = true;
            }
            handleApiCall(city.getId(),city.getName());
        }

        //Display and enable the back arrow in the toolbar
        enableHomeButton();

    }
    public City createCityFromParams(Bundle params)
    {
        long id = params.getLong("id");
        String name = params.getString("name");
        //Get city from database with the id
        city = db.getCity(id);
        //If city != null so was found in the database and has weather informations.
        if(city!=null)
        {
            idWeather = city.getIdWeather();
        }
        //If not found, its not in the favorite list and has no weather info yet.
        else
        {
            idWeather = -1;
            //Create a new city and set idWeather -1.
            city = new City(id,name);
            city.setIdWeather(idWeather);
        }
        return city;
    }
    public void enableHomeButton(){
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        //If its a favorite change icon
        if(favorite)
        {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        if(item.getItemId() == R.id.favorite)
        {
            favorite = !favorite;
            //Change icon corresponded to the favorite
            if(favorite)
            {
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
            }
            else{
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
            }
            //Change database
            alterDbOnFavorite();
        }
        return super.onOptionsItemSelected(item);
    }
    private void showProgressDialog(Context c) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(c);
        }
        progressDialog.setMessage(getString(R.string.wait) +" ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }
    public void handleApiCall(final long id, final String city_name)
    {
        //Shows the progress dialog while communicate with the api
        showProgressDialog(this);
        RequestParams rp = new RequestParams();
        ApiConnect.post(String.valueOf(id),rp, new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                super.onFinish();
                //On finish closes the dialog
                dismissProgressDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                //If it fail, shows connectivity warning
                warningLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                //Decode the weather info from the JSON response
                Weather weather = WeatherJsonReader.read(response);

                //If it's already on favorite list, it is updated
                if(idWeather!=-1)
                {
                    weather.setId(idWeather);
                    db.updateWeather(weather);
                }
                //Set the weather to the correspond city
                city.setWeather(weather);

                //Fill the textViews and imgView
                fillTextAndImageInfo(weather,city_name);
            }
        });
    }
    private void fillTextAndImageInfo(Weather weather,String city_name)
    {
        ((TextView)findViewById(R.id.txtCity)).setText(""+city_name);
        ((TextView)findViewById(R.id.txtTemp)).setText(""+weather.getTemp()+" °C");
        ((TextView)findViewById(R.id.txtTempMax)).setText(getString(R.string.temp_max ) +":   "+weather.getMaxTemp()+" °C");
        ((TextView)findViewById(R.id.txtTempMin)).setText(getString(R.string.temp_min) +":   "+weather.getMinTemp()+" °C");
        ((TextView)findViewById(R.id.txtDesc)).setText(""+firstLetterUppercase(weather.getDesc()));

        ImageView img = findViewById(R.id.imgStatus);
        img.setImageResource(getResourceByStatus(weather.getStatus()));
    }

    private String firstLetterUppercase(String desc) {
        //Get the first letter and uppercase
        String first = desc.substring(0,1).toUpperCase();
        //Concatenate with the rest of the string
        desc = first+desc.substring(1,desc.length());
        return desc;
    }
    //Get the drawable image that correspond to the status
    public int getResourceByStatus(String status) {
        int resource = -1;
        switch (status)
        {
            case "Clear":
                resource = R.drawable.clearb;
                break;
            case "Rain":
                resource = R.drawable.rainb;
                break;
            case "Clouds":
                resource = R.drawable.cloudsb;
                break;
            case "Thunderstorm":
                resource = R.drawable.thunderstormb;
                break;

        }

        return resource;
    }


    public void alterDbOnFavorite()
    {
        //In the case of no internet connectivity the weather will be null
        if(city.getWeather()!=null)
        {
            //If its a favorite, than save
            if(favorite)
            {
                db.insertCity(city);
            }
            //Else delete from favorite list
            else{
                db.deleteCity(city);
            }
        }
    }
}

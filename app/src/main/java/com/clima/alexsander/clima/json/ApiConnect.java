package com.clima.alexsander.clima.json;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * Created by alexs on 29/03/2018.
 */

public class ApiConnect {
    private static final String APP_ID = "&appid=2bac87e0cb16557bff7d4ebcbaa89d2f";
    private static final String CONFIGS = "&lang=pt&units=metric";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void post(String CITY_ID, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        //Make an a assynchttp post with the url of the API with the params
        client.post("http://api.openweathermap.org/data/2.5/weather?id="+CITY_ID+APP_ID+CONFIGS, params, responseHandler);
    }
}
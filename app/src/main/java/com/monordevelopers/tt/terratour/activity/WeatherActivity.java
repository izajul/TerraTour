package com.monordevelopers.tt.terratour.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.monordevelopers.tt.terratour.GeocodingSearch.findLocationFromLatLan.GetLocationByLatLanAPI;
import com.monordevelopers.tt.terratour.GeocodingSearch.findLocationFromLatLan.GetLocationByLatLonPojo;
import com.monordevelopers.tt.terratour.GeocodingSearch.findLocationFromLatLan.Result;
import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.adapter.DayForecastAdapter;
import com.monordevelopers.tt.terratour.adapter.HourlyForcastAdapter;
import com.monordevelopers.tt.terratour.weatherPojo.dayforcastpojo.DayForecastAPI;
import com.monordevelopers.tt.terratour.weatherPojo.dayforcastpojo.Days10ForecastPojo;
import com.monordevelopers.tt.terratour.weatherPojo.dayforcastpojo.Forecastday_;
import com.monordevelopers.tt.terratour.weatherPojo.hourlyforecastpojo.HourlyForecast;
import com.monordevelopers.tt.terratour.weatherPojo.hourlyforecastpojo.HourlyForecastAPI;
import com.monordevelopers.tt.terratour.weatherPojo.hourlyforecastpojo.HourlyForecastPojo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    RecyclerView hourlyrecyclerView,dailyrecyclerview;
    Toolbar toolbar;
    ActionBar actionbar;
    DayForecastAPI dayForecastApi;
    Days10ForecastPojo daysforcast;
    HourlyForecastAPI hourlyforcastApi;
    HourlyForecastPojo hourlyforcast;
    SharedPreferences sharedPreferences ;
    GetLocationByLatLanAPI getlocationApi;
    GetLocationByLatLonPojo getLocationByLatLonPojo;

    String country,city;

    HourlyForcastAdapter hourAdapt;
    String MyLat;String MyLong;
    String mLatlan;

    Menu mMenu;
    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_weather );
        sharedPreferences = getSharedPreferences( "terratour", MODE_PRIVATE );
        try {
            getlocationbyLatLon();
        } catch (IOException e) {e.printStackTrace();}

        hourlyrecyclerView = (RecyclerView) findViewById( R.id.hourly_forecast_recycle_View );
        dailyrecyclerview = (RecyclerView) findViewById( R.id.day_forecast_recycle_View );
        toolbar = (Toolbar) findViewById( R.id.weather_toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onBackPressed();}
        });
        actionbar = getSupportActionBar();
        initilize();getHourlyForecastData();getDayforcastData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.weather_add_location, menu );
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() ==  R.id.addLocation){
            intentStartSearchbox();
        }
        return super.onOptionsItemSelected( item );
    }

    private void initilize() {
        Retrofit retrofitdays =new Retrofit.Builder()
                .baseUrl("http://api.wunderground.com/api/3828843fb10a4749/forecast10day/q/")
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        dayForecastApi = retrofitdays.create( DayForecastAPI.class );
        Retrofit retrofithour =new Retrofit.Builder()
                .baseUrl("http://api.wunderground.com/api/3828843fb10a4749/hourly/q/")
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        hourlyforcastApi = retrofithour.create( HourlyForecastAPI.class);
    }
    private void getDayforcastData(){
        String s = mLatlan+".json";
        final Call<Days10ForecastPojo>days10ForecastPojoCall = dayForecastApi.get10DaysForecast( s );
        days10ForecastPojoCall.enqueue( new Callback<Days10ForecastPojo>() {
            @Override
            public void onResponse(Call<Days10ForecastPojo> call, Response<Days10ForecastPojo> response) {
                daysforcast = response.body();
                try{
                    ArrayList<Forecastday_>forecastday = (ArrayList<Forecastday_>) daysforcast.getForecast().getSimpleforecast().getForecastday();
                    if (forecastday.size()>0){
                        forecastday.remove( 0 );
                    }
                    DayForecastAdapter dayadapter = new DayForecastAdapter( forecastday );
                    dailyrecyclerview.setAdapter( dayadapter );
                }catch (NullPointerException e){}
            }

            @Override
            public void onFailure(Call<Days10ForecastPojo> call, Throwable t) {

            }
        } );
    }
    private void getHourlyForecastData(){
        String s = mLatlan+".json";
        final Call<HourlyForecastPojo>hourlyForecastPojoCall = hourlyforcastApi.getHourlyforcast( s );
        hourlyForecastPojoCall.enqueue( new Callback<HourlyForecastPojo>() {
            @Override
            public void onResponse(Call<HourlyForecastPojo> call, Response<HourlyForecastPojo> response) {
                hourlyforcast = response.body();
                try {
                    ArrayList<HourlyForecast> hourlyForecasts = (ArrayList<HourlyForecast>) hourlyforcast.getHourlyForecast();
                    ArrayList<HourlyForecast> hour24Forecasts = new ArrayList<HourlyForecast>(  );
                    for (int i = 0; i < 24; i++) {
                        hour24Forecasts.add( hourlyForecasts.get( i ) );
                    }
                    hourAdapt = new HourlyForcastAdapter( hour24Forecasts );
                    hourlyrecyclerView.setAdapter( hourAdapt );
                }catch (NullPointerException e){}
            }

            @Override
            public void onFailure(Call<HourlyForecastPojo> call, Throwable t) {

            }
        } );
    }

    private void getlocationbyLatLon() throws IOException {
        MyLat = sharedPreferences.getString( "lat","" );
        MyLong = sharedPreferences.getString( "lon","" );
        mLatlan = MyLat+","+MyLong;
        setLocationAndTime();
    }

    private void intentStartSearchbox(){
        try {
            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
        } catch (GooglePlayServicesNotAvailableException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                place = PlaceAutocomplete.getPlace(this, data);
                LatLng latLng = place.getLatLng();
                mLatlan = latLng.latitude+","+latLng.longitude;
                setLocationAndTime();
                getHourlyForecastData();getDayforcastData();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    private void setLocationAndTime(){
        Retrofit retrofitdays =new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/geocode/")
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        getlocationApi = retrofitdays.create( GetLocationByLatLanAPI.class );

        String query = "json?address="+mLatlan+"&key=AIzaSyA4XRdyaoGiLz5sPboYrTfpZHqyi49Azyc";
        Call<GetLocationByLatLonPojo>getLocationByLatLonPojoCall = getlocationApi.getLocationCall( query );
        getLocationByLatLonPojoCall.enqueue( new Callback<GetLocationByLatLonPojo>() {
            @Override
            public void onResponse(Call<GetLocationByLatLonPojo> call, Response<GetLocationByLatLonPojo> response) {
                getLocationByLatLonPojo = response.body();

                try{
                    Result result = getLocationByLatLonPojo.getResults().get( 0 );
                    String dummy = result.getAddressComponents().get( 2 ).getLongName();
                    String finalString = dummy.replaceAll( "District","" ).trim();
                    actionbar.setTitle(finalString+","+result.getAddressComponents()
                            .get( result.getAddressComponents().size()-1 )
                            .getShortName());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
                    String formattedDate = dateFormat.format(new java.util.Date( )).toString();
                    actionbar.setSubtitle( formattedDate );
                }catch (NullPointerException e){}
            }

            @Override
            public void onFailure(Call<GetLocationByLatLonPojo> call, Throwable t) {

            }
        } );
    }

}

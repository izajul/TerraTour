package com.monordevelopers.tt.terratour.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.nearbylocationpojo.LocationPojo;
import com.monordevelopers.tt.terratour.nearbylocationpojo.NearbyLocationDetailsAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NearbyLocationDetailsActivity extends AppCompatActivity {

    String placeId="";
    TextView title,address,weekstatus,phone;
    NearbyLocationDetailsAPI locationDetailsApi;
    LocationPojo locationpojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_nearby_location_details );
        placeId = getIntent().getStringExtra( "placeid" );
        title= (TextView) findViewById( R.id.Title_TV );
        address= (TextView) findViewById( R.id.Address_TV );
        weekstatus= (TextView) findViewById( R.id.weekend_day_TV );
        phone= (TextView) findViewById( R.id.Phone_TV );
        networkInitializer();
        getLocationDetails();
    }

    private void networkInitializer() {
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/details/")
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        locationDetailsApi = retrofit.create( NearbyLocationDetailsAPI.class );
    }
    private void getLocationDetails(){
        String url = "json?placeid="+placeId+"&key=AIzaSyDFcUV2axWvUZgFj6HthZgob0J0kHRepBQ";
        final Call<LocationPojo> locationPojoCall = locationDetailsApi.getLocation( url );
        locationPojoCall.enqueue( new Callback<LocationPojo>() {
            @Override
            public void onResponse(Call<LocationPojo> call, Response<LocationPojo> response) {
                locationpojo = response.body();
                try{
                    title.setText( locationpojo.getResult().getName() );
                    address.setText( locationpojo.getResult().getFormattedAddress() );
                    phone.setText( locationpojo.getResult().getInternationalPhoneNumber() );

                    ArrayList<String>weekdayText = (ArrayList<String>) locationpojo.getResult()
                            .getOpeningHours().getWeekdayText();
                    String day ="";
                    for (String s:weekdayText){
                        day+=s+"\n";
                    }weekstatus.setText( "Week Day \n"+day );

                }catch (NullPointerException e){}
            }

            @Override
            public void onFailure(Call<LocationPojo> call, Throwable t) {

            }
        } );
    }
}

package com.monordevelopers.tt.terratour.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.adapter.NearbyListAdapter;
import com.monordevelopers.tt.terratour.nearbypojo.MyPojoNearBy;
import com.monordevelopers.tt.terratour.nearbypojo.NearbyApi;
import com.monordevelopers.tt.terratour.nearbypojo.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowNearByDetailsActivity extends AppCompatActivity {
    TextView nearByTitle;
    ListView nearbyListView;
    String mLatLang;

    NearbyApi nearbyApi;
    MyPojoNearBy myPojoNearBy;
    String keyword= "",mRadius = "5000";
    ArrayList<Result> mNearbyResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_show_near_by_details );
        nearByTitle = (TextView) findViewById( R.id.nearbyTitle_TV );
        nearbyListView = (ListView) findViewById( R.id.nearBy_List_View );
        nearByTitle.setText( getIntent().getStringExtra( "title" ) );
        mLatLang = getIntent().getStringExtra( "latlang" );
        keyword = getIntent().getStringExtra( "keyword" );

        iniTializeRetrofit();
        getNearByData();

        nearbyListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Result result = mNearbyResults.get( position );
                try{
                    startActivity( new Intent( ShowNearByDetailsActivity.this,NearbyLocationDetailsActivity.class )
                            .putExtra( "placeid",result.getPlaceId()));
                }catch (NullPointerException e){}
            }
        } );
    }

    private void iniTializeRetrofit() {
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        nearbyApi = retrofit.create( NearbyApi.class );
    }

    public void getNearByData(){
        String s ="json?location="+mLatLang+"&radius=5000"+keyword+"&key=AIzaSyDFcUV2axWvUZgFj6HthZgob0J0kHRepBQ";
        final Call<MyPojoNearBy> myPojoNearByCall = nearbyApi.getMyNearBy(s);

        myPojoNearByCall.enqueue( new Callback<MyPojoNearBy>() {
            @Override
            public void onResponse(Call<MyPojoNearBy> call, Response<MyPojoNearBy> response) {
                myPojoNearBy = response.body();
                try{
                    Gson gson = new Gson();
                    String json = gson.toJson(myPojoNearBy);

                    mNearbyResults = (ArrayList<Result>) myPojoNearBy.getResults();
                    NearbyListAdapter nearbyListAdapter = new NearbyListAdapter( ShowNearByDetailsActivity.this,mNearbyResults );
                    nearbyListView.setAdapter( nearbyListAdapter );
                }catch (NullPointerException e){}

            }

            @Override
            public void onFailure(Call<MyPojoNearBy> call, Throwable t) {
                Toast.makeText( ShowNearByDetailsActivity.this, "Connection Fail Try Again", Toast.LENGTH_SHORT ).show();
            }
        } );
    }


}

package com.monordevelopers.tt.terratour.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.activity.ShowNearByDetailsActivity;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class NearBy_Fragment extends Fragment {
    Button bank,cafe,restaurant,hospital,parkLek;
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    Context mContext;
    Location mLocation;
    SharedPreferences sharedPreferences;

    public NearBy_Fragment() { }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = inflater.getContext();
        getLocationByGPS_NETWORK(inflater.getContext());
        View view = inflater.inflate( R.layout.fragment_near_by_, container, false );
        bank = (Button) view.findViewById( R.id.nearby_button_bank );
        sharedPreferences = this.getActivity().getSharedPreferences( "terratour", MODE_PRIVATE );
        bank.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        bank.setBackgroundResource( R.drawable.nearby_options_buttions_onclick );
                        break;
                    case MotionEvent.ACTION_UP:
                        bank.setBackgroundResource( R.drawable.neary_option_buttons_background );
                        if (mLocation!=null){
                            String latlang = String.valueOf(mLocation.getLatitude())+","+String.valueOf(mLocation.getLongitude());
                            startActivity( new Intent( mContext,
                                    ShowNearByDetailsActivity.class )
                                    .putExtra( "latlang",latlang )
                                    .putExtra( "title","All NearBy Banks" )
                                    .putExtra( "keyword","&keyword=Bank&keyword=ATM" ));
                        }
                }return false;
            }
        } );
        cafe = (Button) view.findViewById( R.id.nearby_button_cafe );
        cafe.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        cafe.setBackgroundResource( R.drawable.nearby_options_buttions_onclick );
                        break;
                    case MotionEvent.ACTION_UP:
                        cafe.setBackgroundResource( R.drawable.neary_option_buttons_background );
                        if (mLocation!=null){
                            String latlang = String.valueOf(mLocation.getLatitude())+","+String.valueOf(mLocation.getLongitude());
                            startActivity( new Intent( mContext,
                                    ShowNearByDetailsActivity.class )
                                    .putExtra( "latlang",latlang )
                                    .putExtra( "title","All NearBy Cafe" )
                                    .putExtra( "keyword","&keyword=Cafe" ));
                        }
                }return false;
            }
        } );
        restaurant = (Button) view.findViewById( R.id.nearby_button_Restaurants);
        restaurant.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        restaurant.setBackgroundResource( R.drawable.nearby_options_buttions_onclick );
                        break;
                    case MotionEvent.ACTION_UP:
                        restaurant.setBackgroundResource( R.drawable.neary_option_buttons_background );
                        if (mLocation!=null){
                            String latlang = String.valueOf(mLocation.getLatitude())+","+String.valueOf(mLocation.getLongitude());
                            startActivity( new Intent( mContext,
                                    ShowNearByDetailsActivity.class )
                                    .putExtra( "latlang",latlang )
                                    .putExtra( "title","All NearBy Restaurant & Hotel" )
                                    .putExtra( "keyword","&keyword=Restaurant&keyword=Hotel" ));
                        }
                }return false;
            }
        } );
        hospital = (Button) view.findViewById( R.id.nearby_button_Hospital);
        hospital.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        hospital.setBackgroundResource( R.drawable.nearby_options_buttions_onclick );
                        break;
                    case MotionEvent.ACTION_UP:
                        hospital.setBackgroundResource( R.drawable.neary_option_buttons_background );
                        if (mLocation!=null){
                            String latlang = String.valueOf(mLocation.getLatitude())+","+String.valueOf(mLocation.getLongitude());
                            startActivity( new Intent( mContext,
                                    ShowNearByDetailsActivity.class )
                                    .putExtra( "latlang",latlang )
                                    .putExtra( "title","All NearBy Hospital & Medical" )
                                    .putExtra( "keyword","&keyword=Hospital&keyword=Medical" ));
                        }
                }return false;
            }
        } );
        parkLek = (Button) view.findViewById( R.id.nearby_button_Lake_Park);
        parkLek.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        parkLek.setBackgroundResource( R.drawable.nearby_options_buttions_onclick );
                        break;
                    case MotionEvent.ACTION_UP:
                        parkLek.setBackgroundResource( R.drawable.neary_option_buttons_background );
                        if (mLocation!=null){
                            String latlang = String.valueOf(mLocation.getLatitude())+
                                    ","+String.valueOf(mLocation.getLongitude());
                            startActivity( new Intent( mContext,
                                    ShowNearByDetailsActivity.class )
                                    .putExtra( "latlang",latlang )
                                    .putExtra( "title","All NearBy Park & Lake" )
                                    .putExtra( "keyword","&keyword=Park&keyword=Lake" ));
                        }
                }return false;
            }
        } );
        return view;
    }


    public void getLocationByGPS_NETWORK(final Context context) {
        mLocationManager = (LocationManager) context.getSystemService( LOCATION_SERVICE );
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString( "lat", String.valueOf( location.getLatitude() ));
                editor.putString( "lon", String.valueOf( location.getLongitude() ));
                editor.commit();
                mLocationManager.removeUpdates( mLocationListener );
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                new AlertDialog.Builder(mContext)
                        .setTitle( " Check Location Settings " )
                        .setMessage( "Do you want Enable GPS? " )
                        .setCancelable( true )
                        .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity( new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS ) );
                            }
                        } )
                        .show();
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION )
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( context,
                    Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission( context,
                            Manifest.permission.INTERNET ) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions( new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET}, 10 );
                return;
            }else { setConfiger(); }
        } else { setConfiger(); }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setConfiger();
                }
                return;
        }
    }

    private void setConfiger() {
        mLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mLocationListener );
    }

}

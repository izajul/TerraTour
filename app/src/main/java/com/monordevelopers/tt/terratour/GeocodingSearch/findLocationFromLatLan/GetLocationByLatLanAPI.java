package com.monordevelopers.tt.terratour.GeocodingSearch.findLocationFromLatLan;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetLocationByLatLanAPI {
    @GET
    Call<GetLocationByLatLonPojo>getLocationCall(@Url String url);
}

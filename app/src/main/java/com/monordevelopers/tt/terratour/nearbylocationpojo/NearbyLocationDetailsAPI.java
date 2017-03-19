package com.monordevelopers.tt.terratour.nearbylocationpojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NearbyLocationDetailsAPI {
    @GET
    Call<LocationPojo>getLocation(@Url String url);
}


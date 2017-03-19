package com.monordevelopers.tt.terratour.nearbypojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NearbyApi {
    @GET
    Call<MyPojoNearBy> getMyNearBy(@Url String url);
}

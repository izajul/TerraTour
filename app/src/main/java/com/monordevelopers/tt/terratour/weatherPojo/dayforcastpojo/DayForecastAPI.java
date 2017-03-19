package com.monordevelopers.tt.terratour.weatherPojo.dayforcastpojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DayForecastAPI {
    @GET
    Call<Days10ForecastPojo>get10DaysForecast(@Url String url);
}

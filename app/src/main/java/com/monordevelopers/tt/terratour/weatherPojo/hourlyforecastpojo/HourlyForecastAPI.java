package com.monordevelopers.tt.terratour.weatherPojo.hourlyforecastpojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface HourlyForecastAPI {
    @GET
    Call<HourlyForecastPojo>getHourlyforcast(@Url String url );
}

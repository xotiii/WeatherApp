package com.danchua.weatherapp.server;

import androidx.lifecycle.LiveData;

import com.danchua.weatherapp.server.response.WeatherResponse;
import com.danchua.weatherapp.server.utils.ApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dan Chua
 */
public interface WeatherAppService {

    @GET("weather?")
    LiveData<ApiResponse<WeatherResponse>> getWeather(@Query("lat") String lat,
                                                      @Query("lon") String lon,
                                                      @Query("units") String unit,
                                                      @Query("APPID") String apiKey);

}

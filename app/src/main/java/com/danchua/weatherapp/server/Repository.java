package com.danchua.weatherapp.server;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.danchua.weatherapp.server.response.WeatherResponse;
import com.danchua.weatherapp.utils.AppExecutors;
import com.danchua.weatherapp.utils.Resource;

import static com.danchua.weatherapp.utils.Constants.API_KEY;
import static com.danchua.weatherapp.utils.Constants.SPREF_CITY_KEY;
import static com.danchua.weatherapp.utils.Constants.SPREF_CLOUDS_KEY;
import static com.danchua.weatherapp.utils.Constants.SPREF_COUNTRY_KEY;
import static com.danchua.weatherapp.utils.Constants.SPREF_DATE_KEY;
import static com.danchua.weatherapp.utils.Constants.SPREF_HUMIDITY_KEY;
import static com.danchua.weatherapp.utils.Constants.SPREF_ICON_KEY;
import static com.danchua.weatherapp.utils.Constants.SPREF_MAXTEMP_KEY;
import static com.danchua.weatherapp.utils.Constants.SPREF_MINTEMP_KEY;
import static com.danchua.weatherapp.utils.Constants.SPREF_TEMP_KEY;
import static com.danchua.weatherapp.utils.Constants.SPREF_UNIT_KEY;
import static com.danchua.weatherapp.utils.Constants.SPREF_WEATHER_KEY;
import static com.danchua.weatherapp.utils.Constants.SPREF_WIND_KEY;

/**
 * Created by Dan Chua
 */
public class Repository {

    private AppExecutors mAppExecutors;
    private SharedPreferences mSharedPreferences;
    private WeatherAppService mWeatherAppService;

    public Repository(AppExecutors appExecutors, SharedPreferences sharedPreferences, WeatherAppService weatherAppService) {
        this.mAppExecutors = appExecutors;
        this.mSharedPreferences = sharedPreferences;
        this.mWeatherAppService = weatherAppService;
    }

    public LiveData<Resource<WeatherResponse>> getWeather(String lat, String lon) {
        MediatorLiveData<Resource<WeatherResponse>> mld = new MediatorLiveData<>();
        mld.postValue(Resource.loading(null));
        mld.addSource(mWeatherAppService.getWeather(lat, lon, getUnit(), API_KEY), requestResult -> {
            if (requestResult.isSuccessful()) {
                mld.postValue(Resource.success(requestResult.body));
            } else {
                mld.postValue(Resource.serverError(requestResult.errorMessage, null));
            }
        });
        return mld;
    }

    public void setLastWeather(WeatherResponse weatherResponse) {
        mSharedPreferences.edit().putInt(SPREF_DATE_KEY, weatherResponse.getDt())
                .putString(SPREF_CITY_KEY, weatherResponse.getName())
                .putString(SPREF_COUNTRY_KEY, weatherResponse.getSys().getCountry())
                .putString(SPREF_ICON_KEY, weatherResponse.getWeather().get(0).getIcon())
                .putString(SPREF_WEATHER_KEY, weatherResponse.getWeather().get(0).getMain())
                .putString(SPREF_TEMP_KEY, String.valueOf(weatherResponse.getMain().getTemp()))
                .putString(SPREF_MAXTEMP_KEY, String.valueOf(weatherResponse.getMain().getTempMax()))
                .putString(SPREF_MINTEMP_KEY, String.valueOf(weatherResponse.getMain().getTempMin()))
                .putInt(SPREF_CLOUDS_KEY, weatherResponse.getClouds().getAll())
                .putString(SPREF_WIND_KEY, String.valueOf(weatherResponse.getWind().getSpeed()))
                .putInt(SPREF_HUMIDITY_KEY, weatherResponse.getMain().getHumidity())
                .commit();
    }

    public WeatherResponse getLastWeather() {
        WeatherResponse weatherResponse = new WeatherResponse(
                mSharedPreferences.getInt(SPREF_DATE_KEY, 0),
                mSharedPreferences.getString(SPREF_CITY_KEY, ""),
                mSharedPreferences.getString(SPREF_COUNTRY_KEY, ""),
                mSharedPreferences.getString(SPREF_ICON_KEY, ""),
                mSharedPreferences.getString(SPREF_WEATHER_KEY, ""),
                Double.parseDouble(mSharedPreferences.getString(SPREF_TEMP_KEY, "0")),
                Double.parseDouble(mSharedPreferences.getString(SPREF_MAXTEMP_KEY, "0")),
                Double.parseDouble(mSharedPreferences.getString(SPREF_MINTEMP_KEY, "0")),
                mSharedPreferences.getInt(SPREF_CLOUDS_KEY, 0),
                Double.parseDouble(mSharedPreferences.getString(SPREF_WIND_KEY, "0")),
                mSharedPreferences.getInt(SPREF_HUMIDITY_KEY, 0));
        return weatherResponse;
    }

    public void setUnit(String unit) {
        mSharedPreferences.edit().putString(SPREF_UNIT_KEY, unit).commit();
    }

    public String getUnit() {
        return mSharedPreferences.getString(SPREF_UNIT_KEY, "metric");
    }

}

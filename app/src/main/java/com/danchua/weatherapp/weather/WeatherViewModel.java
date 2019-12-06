package com.danchua.weatherapp.weather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.danchua.weatherapp.server.Repository;
import com.danchua.weatherapp.server.response.WeatherResponse;
import com.danchua.weatherapp.utils.Resource;

/**
 * Created by Dan Chua
 */
public class WeatherViewModel extends ViewModel {

    private Repository mRepository;

    public void init(Repository repository) {
        this.mRepository = repository;
    }

    public void setUnit(String unit) {
        mRepository.setUnit(unit);
    }

    public String getUnit() {
        return mRepository.getUnit();
    }

    public LiveData<Resource<WeatherResponse>> getWeather(String lat, String lon) {
        return mRepository.getWeather(lat, lon);
    }

    public void setLastWeather(WeatherResponse weatherResponse) {
        mRepository.setLastWeather(weatherResponse);
    }

    public WeatherResponse getLastWeather() {
        return mRepository.getLastWeather();
    }

}

package com.danchua.weatherapp;

import android.app.Application;
import android.content.SharedPreferences;

import com.danchua.weatherapp.server.Repository;
import com.danchua.weatherapp.server.WeatherAppService;
import com.danchua.weatherapp.server.utils.LiveDataCallAdapterFactory;
import com.danchua.weatherapp.utils.AppExecutors;
import com.danchua.weatherapp.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.danchua.weatherapp.utils.Constants.SPREF_NAME;

/**
 * Created by Dan Chua
 */

public class WeatherApp extends Application {

    private AppExecutors mAppExecutors;

    private Gson mGson;

    private Repository mRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppExecutors = new AppExecutors();
        mGson = new GsonBuilder().setPrettyPrinting().create();

        mRepository = new Repository(mAppExecutors,
                getSharedPreferences(SPREF_NAME, MODE_PRIVATE),
                retrofit().create(WeatherAppService.class));

    }

    private Retrofit retrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        return new Retrofit.Builder()
                .baseUrl(Constants.WEATHER_API_URL)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(builder.build())
                .build();
    }

    public Repository getRepository() {
        return this.mRepository;
    }

}

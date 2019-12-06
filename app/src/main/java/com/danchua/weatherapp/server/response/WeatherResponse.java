package com.danchua.weatherapp.server.response;

import com.danchua.weatherapp.models.Clouds;
import com.danchua.weatherapp.models.Coord;
import com.danchua.weatherapp.models.Main;
import com.danchua.weatherapp.models.Sys;
import com.danchua.weatherapp.models.Weather;
import com.danchua.weatherapp.models.Wind;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan Chua
 */
public class WeatherResponse {

    @SerializedName("coord")
    @Expose
    private Coord coord;
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("visibility")
    @Expose
    private Integer visibility;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("sys")
    @Expose
    private Sys sys;
    @SerializedName("timezone")
    @Expose
    private Integer timezone;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose
    private Integer cod;

    public WeatherResponse() {

    }

    public WeatherResponse(int date, String city, String country, String icon,
                           String weatherName, Double temp, Double maxTemp,
                           Double minTemp, int clouds, Double wind, int humidity) {

        Sys sys = new Sys();
        sys.setCountry(country);

        List<Weather> weatherList = new ArrayList<>();
        Weather weather = new Weather();
        weather.setIcon(icon);
        weather.setMain(weatherName);
        weatherList.add(weather);

        Main main = new Main();
        main.setTemp(temp);
        main.setTempMax(maxTemp);
        main.setTempMin(minTemp);
        main.setHumidity(humidity);

        Clouds clouds1 = new Clouds();
        clouds1.setAll(clouds);

        Wind wind1 = new Wind();
        wind1.setSpeed(wind);

        this.setDt(date);
        this.setName(city);
        this.setSys(sys);
        this.setWeather(weatherList);
        this.setMain(main);
        this.setClouds(clouds1);
        this.setWind(wind1);


    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

}

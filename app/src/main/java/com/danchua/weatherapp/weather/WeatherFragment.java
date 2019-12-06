package com.danchua.weatherapp.weather;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.danchua.weatherapp.R;
import com.danchua.weatherapp.WeatherApp;
import com.danchua.weatherapp.server.response.WeatherResponse;
import com.danchua.weatherapp.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;
import static com.danchua.weatherapp.utils.Constants.FAHRENHEIT;
import static com.danchua.weatherapp.utils.Constants.METRIC_UNIT;

/**
 * Created by Dan Chua
 */
public class WeatherFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, LocationListener {

    private WeatherViewModel mWeatherViewModel;

    private LocationManager mLocationManager;

    private SwipeRefreshLayout mSwipeLayout;

    private CardView mCardViewUnit;
    private AppCompatTextView mTextViewUnit;

    private AppCompatImageView mImageViewWeather;

    private AppCompatTextView mTextViewLocation;
    private AppCompatTextView mTextViewDate;
    private AppCompatTextView mTextViewWeatherMain;
    private AppCompatTextView mTextViewTemp;

    private AppCompatTextView mTextViewMaxTemp;
    private AppCompatTextView mTextViewMinTemp;

    private AppCompatTextView mTextViewClouds;
    private AppCompatTextView mTextViewWind;
    private AppCompatTextView mTextViewHumidity;

    /**
     *  Initialize UI Components
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        mSwipeLayout = view.findViewById(R.id.fragment_weather_swiperefresh_layout);
        mSwipeLayout.setOnRefreshListener(this);

        mCardViewUnit = view.findViewById(R.id.fragment_weather_cardview_unit);
        mCardViewUnit.setOnClickListener(this);

        mTextViewUnit = view.findViewById(R.id.fragment_weather_textview_unit);

        mImageViewWeather = view.findViewById(R.id.fragment_weather_imageview);

        mTextViewLocation = view.findViewById(R.id.fragment_weather_textview_location);
        mTextViewDate = view.findViewById(R.id.fragment_weather_textview_date);
        mTextViewWeatherMain = view.findViewById(R.id.fragment_weather_textview_weather_main);
        mTextViewTemp = view.findViewById(R.id.fragment_weather_textview_temperature);

        mTextViewMaxTemp = view.findViewById(R.id.fragment_weather_textview_max_temp);
        mTextViewMinTemp = view.findViewById(R.id.fragment_weather_textview_min_temp);

        mTextViewClouds = view.findViewById(R.id.fragment_weather_textview_clouds);
        mTextViewWind = view.findViewById(R.id.fragment_weather_textview_wind);
        mTextViewHumidity = view.findViewById(R.id.fragment_weather_textview_humidity);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mWeatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        mWeatherViewModel.init(((WeatherApp) getActivity().getApplication()).getRepository());

        mTextViewUnit.setText(mWeatherViewModel.getUnit().equals(METRIC_UNIT) ?
                Constants.CELSIUS : Constants.FAHRENHEIT);

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        populateUI(mWeatherViewModel.getLastWeather());
        requestLocationUpdate();

    }

    @Override
    public void onRefresh() {
        requestLocationUpdate();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fragment_weather_cardview_unit:
                toggleUnit();
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        getLatestWeather(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        requestLocationUpdate();
    }

    @Override
    public void onProviderDisabled(String provider) {
        showEnableLocationMessage();
        mSwipeLayout.setRefreshing(false);
    }

    private void getLatestWeather(Double lat, Double lon) {
        mWeatherViewModel.getWeather(String.valueOf(lat),
                String.valueOf(lon)).observe(this, result -> {
            mSwipeLayout.setRefreshing(true);
            switch(result.status) {

                case SUCCESS:
                    WeatherResponse weatherResponse = result.data;
                    populateUI(weatherResponse);

                    mWeatherViewModel.setLastWeather(weatherResponse);

                    break;
                case LOADING:
                    break;
                case CLIENT_ERROR:
                case SERVER_ERROR:
                    // Show error
                    populateUI(mWeatherViewModel.getLastWeather());
                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    break;
            }
            mSwipeLayout.setRefreshing(false);
        });
    }

    private void showEnableLocationMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.enable_location_message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void requestLocationUpdate() {
        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PERMISSION_GRANTED) {
            mSwipeLayout.setRefreshing(true);
            mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        }
    }

    private void populateUI(WeatherResponse weatherResponse) {
        Glide.with(getContext())
                .load(Constants.ICON_BASE_URL
                        + weatherResponse.getWeather().get(0).getIcon() + "@2x.png")
                .apply(new RequestOptions().centerCrop())
                .into(mImageViewWeather);

        mTextViewLocation.setText(weatherResponse.getName()
                + ", " + weatherResponse.getSys().getCountry());

        mTextViewDate.setText(weatherResponse.getDt() == 0 ? "" :
                new SimpleDateFormat(Constants.DATE_FORMAT)
                .format(new Date(weatherResponse.getDt() * 1000l)));

        mTextViewWeatherMain.setText(weatherResponse.getWeather().get(0).getMain());
        mTextViewTemp.setText(weatherResponse.getMain().getTemp().intValue() + unitTemp());

        mTextViewMaxTemp.setText(weatherResponse.getMain().getTempMax().intValue() + unitTemp());
        mTextViewMinTemp.setText(weatherResponse.getMain().getTempMin().intValue() + unitTemp());

        mTextViewClouds.setText(weatherResponse.getClouds().getAll() + "%");
        mTextViewWind.setText(weatherResponse.getWind().getSpeed().intValue() + unitSpeed());
        mTextViewHumidity.setText(weatherResponse.getMain().getHumidity() + "%");
    }

    private void toggleUnit() {
        if(mWeatherViewModel.getUnit().equals(METRIC_UNIT)) {
            mWeatherViewModel.setUnit(Constants.IMPERIAL_UNIT);
            mTextViewUnit.setText(Constants.FAHRENHEIT);
        } else {
            mWeatherViewModel.setUnit(METRIC_UNIT);
            mTextViewUnit.setText(Constants.CELSIUS);
        }
        requestLocationUpdate();

    }

    private String unitTemp() {
        return mWeatherViewModel.getUnit().equals(METRIC_UNIT) ? Constants.CELSIUS : FAHRENHEIT;
    }

    private String unitSpeed() {
        return mWeatherViewModel.getUnit().equals(METRIC_UNIT) ? Constants.METERS : Constants.MILES;
    }

}

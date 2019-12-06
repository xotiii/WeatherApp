package com.danchua.weatherapp.splash;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.danchua.weatherapp.R;

/**
 * Created by Dan Chua
 */
public class SplashFragment extends Fragment implements LocationListener {

    /**
     *  Initialize UI Components
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    99);
        } else {
            LocationManager locationManager = (LocationManager) getActivity()
                    .getSystemService(Context.LOCATION_SERVICE);

            if (locationManager != null) {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,
                        this, null);
                locationManager.removeUpdates(this);
            }
            Navigation.findNavController(getView())
                    .navigate(R.id.action_splashFragment_to_weatherFragment, null,
                            new NavOptions.Builder().setPopUpTo(R.id.splashFragment,
                                    true).build());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 99:
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Navigation.findNavController(this.getView())
                            .navigate(R.id.action_splashFragment_to_weatherFragment, null,
                                    new NavOptions.Builder().setPopUpTo(R.id.splashFragment,
                                            true).build());
                } else {
                    Log.d("Permission", "DENIED");
                }
                return;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("LAT", String.valueOf(location.getLatitude()));
        Log.d("LONG", String.valueOf(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

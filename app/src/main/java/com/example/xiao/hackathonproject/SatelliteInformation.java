package com.example.xiao.hackathonproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SatelliteInformation extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    private TextView mLatitude;
    private TextView mLongitude;
    private TextView mAccuracy;
    private TextView mProvider;
    private Switch switchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satellite_information);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the GPS location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                //Called when the provider status changes. This method is called when a provider is unable to fetch a location or if the provider has recently become available
                //after a period of unavailability
                providerStatusChanged(provider, status, extras);
            }

            public void onProviderEnabled(String provider) {
                //Called when the provider is enabled by the user.
                providerEnabled(provider);
            }

            public void onProviderDisabled(String provider) {
                //Called when the provider is disabled by the user. If requestLocationUpdates is called on already disabled provider, this method is called immediately.
                providerDisabled(provider);
            }
        };

        switchListener = (Switch) findViewById(R.id.listener_switch);
        switchListener.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // the toggle is enabled
                    startLocationListening();
                } else {
                    // the toggle is disabled
                    stopLocationListening();
                }
            }
        });

        mLatitude = (TextView) findViewById(R.id.latitude);
        mLongitude = (TextView) findViewById(R.id.longitude);
        mAccuracy = (TextView) findViewById(R.id.accuracy);
        mProvider = (TextView) findViewById(R.id.provider);
    }

    @Override
    protected void onStop() {
        super.onStop();
        switchListener.setChecked(false);
    }

    protected void startLocationListening() {
        // Register the listener with the Location Manager to receive location updates
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Permission is not granted.", Toast.LENGTH_LONG).show();
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    protected void stopLocationListening() {
        locationManager.removeUpdates(locationListener);
        mLatitude.setText(getString(R.string.latitude));
        mLongitude.setText(getString(R.string.longitude));
        mAccuracy.setText(getString(R.string.accuracy));
        mProvider.setText(getString(R.string.provider));
    }

    protected void makeUseOfNewLocation(Location location) {
        if (location != null) {
            mLatitude.setText(String.format("%s %f °", getString(R.string.latitude), location.getLatitude()));
            mLongitude.setText(String.format("%s %f °", getString(R.string.longitude), location.getLongitude()));
            mAccuracy.setText(String.format("%s %.1f m", getString(R.string.accuracy), location.getAccuracy()));
            mProvider.setText(String.format("%s %s", getString(R.string.provider), location.getProvider()));
        }
    }

    protected void providerEnabled(String provider) {
        Toast.makeText(this, provider+" has been enabled.", Toast.LENGTH_LONG).show();
    }

    protected void providerDisabled(String provider) {
        Toast.makeText(this, provider+" has been disabled.", Toast.LENGTH_LONG).show();
    }

    protected void providerStatusChanged(String provider, int status, Bundle extras) {
        String providerStatus = "Unknown";
        switch(status) {
            case LocationProvider.AVAILABLE: providerStatus = "Available"; break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE: providerStatus = "Temporarily Unavailable"; break;
            case LocationProvider.OUT_OF_SERVICE: providerStatus = "Out of Service"; break;
        }
        Toast.makeText(this, String.format("%s is %s.", provider, providerStatus), Toast.LENGTH_LONG).show();
    }
}

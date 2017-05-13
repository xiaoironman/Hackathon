package com.example.xiao.hackathonproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.location.OnNmeaMessageListener;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SatelliteInformation extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private GnssStatus.Callback gnssStatusListener;
    private GnssMeasurementsEvent.Callback gnssMeasurementsEventsListener;
    private GnssNavigationMessage.Callback gnssNavigationMessageListener;
    private OnNmeaMessageListener nmeaMessageListener;
    private int[] lastGnssStatus;

    private TextView mLatitude;
    private TextView mLongitude;
    private TextView mAccuracy;
    private TextView mProvider;
    private TextView mFirstFix;
    private TextView mSatCount;
    private TextView mGNSSMeasurementsStatus;
    private TextView mNavMsgStatus;
    private Switch switchListener;

    //Connection to the button(to satellite list view) is below:
    public void onClickToSatList(View v)
    {
        Intent myIntent = new Intent(this,SatelliteList.class);
        myIntent.putExtra("SatInfo",lastGnssStatus);
        startActivity(myIntent);
    }

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

        // define a GNSS raw measurements listener that contains raw and computed data identifying a satellite
        gnssMeasurementsEventsListener = new GnssMeasurementsEvent.Callback() {
            public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
                // Reports the latest collected GNSS measurements
                gnssMeasurementsReceived(eventArgs);
            }

            public void onStatusChanged(int status) {
                // Reports the latest status of the GNSS measurements sub-system
                gnssMeasurementsStatusChanged(status);
            }
        };

        // define a GNSS status listener that contains all information about tracked satellites
        gnssStatusListener = new GnssStatus.Callback() {
            public void onStarted() {
                super.onStarted();
            }

            public void onStopped() {
                super.onStopped();
            }

            public void onFirstFix(int ttffMillis) {
                // Called when the GNSS system has received its first fix since starting
                firstFixAcquired(ttffMillis);
            }

            public void onSatelliteStatusChanged(GnssStatus status) {
                // Called periodically to report GNSS satellite status
                onGnssStatusChanged(status);
            }
        };

        // define a GNSS Navigation message listener that contains all information about Navigation Messages
        gnssNavigationMessageListener = new GnssNavigationMessage.Callback() {
            public void onGnssNavigationMessageReceived(GnssNavigationMessage event) {
                // Returns the latest collected GNSS Navigation Message
            }

            public void onStatusChanged(int status) {
                // Returns the latest status of the GNSS Navigation Messages sub-system
                gnssNavigationMessageStatusChanged(status);
            }
        };

        // define a listener that receives NMEA sentences from the GNSS
        nmeaMessageListener = new OnNmeaMessageListener() {
            @Override
            public void onNmeaMessage(String message, long timestamp) {
                // Called when NMEA message is received
            }
        };

        // initialize switch's functionality
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

        // get references to TextViews in the layout
        mLatitude = (TextView) findViewById(R.id.latitude);
        mLongitude = (TextView) findViewById(R.id.longitude);
        mAccuracy = (TextView) findViewById(R.id.accuracy);
        mProvider = (TextView) findViewById(R.id.provider);
        mFirstFix = (TextView) findViewById(R.id.first_fix);
        mSatCount = (TextView) findViewById(R.id.sat_count);
        mNavMsgStatus = (TextView) findViewById(R.id.nav_msg_status);
        mGNSSMeasurementsStatus = (TextView) findViewById(R.id.gnss_measurement_status);
    }

    // when we leave the activity (back and home button)
    @Override
    protected void onStop() {
        super.onStop();
        switchListener.setChecked(false);
    }

    // request location updates for the listener if we have permission
    // 0, 0 means we want updates as often as possible
    // register also GNSS status callback to the manager = request GNSS status changes for the listener
    // also GNSS measurements callback is registered, listening to raw data
    protected void startLocationListening() {
        // Register the listener with the Location Manager to receive location updates
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Permission is not granted.", Toast.LENGTH_LONG).show();
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.registerGnssStatusCallback(gnssStatusListener);
            locationManager.registerGnssMeasurementsCallback(gnssMeasurementsEventsListener);
            locationManager.registerGnssNavigationMessageCallback(gnssNavigationMessageListener);
        }
    }

    // stop listening to location updates and clear TextViews
    // stop listening to GNSS status changes
    protected void stopLocationListening() {
        locationManager.removeUpdates(locationListener);
        locationManager.unregisterGnssStatusCallback(gnssStatusListener);
        mLatitude.setText(getString(R.string.latitude));
        mLongitude.setText(getString(R.string.longitude));
        mAccuracy.setText(getString(R.string.accuracy));
        mProvider.setText(getString(R.string.provider));
        mFirstFix.setText(getString(R.string.first_fix));
        mSatCount.setText(getString(R.string.sat_count));
        mGNSSMeasurementsStatus.setText(getString(R.string.gnss_measurement_status));
        mNavMsgStatus.setText(getString(R.string.nav_msg_status));
    }

    // after getting a new location
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

    protected void onGnssStatusChanged(GnssStatus status) {
        mSatCount.setText(String.format("%s %d", getString(R.string.sat_count), status.getSatelliteCount()));
        lastGnssStatus = new int[status.getSatelliteCount()];
        for (int i = 0; i < status.getSatelliteCount(); i++) {
            lastGnssStatus[i] = status.getSvid(i);
        }
    }

    protected void firstFixAcquired(int time) {
        //time is in ms
        mFirstFix.setText(String.format("%s %d ms", getString(R.string.first_fix), time));
    }

    protected void gnssMeasurementsReceived(GnssMeasurementsEvent event) {
        // Raw measurements are received!!!
    }

    protected void gnssMeasurementsStatusChanged(int status) {
        String gnssStatus = "Unknown";
        switch(status) {
            case GnssMeasurementsEvent.Callback.STATUS_NOT_SUPPORTED: gnssStatus = "The system does not support tracking of GNSS Measurements"; break;
            case GnssMeasurementsEvent.Callback.STATUS_READY: gnssStatus = "GNSS Measurements are successfully being tracked"; break;
            case GnssMeasurementsEvent.Callback.STATUS_LOCATION_DISABLED: gnssStatus = "GNSS provider of Location is disabled"; break;
        }
        mGNSSMeasurementsStatus.setText(String.format("%s %s", getString(R.string.gnss_measurement_status), gnssStatus));
    }

    protected void gnssNavigationMessageStatusChanged(int status) {
        String gnssNavMsgStatus = "Unknown";
        switch(status) {
            case GnssNavigationMessage.Callback.STATUS_NOT_SUPPORTED: gnssNavMsgStatus = "The system does not support tracking of GNSS Navigation Messages"; break;
            case GnssNavigationMessage.Callback.STATUS_READY: gnssNavMsgStatus = "GNSS Navigation Messages are successfully being tracked"; break;
            case GnssNavigationMessage.Callback.STATUS_LOCATION_DISABLED: gnssNavMsgStatus = "GNSS provider of Location is disabled"; break;
        }
        mNavMsgStatus.setText(String.format("%s %s", getString(R.string.nav_msg_status), gnssNavMsgStatus));
    }
}

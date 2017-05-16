package com.example.xiao.hackathonproject;

import android.content.Intent;
import android.location.GnssStatus;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SatelliteList extends AppCompatActivity {

    private int NumberOfSatellites = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satellite_list);

        Intent intent = getIntent();
        int[] SvidArray = intent.getIntArrayExtra("Svid");
        int[] ConstellationArray = intent.getIntArrayExtra("Constellation");
        float[] Elevation = intent.getFloatArrayExtra("Elevation Angle");
        float[] Azimuth = intent.getFloatArrayExtra("Azimuth Angle");
        String[] SatInfo = new String[SvidArray.length];


        for (int i = 0; i < SatInfo.length; i++) {
            String constel = "";
            switch(ConstellationArray[i]) {
                case GnssStatus.CONSTELLATION_BEIDOU: constel = "BEIDOU"; break;
                case GnssStatus.CONSTELLATION_GLONASS: constel = "GLONASS"; break;
                case GnssStatus.CONSTELLATION_GPS: constel = "GPS"; break;
                case GnssStatus.CONSTELLATION_GALILEO: constel= "GALILEO"; break;
                default: constel = "UNKNOWN"; break;
            }
            SatInfo[i] = String.format("SV ID: %d CONSTELLATION: %s \nThe elevation angle is: %f \nThe Azimuth Angle is: %f ",SvidArray[i],constel, Elevation[i],Azimuth[i]);
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        final TextView textViewOfSatInfo = (TextView) findViewById(R.id.textViewInSatList);

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SatInfo);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String clickedSatelliteName=String.valueOf(parent.getItemAtPosition(position));
                        textViewOfSatInfo.setText(clickedSatelliteName);
                    }
                }
        );
    }
}

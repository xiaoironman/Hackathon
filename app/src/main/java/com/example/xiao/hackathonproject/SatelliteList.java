package com.example.xiao.hackathonproject;

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

        ListView listView = (ListView) findViewById(R.id.listView);
        final TextView textViewOfSatInfo = (TextView) findViewById(R.id.textViewInSatList);

        //TODO: enter the string as the list of the name of the satellites
        String[] SatelliteNames = new String[NumberOfSatellites];

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SatelliteNames);
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

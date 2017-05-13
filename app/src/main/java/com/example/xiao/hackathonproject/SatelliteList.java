package com.example.xiao.hackathonproject;

import android.content.Intent;
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
        int[] SvidArray = intent.getIntArrayExtra("SatInfo");
        String[] SvidStrings = new String[SvidArray.length];
        for (int i = 0; i < SvidArray.length; i++) {
            SvidStrings[i] = String.valueOf(SvidArray[i]);
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        final TextView textViewOfSatInfo = (TextView) findViewById(R.id.textViewInSatList);

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SvidStrings);
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

package com.example.xiao.hackathonproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ReferenceList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_list);
    }

    public void onClickToMap(View v)
    {
        Intent myIntent = new Intent(this,RoughLocation.class);
        startActivity(myIntent);
    }
}

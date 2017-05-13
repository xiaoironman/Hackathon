package com.example.xiao.hackathonproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //The code for customized toolbar;
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.CogeoToolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("COGEO"); //Change the Toolbar title here!!!

        // check if the permission is granted for getting a fine location; if not, ask for one
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }

    }

    //Click the first button(oclick set to onClick1) to go to the rough location page;
    public void onClick1(View v)
    {
        Intent myIntent = new Intent(this,RoughLocation.class);
        startActivity(myIntent);
    }
    public void onClick2(View v)
    {
        Intent myIntent = new Intent(this,SatelliteInformation.class);
        startActivity(myIntent);
    }
    public void onClick3(View v)
    {
        Intent myIntent = new Intent(this,ReferenceList.class);
        startActivity(myIntent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_LONG).show();
                } else {
                    // permission denied
                    Toast.makeText(this, "Permission not granted.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}

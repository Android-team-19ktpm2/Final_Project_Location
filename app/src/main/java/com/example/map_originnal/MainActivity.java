package com.example.map_originnal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    Button btnCheck ;
    User user_activity = new User();
    Map map_activity = new Map();
    Home home_activity = new Home();
    Family family_activity = new Family();
  //  Fragment mMapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCheck = findViewById(R.id.btnShowHide);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.map);
       // mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.person:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, user_activity).commit();
                return true;

            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, home_activity).commit();
                return true;

            case R.id.family:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, family_activity).commit();
                return true;

            case R.id.map:

                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, map_activity).commit();


                return true;
        }
        return false;
    }


}
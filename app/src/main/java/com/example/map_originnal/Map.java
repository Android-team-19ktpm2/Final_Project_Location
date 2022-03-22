package com.example.map_originnal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class Map extends Fragment  {
    Button btnCheck;

    static  boolean status = true;
    Spinner spinner;
    ImageButton btnCurrentLocation;
    FusedLocationProviderClient client ;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            ArrayList<String> ds_StyleMap = new  ArrayList<>();
            ds_StyleMap.add("Style 1");
            ds_StyleMap.add("Style 2");
            ds_StyleMap.add("Style 3");
            ds_StyleMap.add("Style 4");
            ArrayAdapter arrayAdapter   = new ArrayAdapter(getContext() , android.R.layout.simple_spinner_item,ds_StyleMap);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;

                        case 1:
                            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            break;

                        case 2:
                            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            break;

                        case 3:
                            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            break;

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    };






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        btnCurrentLocation = v.findViewById(R.id.imageButton);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
                        &&ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        getCurrentLocation();
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},100);

                }
            }


        });
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100&&(grantResults.length>0)
                &&(grantResults[0]+grantResults[1]==PackageManager.PERMISSION_GRANTED)){
            getCurrentLocation();
        }else{
            Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager)getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                   if(location!=null){
                      // Log.e("location",)
                       Log.e("hello",String.valueOf(location.getLatitude()));
                       Log.e("hello",String.valueOf(location.getLongitude()));
                    }
                   else{
                       com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest()
                               .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
                               .setInterval(10000)
                               .setFastestInterval(1000)
                               .setNumUpdates(1);
                       LocationCallback locationCallback = new LocationCallback() {
                           @Override
                           public void onLocationResult(@NonNull LocationResult locationResult) {
                               super.onLocationResult(locationResult);
                               Location location1 = locationResult.getLastLocation();
                               Log.e("hello",String.valueOf(location1.getLatitude()));
                               Log.e("hello",String.valueOf(location1.getLongitude()));
                           }
                       };
                        client.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                   }
                }
            });
        }
        else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        btnCheck = view.findViewById(R.id.btnShowHide);
        spinner = view.findViewById(R.id.spinner);


        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status){
                    mapFragment.getView().setVisibility(View.INVISIBLE);
                    btnCheck.setText("Hide map");
                    status= false;
                }else {
                    mapFragment.getView().setVisibility(View.VISIBLE);
                    btnCheck.setText("Show map");
                    status = true;
                }
            }
        });
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);

        }
    }


}
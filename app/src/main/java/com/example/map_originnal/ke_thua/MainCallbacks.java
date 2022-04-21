package com.example.map_originnal.ke_thua;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public interface MainCallbacks {

    public void onMsgFromFragToMain (String sender, String strValue);
    public void onLocationFromFragToMain (String sender, LatLng Value);
    public void onLocationFromAdapToMain (String sender, LatLng Value);

}

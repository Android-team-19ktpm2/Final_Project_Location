package com.example.map_originnal.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.map_originnal.ke_thua.FrangmentCallbacks;
import com.example.map_originnal.R;
import com.example.map_originnal.activity.MainActivity;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class HideMapFragment extends Fragment implements FrangmentCallbacks {


    MainActivity main;
    LatLng locationCurrent;
    TextView txtToaDo,txtDiaChi,txtAccuracy,txtSpeed,txtAttitude;


    public HideMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout layout_hide_map = (RelativeLayout) inflater.inflate(R.layout.fragment_hide_map, container, false);
        main = (MainActivity) getActivity();

        txtToaDo = layout_hide_map.findViewById(R.id.txtToaDo);
        txtDiaChi = layout_hide_map.findViewById(R.id.txtDiaChi);
        txtAccuracy = layout_hide_map.findViewById(R.id.txtAccuracy);
        txtSpeed = layout_hide_map.findViewById(R.id.txtSpeed);
        txtAttitude = layout_hide_map.findViewById(R.id.txtAttitude);

        if (locationCurrent !=null){
            try {
                Geocoder geocoder = new Geocoder(main);
                List<Address> a = geocoder.getFromLocation(locationCurrent.latitude,locationCurrent.longitude,1);
                Location temp = new Location("Test");
                temp.setLatitude(locationCurrent.latitude);
                temp.setLongitude(locationCurrent.longitude);

                txtToaDo.setText(locationCurrent.latitude + ", "+locationCurrent.longitude);
                if (!a.isEmpty()){
                    txtDiaChi.setText(a.get(0).getAddressLine(0));
                    txtAccuracy.setText(String.valueOf((int) temp.getAccuracy()));
                    txtAttitude.setText(String.valueOf((int) temp.getAltitude()));
                    txtSpeed.setText((String.valueOf((int) temp.getSpeed())));
                }else {
                    txtDiaChi.setText("Biển");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        layout_hide_map.findViewById(R.id.btnShowMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("Hide-MapFragment", "ShowMap");
            }
        });


        return layout_hide_map;
    }

    @Override
    public void onLocationFromMainToFrag(String sender, LatLng Value) {
        if (sender.equals("main")){
            locationCurrent = Value;
        }
    }
}
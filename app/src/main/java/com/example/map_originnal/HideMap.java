package com.example.map_originnal;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HideMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HideMap extends Fragment implements FrangmentCallbacks {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MainActivity main;
    LatLng locationCurrent;
    TextView txtToaDo,txtDiaChi,txtAccuracy,txtSpeed,txtAttitude;


    public HideMap() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HideMap.
     */
    // TODO: Rename and change types and number of parameters
    public static HideMap newInstance(String param1, String param2) {
        HideMap fragment = new HideMap();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                main.onMsgFromFragToMain("Hide-Map", "ShowMap");
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
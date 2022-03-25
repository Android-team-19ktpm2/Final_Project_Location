package com.example.map_originnal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends Fragment {

    Button btnHideMap;
    ImageButton btnSetting,btnSatellite;
    MainActivity main;
    View main_menu,view_mode;
    Boolean show_view_mode=false;
    Boolean show_main_menu=false;

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
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        RelativeLayout linearLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_map, container, false);
        main = (MainActivity)getActivity();


        show_main_menu=false;

        btnHideMap = linearLayout.findViewById(R.id.btnHideMap);
        btnSetting = linearLayout.findViewById(R.id.btnSetting);
        btnSatellite=linearLayout.findViewById(R.id.btnSatellite);
        main_menu=linearLayout.findViewById(R.id.main_menu);
        view_mode=linearLayout.findViewById(R.id.view_mode);

        main_menu.animate().alpha(0.0f);
        /*main_menu.animate().alpha(0.0f);*/
/*        view_mode.animate().alpha(0.0f);*/

        btnHideMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("Map-Frag","ShowMap");
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show_main_menu) {
                    btnSetting.animate().alpha(1f).setDuration(400);
                    main_menu.animate().alpha(0.0f).setDuration(400);
                    show_main_menu=false;
                }

                else {
                    main_menu.animate().alpha(0.0f);
                    main_menu.setVisibility(View.VISIBLE);
                    btnSetting.animate().alpha(0.3f).setDuration(400);
                    main_menu.animate().alpha(1f).setDuration(400);
                    show_main_menu=true;
                }

            }
        });

        btnSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //View mode here
            }
        });




        return linearLayout; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
package com.example.map_originnal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.security.Permission;
import java.util.List;

public class Map extends Fragment implements GoogleMap.OnMarkerClickListener, FrangmentCallbacks {

    private static final int PERMISSION_FINE_LOCATION = 99;


    Button btnHideMap;
    RadioButton rd;
    ImageButton btnSetting, btnSearch, btnListFamily, btnSatellite;
    MainActivity main;
    View main_menu, view_mode;
    ProgressDialog progressDialog;
    SearchView searchView;
    SupportMapFragment mapFragment;
    Switch sw_gps;

    Boolean show_main_menu = false;
    Boolean show_view_mode = false;
    GoogleMap mMap;
    LatLng loction_focus = null;

    GoogleMap.OnMyLocationChangeListener locationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(@NonNull Location location) {

            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if (mMap != null) {
                loction_focus = loc;
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(loc);
                mMap.clear();
                mMap.addMarker(markerOptions);
            }
        }
    };

    FusedLocationProviderClient client;


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
            mMap = googleMap;
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                    mMap.addMarker(markerOptions);

                    if (rd.isChecked()) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    } else {
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    }
                }
            });

            mMap.setOnMarkerClickListener(Map.this);
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    progressDialog.dismiss();


                    sw_gps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sw_gps.isChecked()) {
                                if ( ActivityCompat.checkSelfPermission(main, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                }
                                mMap.setMyLocationEnabled(true);
                                mMap.setOnMyLocationChangeListener(locationChangeListener);
                                Toast.makeText(main,"Using GPS sensors",Toast.LENGTH_SHORT).show();
                            } else {
                                mMap.setMyLocationEnabled(false);
                                Toast.makeText(main,"Using Towner + Wifi",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });



        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        RelativeLayout linearLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_map, container, false);
        main = (MainActivity) getActivity();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Thông báo");
        progressDialog.setMessage("Đang tải Map, Vui lòng chờ......");
        progressDialog.show();

        searchView = linearLayout.findViewById(R.id.searchView);
        btnHideMap = linearLayout.findViewById(R.id.btnHideMap);
        btnSetting = linearLayout.findViewById(R.id.btnSetting);
        btnSearch = linearLayout.findViewById(R.id.btnSearch);
        btnSatellite = linearLayout.findViewById(R.id.btnSatellite);
        btnListFamily = linearLayout.findViewById(R.id.btnListFamily);

        main_menu = linearLayout.findViewById(R.id.main_menu);
        view_mode = linearLayout.findViewById(R.id.view_mode);
        sw_gps = main_menu.findViewById(R.id.gps_mode);

        rd = view_mode.findViewById(R.id.satellite_mode);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        show_main_menu = false;
        show_view_mode = false;




        btnHideMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main.onMsgFromFragToMain("Map-Frag", "ShowMap");
                main.onLocationFromFragToMain("Map-Frag", loction_focus);

            }
        });


        btnListFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("Map-Frag", "ShowList");
            }
        });


        main_menu.animate().alpha(0.0f);
        view_mode.animate().alpha(0.0f);


        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show_main_menu) {
                    btnSetting.animate().alpha(1f).setDuration(400);
                    main_menu.animate().alpha(0.0f).setDuration(400);
                    show_main_menu=false;
                }

                else {
                    view_mode.animate().alpha(0.0f);
                    view_mode.setVisibility(View.INVISIBLE);
                    btnSatellite.animate().alpha(1f).setDuration(400);

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
                    if(show_view_mode) {
                        btnSatellite.animate().alpha(1f).setDuration(400);
                        view_mode.animate().alpha(0.0f).setDuration(400);
                        show_view_mode=false;
                    }
                    else {
                        main_menu.animate().alpha(0.0f);
                        main_menu.setVisibility(View.INVISIBLE);
                        btnSetting.animate().alpha(1f).setDuration(400);

                        view_mode.animate().alpha(0.0f);
                        view_mode.setVisibility(View.VISIBLE);
                        btnSatellite.animate().alpha(0.3f).setDuration(400);
                        view_mode.animate().alpha(1f).setDuration(400);
                        show_view_mode=true;
                    }
                }
            });




            main_menu.findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(main);
                    builderSingle.setIcon(R.drawable.breathtaking);
                    builderSingle.setTitle("Lịch Sử");
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(main, android.R.layout.select_dialog_item);
                    arrayAdapter.add("Đường chưa đặt tên, Xuân Thạnh, Thống Nhất, Đồng Nai, Việt Nam");
                    arrayAdapter.add("Đường chùa Tịnh Quang Tổ 10, ấp Ngô Quyền, Thị trấn Bầu Hàm 2, Thống Nhất, Đồng Nai, Việt Nam");
                    arrayAdapter.add("Quốc lộ 1A, xã Bàu Hàm 2, huyện Thống Nhất, Đồng Nai, Việt Nam");
                    arrayAdapter.add("Đường Lê Lợi, phường Bến Thành, quận 1, TP HCM");
                    arrayAdapter.add("135 Nam Kỳ Khởi Nghĩa, Bến Nghé, Quận 1, TP HCM");
                    arrayAdapter.add("01 Công Xã Paris, Bến Nghé, Quận 1, TP HCM");
                    arrayAdapter.add("2 Nguyễn Bỉnh Khiêm, Quận 1, TP HCM");
                    arrayAdapter.add("TL15, Phú Hiệp, Củ Chi, TP HCM");
                    arrayAdapter.add("Đường Nguyễn Huệ, quận 1, TP HCM");


                    builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(main);
                            builderInner.setMessage(strName);
                            builderInner.setTitle("Your Selected Item is");

                            builderInner.show();
                        }
                    });
                    builderSingle.show();


                }
            });

            main_menu.findViewById(R.id.favorite).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(main);
                    builderSingle.setIcon(R.drawable.breathtaking);
                    builderSingle.setTitle("Địa điểm yêu thích");
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(main, android.R.layout.select_dialog_item);
                    arrayAdapter.add("Đường chưa đặt tên, Xuân Thạnh, Thống Nhất, Đồng Nai, Việt Nam");
                    arrayAdapter.add("Đường chùa Tịnh Quang Tổ 10, ấp Ngô Quyền, Thị trấn Bầu Hàm 2, Thống Nhất, Đồng Nai, Việt Nam");
                    arrayAdapter.add("Quốc lộ 1A, xã Bàu Hàm 2, huyện Thống Nhất, Đồng Nai, Việt Nam");
                    arrayAdapter.add("Đường Lê Lợi, phường Bến Thành, quận 1, TP HCM");
                    arrayAdapter.add("135 Nam Kỳ Khởi Nghĩa, Bến Nghé, Quận 1, TP HCM");
                    arrayAdapter.add("01 Công Xã Paris, Bến Nghé, Quận 1, TP HCM");
                    arrayAdapter.add("2 Nguyễn Bỉnh Khiêm, Quận 1, TP HCM");


                    builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(main);
                            builderInner.setMessage(strName);
                            builderInner.setTitle("Your Selected Item is");
                            builderInner.show();
                        }
                    });
                    builderSingle.show();


                }
            });


            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (searchView.getVisibility() == View.VISIBLE) {
                        searchView.setVisibility(View.INVISIBLE);
                    } else {
                        searchView.setVisibility(View.VISIBLE);
                    }
                }
            });


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    String location = searchView.getQuery().toString();
                    List<Address> addressList = null;
                    if (location != null || !location.equals("")) {
                        Geocoder geocoder = new Geocoder(main);
                        try {
                            addressList = geocoder.getFromLocationName(location, 1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }

            });

        return linearLayout;

    }


        @Override
        public boolean onMarkerClick(@NonNull Marker marker) {
            loction_focus = marker.getPosition();
            if(loction_focus != null){
                try {
                    displayLocation(loction_focus);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return false;
        }

        private void displayLocation(LatLng loction_focus) throws IOException {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.layout_current_location,(LinearLayout) getActivity().findViewById(R.id.current_container));
            bottomSheetView.findViewById(R.id.btn_favourite).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Đã thêm vào Favourite", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                }
            });

            Geocoder geocoder = new Geocoder(main);
            List<Address> a = geocoder.getFromLocation(loction_focus.latitude,loction_focus.longitude,1);

            TextView txtDiaChi = bottomSheetView.findViewById(R.id.txtDiaChi);
            TextView txtArea = bottomSheetView.findViewById(R.id.txtArea);


            if (a.isEmpty()){

            }
            else{
                txtArea.setText(a.get(0).getAdminArea());
                txtDiaChi.setText(a.get(0).getAddressLine(0));
            }
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        }

        @Override
        public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);
            mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
        }

    @Override
    public void onLocationFromMainToFrag(String sender, LatLng Value) {
    }
}

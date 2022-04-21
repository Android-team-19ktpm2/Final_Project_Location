package com.example.map_originnal.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.map_originnal.ke_thua.FrangmentCallbacks;
import com.example.map_originnal.R;
import com.example.map_originnal.activity.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, FrangmentCallbacks {

    private static final int PERMISSION_FINE_LOCATION = 99;


    Button btnHideMap;
    RadioButton rdDefualt,rdHybrid;
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
    LatLng locationFriend;


    int count_location_favorite;


    DatabaseReference FavoriteRef;
    DatabaseReference UserRef;
    FirebaseUser firebaseUser;

    GoogleMap.OnMyLocationChangeListener locationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(@NonNull Location location) {
            HashMap hashMap = new HashMap();
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if (mMap != null) {
                loction_focus = loc;
                hashMap.put("lat_X", String.valueOf(loc.latitude));
                hashMap.put("long_Y", String.valueOf(loc.longitude));
                UserRef.updateChildren(hashMap);
            }
        }
    };

    FusedLocationProviderClient client;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

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

                }
            });



            mMap.setOnMarkerClickListener(MapFragment.this);
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    progressDialog.dismiss();

                    sw_gps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sw_gps.isChecked()) {
                                if ( ActivityCompat.checkSelfPermission(main, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }else{
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);

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


            rdDefualt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rdDefualt.isChecked()){
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    }
                }
            });

            rdHybrid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rdHybrid.isChecked()){
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    }
                }
            });




        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        FavoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");





        main = (MainActivity) getActivity();

        RelativeLayout linearLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_map, container, false);

        progressDialog = new ProgressDialog(main);
        progressDialog.setTitle("Thông báo");
        progressDialog.setMessage("Đang tải MapFragment, Vui lòng chờ......");
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

        rdHybrid = view_mode.findViewById(R.id.hybrid_mode);
        rdDefualt = view_mode.findViewById(R.id.default_mode);




        client = LocationServices.getFusedLocationProviderClient(main);

        show_main_menu = false;
        show_view_mode = false;




        btnHideMap.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                main.onMsgFromFragToMain("MapFragment-Frag", "ShowMap");
                main.onLocationFromFragToMain("MapFragment-Frag", loction_focus);

            }
        });


        btnListFamily.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("MapFragment-Frag", "ShowList");
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
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(main,R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(main.getApplicationContext()).inflate(R.layout.dialog_current_location,(LinearLayout) main.findViewById(R.id.current_container));
            bottomSheetView.findViewById(R.id.btn_favourite).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    HashMap hashMap = new HashMap();
                    hashMap.put("latitude",String.valueOf(loction_focus.latitude));
                    hashMap.put("longitude",String.valueOf(loction_focus.longitude));

                    FavoriteRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            count_location_favorite = (int) snapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    FavoriteRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(count_location_favorite)).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Toast.makeText(main, "Đã thêm vào Favourite", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            }
                        }
                    });


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
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapgg);
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
        }

    @Override
    public void onLocationFromMainToFrag(String sender, LatLng Value) {
        locationFriend = Value;
        MarkerVitriCuaFriend();
    }

    private void MarkerVitriCuaFriend() {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(locationFriend).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_friend));
        mMap.clear();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationFriend, 16.0f));
        mMap.addMarker(markerOptions);

    }
}

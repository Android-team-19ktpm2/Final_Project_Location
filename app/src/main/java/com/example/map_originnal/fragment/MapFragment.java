package com.example.map_originnal.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    MainActivity main;

    //---------------Floating Menu---------------//
    //Floating menu widgets
    private FloatingActionButton btn_extend, btn_hide_map, btn_search, btn_family;
    private FloatingActionButton btn_view_mode, btn_gps;
    private TextView tv_search, tv_family, tv_view_mode;

    //Floating menu animation
    private Animation fl_menu_extend, fl_menu_close;

    //Floating menu flag
    private boolean isOpen;
    private boolean isSatellite;
    private boolean isGps;


    //---------------Map Fragment---------------//
    SearchView searchView;
    ProgressDialog progressDialog;
    SupportMapFragment mapFragment;



    //---------------APIs---------------//
    private static GoogleMap mMap;
    LatLng loction_focus = null;
    public static LatLng locationFriend;
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
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    btn_gps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isGps == false) {
                                isGps = true;
                                btn_gps.setImageResource(R.drawable.icon_gps_on);
                                if ( ActivityCompat.checkSelfPermission(main, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }else{
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);
                                }
                                mMap.setMyLocationEnabled(true);
                                mMap.setOnMyLocationChangeListener(locationChangeListener);
                                Toast.makeText(main,"Using GPS sensors",Toast.LENGTH_SHORT).show();
                            } else {
                                isGps = false;
                                btn_gps.setImageResource(R.drawable.icon_gps_off);
                                mMap.setMyLocationEnabled(false);
                                Toast.makeText(main,"Turn off GPS",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });



        }
    };



    //---------------Firebase---------------//
    DatabaseReference FavoriteRef;
    DatabaseReference UserRef;
    FirebaseUser firebaseUser;


    //---------------Others---------------//
    FusedLocationProviderClient client;
    int count_location_favorite;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        FavoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");


        main = (MainActivity) getActivity();

        ConstraintLayout linearLayout = (ConstraintLayout) inflater.inflate(R.layout.fragment_map, container, false);

        searchView = linearLayout.findViewById(R.id.searchView);

        fabInit(linearLayout);

        progressDialog = new ProgressDialog(main);
        progressDialog.setTitle("Thông báo");
        progressDialog.setMessage("Đang lấy thông tin bản đồ......");
        progressDialog.show();


        client = LocationServices.getFusedLocationProviderClient(main);


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


    public void fabInit(ConstraintLayout layout)
    {
        //Basic Button
        btn_extend = layout.findViewById(R.id.fl_menu_btn_extend);
        btn_search = layout.findViewById(R.id.fl_menu_btn_search);
        btn_family = layout.findViewById(R.id.fl_menu_btn_family);
        btn_hide_map = layout.findViewById(R.id.fl_menu_btn_hide_map);

        //Mode Button
        btn_view_mode = layout.findViewById(R.id.fl_menu_btn_view_mode);
        btn_gps = layout.findViewById(R.id.fl_menu_btn_gps);

        //Text
        tv_search = layout.findViewById(R.id.fl_menu_textview_search);
        tv_family = layout.findViewById(R.id.fl_menu_textview_family);
        tv_view_mode = layout.findViewById(R.id.fl_menu_textview_view_mode);


        //Animations
        fabAnimationInit();


        //Floating menu
        fabBtnInit();

        isOpen = false;
        isSatellite = false;
        isGps = false;

    }

    void fabAnimationInit()
    {
        fl_menu_extend = AnimationUtils.loadAnimation(main, R.anim.fl_menu_extend);
        fl_menu_close = AnimationUtils.loadAnimation(main, R.anim.fl_menu_close);
    }

    void fabBtnInit()
    {
        btn_extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isOpen)
                {
                    btn_search.setAnimation(fl_menu_close);
                    btn_family.setAnimation(fl_menu_close);
                    btn_view_mode.setAnimation(fl_menu_close);

                    tv_search.setVisibility(View.INVISIBLE);
                    tv_family.setVisibility(View.INVISIBLE);
                    tv_view_mode.setVisibility(View.INVISIBLE);

                    isOpen = false;
                }
                else
                {
                    btn_search.setAnimation(fl_menu_extend);
                    btn_family.setAnimation(fl_menu_extend);
                    btn_view_mode.setAnimation(fl_menu_extend);

                    tv_search.setVisibility(View.VISIBLE);
                    tv_family.setVisibility(View.VISIBLE);
                    tv_view_mode.setVisibility(View.VISIBLE);

                    isOpen = true;
                }

                //Restart animation
                btn_family.getAnimation().start();
                btn_search.getAnimation().start();

            }
        });


        btn_hide_map.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                main.onMsgFromFragToMain("MapFragment-Frag", "ShowMap");
                main.onLocationFromFragToMain("MapFragment-Frag", loction_focus);

            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getVisibility() == View.VISIBLE) {
                    searchView.setVisibility(View.INVISIBLE);
                } else {
                    searchView.setVisibility(View.VISIBLE);
                }
            }
        });


        btn_family.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("MapFragment-Frag", "ShowList");
            }
        });


        btn_view_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSatellite == true)
                {
                    btn_view_mode.setImageResource(R.drawable.icon_map_view);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    isSatellite = false;
                }

                else
                {
                    btn_view_mode.setImageResource(R.drawable.icon_satellite_view);
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    isSatellite = true;
                }
            }
        });


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
    public  void onLocationFromMainToFrag(String sender, LatLng Value) {
        locationFriend = Value;
        MarkerVitriCuaFriend();
    }

    private  void MarkerVitriCuaFriend() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(locationFriend).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_friend));

        mMap.clear();
        System.out.println("BUG CHO NAY");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationFriend, 16.0f));
        mMap.addMarker(markerOptions);

    }

    public static void MarkerVitriHienTai(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_friend));

        mMap.clear();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
        mMap.addMarker(markerOptions);

    }

}

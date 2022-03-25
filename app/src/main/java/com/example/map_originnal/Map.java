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
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
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

import java.io.IOException;
import java.util.List;

public class Map extends Fragment {

    Button btnHideMap;
//<<<<<<< HEAD
    ImageButton btnSetting,btnSearch , btnListFamily,btnSatellite;
    MainActivity main;
    View main_menu, view_mode;
    Boolean show_main_menu = false;
    Boolean show_view_mode = false;
    GoogleMap mMap;
    ProgressDialog progressDialog;
    SupportMapFragment mapFragment;
    SearchView searchView;
    GoogleMap.OnMyLocationChangeListener locationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(@NonNull Location location) {

            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if (mMap != null) {
                mMap.clear();
                Marker marker = mMap.addMarker(new MarkerOptions().position(loc));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };

    FusedLocationProviderClient client;
//=======
//    ImageButton btnSetting,btnSatellite;
//    MainActivity main;
//    View main_menu,view_mode;
///*    Boolean show_view_mode=false;*/
//    Boolean show_main_menu=false;
//>>>>>>> c5b93d92a3c620c7fe6da80bdff5e380e0d77d37

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

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    progressDialog.dismiss();


                    if (ActivityCompat.checkSelfPermission(main, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(main, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    mMap.setOnMyLocationChangeListener(locationChangeListener);
//                    LatLng sydney = new LatLng(-34, 151);
//                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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

//<<<<<<< HEAD
        searchView = linearLayout.findViewById(R.id.searchView);
        btnHideMap = linearLayout.findViewById(R.id.btnHideMap);
        btnSetting = linearLayout.findViewById(R.id.btnSetting);
        btnSearch = linearLayout.findViewById(R.id.btnSearch);
        btnSatellite = linearLayout.findViewById(R.id.btnSatellite);

        btnListFamily = linearLayout.findViewById(R.id.btnListFamily);

        main_menu = linearLayout.findViewById(R.id.main_menu);
        view_mode = linearLayout.findViewById(R.id.view_mode);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        show_main_menu = false;

        btnHideMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("Map-Frag", "ShowMap");
            }
        });


        btnListFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("Map-Frag", "ShowList");
            }
        });


        main_menu.animate().alpha(0.0f);


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
                    arrayAdapter.add("03 Nguyễn Bỉnh Khiêm, Bến Nghé, Quận 1, TP HCM");
                    arrayAdapter.add("02 Khu Him Lam, quận 7, TP HCM");
                    arrayAdapter.add("19-25 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP HCM");
                    arrayAdapter.add("3 Hòa Bình, phường 3, quận 11, TP HCM");
                    arrayAdapter.add("Số 125 Công xã Paris, Bến Nghé, Quận 1, TPHCM");
                    arrayAdapter.add("720A Điện Biên Phủ, Quận Bình Thạnh, TP HCM");
                    arrayAdapter.add("Số 7 đường Công Trường Lam Sơn, phường Bến Nghé, Quận 1, TP HCM");
                    arrayAdapter.add("Số 202 đường Võ Thị Sáu, phường 7, Quận 3, thành phố Hồ Chí Minh");
                    arrayAdapter.add("Số 1 Nguyễn Tất Thành, Phường 12, Quận 4, Thành phố Hồ Chí Minh");
                    arrayAdapter.add("120 Xa lộ Hà Nội, phường Tân Phú, Quận 9, thành phố Hồ Chí Minh");
                    arrayAdapter.add("Số 02 – 04 đường số 9, KDC Him Lam, phường Tân Hưng, Quận 7, thành phố Hồ Chí Minh");
                    arrayAdapter.add("1147 Bình Quới, phường 28, Quận Bình Thạnh, thành phố Hồ Chí Minh");
                    arrayAdapter.add("Khu du lịch 30/4, đường Thạnh Thới, Long Hà, Cần Giờ, thành phố Hồ Chí Minh");
                    arrayAdapter.add("81 Nguyễn Xiển, Long Bình, Quận 9, thành phố Hồ Chí Minh");
                    arrayAdapter.add("Bùi Viện – Phạm Ngũ Lão – Đề Thám, Quận 1, thành phố Hồ chí Minh");


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
                    arrayAdapter.add("TL15, Phú Hiệp, Củ Chi, TP HCM");
                    arrayAdapter.add("Đường Nguyễn Huệ, quận 1, TP HCM");
                    arrayAdapter.add("03 Nguyễn Bỉnh Khiêm, Bến Nghé, Quận 1, TP HCM");
                    arrayAdapter.add("02 Khu Him Lam, quận 7, TP HCM");
                    arrayAdapter.add("19-25 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP HCM");
                    arrayAdapter.add("3 Hòa Bình, phường 3, quận 11, TP HCM");
                    arrayAdapter.add("Số 125 Công xã Paris, Bến Nghé, Quận 1, TPHCM");
                    arrayAdapter.add("720A Điện Biên Phủ, Quận Bình Thạnh, TP HCM");
                    arrayAdapter.add("Số 7 đường Công Trường Lam Sơn, phường Bến Nghé, Quận 1, TP HCM");
                    arrayAdapter.add("Số 202 đường Võ Thị Sáu, phường 7, Quận 3, thành phố Hồ Chí Minh");
                    arrayAdapter.add("Số 1 Nguyễn Tất Thành, Phường 12, Quận 4, Thành phố Hồ Chí Minh");
                    arrayAdapter.add("120 Xa lộ Hà Nội, phường Tân Phú, Quận 9, thành phố Hồ Chí Minh");
                    arrayAdapter.add("Số 02 – 04 đường số 9, KDC Him Lam, phường Tân Hưng, Quận 7, thành phố Hồ Chí Minh");
                    arrayAdapter.add("1147 Bình Quới, phường 28, Quận Bình Thạnh, thành phố Hồ Chí Minh");
                    arrayAdapter.add("Khu du lịch 30/4, đường Thạnh Thới, Long Hà, Cần Giờ, thành phố Hồ Chí Minh");
                    arrayAdapter.add("81 Nguyễn Xiển, Long Bình, Quận 9, thành phố Hồ Chí Minh");
                    arrayAdapter.add("Bùi Viện – Phạm Ngũ Lão – Đề Thám, Quận 1, thành phố Hồ chí Minh");


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
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
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
        public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);
            mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);

            }
        }
 }

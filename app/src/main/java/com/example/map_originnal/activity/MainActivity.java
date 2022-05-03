package com.example.map_originnal.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.map_originnal.Adapter.AdapterListFamily;
import com.example.map_originnal.fragment.HideMapFragment;
import com.example.map_originnal.ke_thua.MainCallbacks;
import com.example.map_originnal.fragment.MapFragment;
import com.example.map_originnal.model.User;
import com.example.map_originnal.fragment.ProfileFragment;
import com.example.map_originnal.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener , MainCallbacks {

    //Views
    BottomNavigationView bottomNavigationView;
    BottomSheetDialog bottom_Dialog_List_Family, bottomSheetDialogAddFamily;
    BottomSheetDialog bottomSheetDialog;


    //Map API
    LatLng locationCurrent, locationFriend;

    // Create Fragment
    ProfileFragment profileFragment_activity = new ProfileFragment();
    MapFragment map_Fragment_activity = new MapFragment();
    HideMapFragment hideMap = new HideMapFragment();

    //Family scroll
    ArrayList<User> users;
    RecyclerView lvFamilys;

    //Firebase
    FirebaseAuth auth;
    FirebaseUser current_user;
    DatabaseReference friendsReference;
    DatabaseReference userReference;
    ValueEventListener friendsListener;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // set up menu bottom
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.map);



        // Định danh user
        auth = FirebaseAuth.getInstance();
        current_user = auth.getCurrentUser();


        //load friends
        loadUsers();


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.person:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment_activity).commit();
                return true;


            case R.id.explore:
                startActivity(new Intent(this, FindFriend.class));
                return true;

            case R.id.request:
                startActivity(new Intent(this, ViewListFriend.class));
                return true;

            case R.id.map:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, map_Fragment_activity).commit();
                return true;
        }
        return false;
    }


    // function override
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMsgFromFragToMain(String sender, String strValue) {
        if (sender.equals("Profile-Frag") && strValue.equals("Back")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment_activity).commit();
        }
        if (sender.equals("ProfileFragment-Frag") && strValue.equals("Profile")) {
            chuyenSangProflie();
        }
        if (sender.equals("MapFragment-Frag") && strValue.equals("ShowList")) {
            chuyenSangListFamilys();
        }
        if (sender.equals("List-Family") && strValue.equals("Back")) {
            bottom_Dialog_List_Family.hide();
        }
        if (sender.equals("List-Family") && strValue.equals("Dialog-AddMember")) {
            startActivity(new Intent(this, FindFriend.class));
        }
        if (sender.equals("Hide-MapFragment") && strValue.equals("ShowMap")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, map_Fragment_activity).commit();
        }
    }

    @Override
    public void onLocationFromFragToMain(String sender, LatLng Value) {
        locationCurrent = Value;
        hideMap.onLocationFromMainToFrag("main", locationCurrent);
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, hideMap).commit();
    }

    @Override
    public void onLocationFromAdapToMain(String sender, LatLng Value) {
        locationFriend = Value;
        map_Fragment_activity.onLocationFromMainToFrag("main",locationFriend);
    }

    @Override
    public void finish() {
        super.finish();
        FirebaseAuth.getInstance().signOut();
    }



    private void chuyenSangProflie() {
        bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.dialog_change_profile,
                (LinearLayout) findViewById(R.id.bottomSheetContainer)
        );
        bottomSheetView.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void chuyenSangListFamilys() {
        bottom_Dialog_List_Family = new BottomSheetDialog(
                MainActivity.this
        );

        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.dialog_list_family,
                (LinearLayout) findViewById(R.id.bottom_Container_Family)
        );

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);

        //setup btn add family
        bottomSheetView.findViewById(R.id.imgAddFamily).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindFriend.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // setup Recyclerview same list view
        lvFamilys = bottomSheetView.findViewById(R.id.lvFamily);
        lvFamilys.setLayoutManager(layoutManager);

        AdapterListFamily adapterListFamily = new AdapterListFamily(MainActivity.this, users);
        lvFamilys.setAdapter(adapterListFamily);

        bottom_Dialog_List_Family.setContentView(bottomSheetView);
        bottom_Dialog_List_Family.show();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadUsers() {
        users = new ArrayList<User>();
        friendsReference = FirebaseDatabase.getInstance().getReference("Friends").child(current_user.getUid());
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        friendsListener = friendsReference.addValueEventListener(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String friendID = snapshot.getKey();
                    userReference.child(friendID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User a = snapshot.getValue(User.class);
                            // điểu kiện để load user
                            if (a != null && current_user != null) {
                                if (!current_user.getUid().equals(a.getId())) {
                                       users.add(a);
                                }
                          }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
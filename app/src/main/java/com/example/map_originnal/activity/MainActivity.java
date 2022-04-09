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
import com.example.map_originnal.fragment.HomeFragment;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener , MainCallbacks {

    //Views
    BottomNavigationView bottomNavigationView;
    BottomSheetDialog bottom_Dialog_List_Family, bottomSheetDialogAddFamily;
    BottomSheetDialog bottomSheetDialog;

    //Map API
    LatLng locationCurrent;

    // Create Fragment
    ProfileFragment profileFragment_activity = new ProfileFragment();
    MapFragment map_Fragment_activity = new MapFragment();
    HomeFragment home_Fragment_activity = new HomeFragment();
    HideMapFragment hideMap = new HideMapFragment();

    //Family scroll
    ArrayList<User> users;
    RecyclerView lvFamilys;

    //Firebase
    FirebaseAuth auth;
    FirebaseUser current_user;
    DatabaseReference userReference;
    ValueEventListener userListener;

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

        //Load User
        loadUsers();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (current_user == null) {
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.person:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment_activity).commit();
                return true;


            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, home_Fragment_activity).commit();
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
            chuyenSangDialogAddFamily();
        }
        if (sender.equals("Hide-MapFragment") && strValue.equals("ShowMap")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, map_Fragment_activity).commit();
        }

/*        if (sender.equals("Start-Frag") && strValue.equals("Profile-Frag")){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment_activity).commit();
        }
        if (sender.equals("Login-Frag") && strValue.equals("Profile-Frag")){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment_activity).commit();
        }

        if (sender.equals("ProfileFragment-Frag") && strValue.equals("Start-Frag")){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, startFragment).commit();
        }*/
    }

    @Override
    public void onLocationFromFragToMain(String sender, LatLng Value) {
        locationCurrent = Value;
        hideMap.onLocationFromMainToFrag("main", locationCurrent);
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, hideMap).commit();
    }

    @Override
    public void finish() {
        super.finish();
        FirebaseAuth.getInstance().signOut();
    }

    // function Tự tạo

    private void chuyenSangDialogAddFamily() {
        bottomSheetDialogAddFamily = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.dialog_signin,
                (LinearLayout) findViewById(R.id.dialog_addfamily)
        );
        bottomSheetDialogAddFamily.setContentView(bottomSheetView);
        bottomSheetDialogAddFamily.show();
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


        // setup Recyclerview same list view

        lvFamilys = bottomSheetView.findViewById(R.id.lvFamily);
        lvFamilys.setLayoutManager(layoutManager);

        AdapterListFamily adapterListFamily = new AdapterListFamily(MainActivity.this, users);
        lvFamilys.setAdapter(adapterListFamily);

        bottom_Dialog_List_Family.setContentView(bottomSheetView);
        bottom_Dialog_List_Family.show();

    }

/*    private ArrayList<User> giaLapDuLieu() {
        ArrayList<User> a = new ArrayList<>();
        a.add(new User("Anh","https://res.cloudinary.com/imag/image/upload/v1638508152/Shopshoes/show3_f6ckhp.jpg"));
        a.add(new User("Vu","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        return a;
    }*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadUsers() {
        users = new ArrayList<User>();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        userListener = userReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println(snapshot);
                    User a = snapshot.getValue(User.class);
                    if (a != null && current_user != null) {
                        if (!current_user.getUid().equals(a.getId())) {
                            /*                            if (a.getOnline().indexOf("False")==-1)*/
                            users.add(a);
                        } else {
                            String onlineAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                            snapshot.getRef().child("online").setValue("True////" + onlineAt);
                        }
                    }

                }
                System.out.println(users.size());

/*                ContactAdapter adapter = new ContactAdapter(ContactActivity.this,R.layout.adapter_custom_contact,users);
                contacts.setAdapter(adapter);*/
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String offlineAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        DatabaseReference onlineRef = FirebaseDatabase.getInstance().getReference("Users/" + current_user.getUid() + "/online");


    }
}
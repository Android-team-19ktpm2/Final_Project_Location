package com.example.map_originnal.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.map_originnal.Adapter.AdapterListFamily;
import com.example.map_originnal.fragment.HideMapFragment;
import com.example.map_originnal.fragment.HomeFragment;
import com.example.map_originnal.fragment.LoginFragment;
import com.example.map_originnal.ke_thua.MainCallbacks;
import com.example.map_originnal.fragment.MapFragment;
import com.example.map_originnal.model.MemberFamily;
import com.example.map_originnal.fragment.ProfileFragment;
import com.example.map_originnal.R;
import com.example.map_originnal.fragment.RegisterFragment;
import com.example.map_originnal.StartFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener , MainCallbacks {

    BottomNavigationView bottomNavigationView;
    BottomSheetDialog bottom_Dialog_List_Family,bottomSheetDialogAddFamily;
    BottomSheetDialog bottomSheetDialog;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    LatLng locationCurrent;

    // Create Fragment
    LoginFragment loginFragment = new LoginFragment();
    RegisterFragment registerFragment = new RegisterFragment();
    ProfileFragment profileFragment_activity = new ProfileFragment();
    MapFragment map_Fragment_activity = new MapFragment();
    HomeFragment home_Fragment_activity = new HomeFragment();
    HideMapFragment hideMap = new HideMapFragment();
    StartFragment startFragment = new StartFragment();

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
        firebaseUser = auth.getCurrentUser();
        
    }


    


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.person:
                // Định danh user
                if (firebaseUser==null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, startFragment).commit();
                }
                else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment_activity).commit();
                }
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
    @Override
    public void onMsgFromFragToMain(String sender, String strValue) {
        if (sender.equals("Profile-Frag") && strValue.equals("Back")){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment_activity).commit();
        }
        if (sender.equals("ProfileFragment-Frag") && strValue.equals("Profile")){
            chuyenSangProflie();
        }
        if (sender.equals("MapFragment-Frag") && strValue.equals("ShowList")){
            chuyenSangListFamilys();
        }
        if (sender.equals("List-Family") && strValue.equals("Back")){
            bottom_Dialog_List_Family.hide();
        }
        if (sender.equals("List-Family") && strValue.equals("Dialog-AddMember")){
            chuyenSangDialogAddFamily();
        }
        if (sender.equals("Hide-MapFragment") && strValue.equals("ShowMap")){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, map_Fragment_activity).commit();
        }
        if (sender.equals("Login-Frag") && strValue.equals("Register-Frag")){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, registerFragment).commit();
        }
        if (sender.equals("Register-Frag") && strValue.equals("Login-Frag")){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, loginFragment).commit();
        }
        if (sender.equals("Start-Frag") && strValue.equals("Profile-Frag")){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment_activity).commit();
        }
        if (sender.equals("Login-Frag") && strValue.equals("Profile-Frag")){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment_activity).commit();
        }
        if (sender.equals("Start-Frag") && strValue.equals("Login-Frag")){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, loginFragment).commit();
        }
        if (sender.equals("ProfileFragment-Frag") && strValue.equals("Start-Frag")){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, startFragment).commit();
        }
    }

    @Override
    public void onLocationFromFragToMain(String sender, LatLng Value) {
        locationCurrent = Value;
        hideMap.onLocationFromMainToFrag("main",locationCurrent);
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
                MainActivity.this,R.style.BottomSheetDialogTheme
        );
        View bottomSheetView  = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.dialog_signin,
                (LinearLayout) findViewById(R.id.dialog_addfamily)
        );
        bottomSheetDialogAddFamily.setContentView(bottomSheetView);
        bottomSheetDialogAddFamily.show();
    }

    private void chuyenSangProflie() {
        bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this,R.style.BottomSheetDialogTheme
        );
        View bottomSheetView  = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.dialog_change_profile,
                (LinearLayout) findViewById(R.id.bottomSheetContainer)
        );
        bottomSheetView.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Share",Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


    private void chuyenSangListFamilys() {

        ArrayList<MemberFamily> objects;
        RecyclerView lvFamilys;

        bottom_Dialog_List_Family = new BottomSheetDialog(
                MainActivity.this
        );

        View bottomSheetView  = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.dialog_list_family,
                (LinearLayout) findViewById(R.id.bottom_Container_Family)
        );

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this , LinearLayoutManager.HORIZONTAL, false);


        // setup Recyclerview same list view
        objects= giaLapDuLieu();
        lvFamilys = bottomSheetView.findViewById(R.id.lvFamily);
        lvFamilys.setLayoutManager(layoutManager);
        AdapterListFamily adapterListFamily = new AdapterListFamily(MainActivity.this, objects);
        lvFamilys.setAdapter(adapterListFamily);

        bottom_Dialog_List_Family.setContentView(bottomSheetView);
        bottom_Dialog_List_Family.show();

    }

    private ArrayList<MemberFamily> giaLapDuLieu() {
        ArrayList<MemberFamily> a = new ArrayList<>();
        a.add(new MemberFamily("Anh","https://res.cloudinary.com/imag/image/upload/v1638508152/Shopshoes/show3_f6ckhp.jpg"));
        a.add(new MemberFamily("Vu","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        return a;
    }




}
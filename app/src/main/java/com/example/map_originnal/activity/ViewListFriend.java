package com.example.map_originnal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.map_originnal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewListFriend extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference requests,users;
    ListView lv ;
    ArrayList<String> searchedList ;
    String idGuess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_friend);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        users = FirebaseDatabase.getInstance().getReference().child("Users");
        requests = FirebaseDatabase.getInstance().getReference().child("Requests");
        searchedList =  new ArrayList<>();
        lv = findViewById(R.id.lvFriends);

        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LoadFriends(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ValueEventListener event1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ShowFriends(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        requests.addValueEventListener(event);
        users.addValueEventListener(event1);


    }
    private void ShowFriends(DataSnapshot snapshot){
        ArrayList<String> searchedListFriend =  new ArrayList<>();
        if(snapshot.exists()) {
            for (DataSnapshot ds : snapshot.getChildren()) {
//                Log.e("Test 1",String.valueOf(ds.child(ds.getKey())));

                for(String str:searchedList){
                    if(str.equals(ds.getKey())){
                        searchedListFriend.add(String.valueOf(ds.child("email").getValue()));
                        idGuess = ds.getKey();

                    }
                }

            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,searchedListFriend);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(ViewListFriend.this,ViewFriend.class);

                    Log.e("position ",position+"");

                    intent.putExtra("userID",idGuess);
                    startActivity(intent);
                }
            });

        }

    }


    private void LoadFriends(DataSnapshot snapshot) {

        if(snapshot.exists()) {
            for (DataSnapshot ds : snapshot.getChildren()) {
//                Log.e("Test 1",String.valueOf(ds.child(ds.getKey())));

                for(DataSnapshot ds1 : ds.getChildren()){
//                    if(String.valueOf(ds1.getKey()).equals(mUser.getUid())){
//                        Log.e("Test 1","Request exist");
//                    }
                    if(String.valueOf(ds1.getKey()).equals(mUser.getUid())){

                        searchedList.add(ds.getKey());
                        Log.e("Test 1 ",ds.getKey());
                    }


                }

            }

        }

    }

}
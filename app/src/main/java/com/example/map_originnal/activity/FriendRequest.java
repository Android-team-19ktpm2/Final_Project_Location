package com.example.map_originnal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class FriendRequest extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference friends;
    ListView lv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_friend);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        friends = FirebaseDatabase.getInstance().getReference().child("Friends").child(mUser.getUid());
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
        friends.addValueEventListener(event);
    }

    private void LoadFriends(DataSnapshot snapshot) {
        ArrayList<String> searchedList =  new ArrayList<>();
        if(snapshot.exists()) {
            for (DataSnapshot ds : snapshot.getChildren()) {
                if(String.valueOf(ds.child("status").getValue()).equals("friend")){
                    searchedList.add(String.valueOf(ds.child("email").getValue()));
                }

            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,searchedList);
            lv.setAdapter(arrayAdapter);
        }

    }

}
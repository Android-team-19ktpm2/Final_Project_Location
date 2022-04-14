package com.example.map_originnal.activity;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.map_originnal.R;
import com.example.map_originnal.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FindFriend extends AppCompatActivity {
    DatabaseReference mRef;
    private ListView listData;
    ImageView imgSearch;
    AutoCompleteTextView autoCompleteTextView;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        setContentView(R.layout.activity_find_friend);

        mRef = FirebaseDatabase.getInstance().getReference("Users");

        listData = (ListView) findViewById(R.id.foundUsers);
        imgSearch = findViewById(R.id.searchImg);
        autoCompleteTextView = findViewById(R.id.search_bar);

     //   Log.e("Hello",mRef.getKey()) ;
        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mRef.addListenerForSingleValueEvent(event);

    }

    private void populateSearch(DataSnapshot snapshot) {
        ArrayList<String> searchedList =  new ArrayList<>();
        if(snapshot.exists()){
            for(DataSnapshot ds : snapshot.getChildren()){
                searchedList.add(String.valueOf(ds.child("email").getValue()));
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,searchedList);
            autoCompleteTextView.setAdapter(arrayAdapter);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String username = autoCompleteTextView.getText().toString();
                    searchUser(username);
                }
            });

        }
    }

    private void searchUser(String username) {
        Query query = mRef.orderByChild("email").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ArrayList<String> listUser = new ArrayList<>();
                    ArrayList<String> listUserID = new ArrayList<>();
                    for(DataSnapshot ds: snapshot.getChildren()){

                        User user = new User(String.valueOf(ds.child("id").getValue()),
                                String.valueOf(ds.child("email").getValue()),
                                String.valueOf(ds.child("avatar").getValue()),
                                String.valueOf(ds.child("first_name").getValue()),
                                String.valueOf(ds.child("last_name").getValue()),
                                String.valueOf(ds.child("online").getValue()));
                        listUser.add(user.getEmail());
                        listUserID.add(ds.getKey());
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext() , android.R.layout.simple_spinner_item,listUser);
                    listData.setAdapter(adapter);
                    listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent(FindFriend.this,ViewFriend.class);
                            Log.e("position ",position+"");
                            intent.putExtra("userID",listUserID.get(position));
                            startActivity(intent);
                        }
                    });
                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
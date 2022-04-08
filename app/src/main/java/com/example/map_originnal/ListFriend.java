package com.example.map_originnal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFriend extends AppCompatActivity {
    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseUser firebaseUser;
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_friend);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference("Friends").child(firebaseUser.getUid());
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        listView = findViewById(R.id.listFriend);
        LoadListFriend();
    }

    private void LoadListFriend() {
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ArrayList<MemberFamily> arrayList = new ArrayList<>();
                    if(snapshot.child("status").getValue().toString().equals("friend")){
                   //     MemberFamily user = new MemberFamily()
                        //Adapter adapter = new Adapter((Activity) context, R.layout.items, objects);
                        //listView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
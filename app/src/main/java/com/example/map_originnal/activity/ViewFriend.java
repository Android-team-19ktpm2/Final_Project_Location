package com.example.map_originnal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.map_originnal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFriend extends AppCompatActivity {
    DatabaseReference mUserRef,requestRef,friendRef,userPartner;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String prfileImageUrl,username,address;
//    String userID;
    TextView txtUsername, txtAddress;
    Button btnRequest;
    Button btnDecline;
    String currentState="nothing_happen";
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);
        String userID = getIntent().getStringExtra("userID");
        Toast.makeText(this,""+userID,Toast.LENGTH_SHORT).show();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userPartner = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        mUserRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        txtAddress = findViewById(R.id.txtAddress);
        btnRequest = findViewById(R.id.btnRequest);
        btnDecline = findViewById(R.id.btnDecline);
        txtUsername = findViewById(R.id.txtName);
        if(userID.equals(mUser.getUid())){
            btnRequest.setEnabled(false);
            btnDecline.setEnabled(false);
        }


        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAction(userID);
            }
        });
        CheckUserExistance(userID);
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Unfriend(userID);
            }
        });
        LoadUser();
    }

    private void Unfriend(String userID) {
        if(currentState.equals("friend")){
            friendRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        friendRef.child(userID).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText( ViewFriend.this,"You are unfriend", Toast.LENGTH_SHORT).show();
                                    currentState = "nothing_happen";
                                    btnRequest.setText("Send Friend Request");
                                    btnDecline.setVisibility(View.GONE);
                                }
                            }
                        });

                    }
                }
            });
        }
        if(currentState.equals("he_sent_pending")){
            HashMap hashMap = new HashMap();
            hashMap.put("status","decline");
            requestRef.child(userID).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ViewFriend.this,"You have decline friend",Toast.LENGTH_SHORT).show();
                        currentState = "he_sent_decline";
                        btnRequest.setVisibility(View.GONE);
                        btnDecline.setVisibility(View.GONE);
                    }
                }
            });
        }
    }




// van de nam day
    private void CheckUserExistance(String userID) {
        friendRef.child(mUser.getUid()).child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    currentState="friend";
                    btnRequest.setText("Friend");
                    btnDecline.setText("Unfriend");
                    btnDecline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        friendRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    currentState="friend";
                    btnRequest.setText("Friend");
                    btnDecline.setText("Unfriend");
                    btnDecline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        requestRef.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        currentState="I_send_pending";
                        btnRequest.setText("Cancel Friend Request");
                        btnDecline.setVisibility(View.GONE);

                    }
                    else   if(snapshot.child("status").getValue().toString().equals("decline")){
                        currentState="I_send_decline";
                        btnRequest.setText("Request");
                        btnDecline.setVisibility(View.GONE);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        currentState = "he_sent_pending";
                        btnRequest.setText("Accept Friend Request");
                        btnDecline.setText("Decline Friend Request");
                        btnDecline.setVisibility(View.VISIBLE);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(currentState.equals("nothing_happen")){
            currentState = "nothing_happen";
            btnRequest.setText("Send Friend Request");
            btnDecline.setVisibility(View.GONE);

        }
    }

    private void PerformAction(String userID) {
        if(currentState.equals("nothing_happen")) {
            HashMap hashMap = new HashMap();
            hashMap.put("status", "pending");
            requestRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ViewFriend.this, "You have sent Friend request", Toast.LENGTH_SHORT).show();
                        btnDecline.setVisibility(View.GONE);
                        currentState = "I_send_pending";
                        btnRequest.setText("Cancel");
                    } else {
                        Toast.makeText(ViewFriend.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else   if(currentState.equals("I_send_pending")||currentState.equals("I_send_decline")) {
            requestRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ViewFriend.this, "You have canceled Friend request", Toast.LENGTH_SHORT).show();
                        currentState = "nothing_happen";
                        btnRequest.setText("Send Friend Request");
                        btnDecline.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(ViewFriend.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        // van den accecpt friends
        else  if (currentState.equals("he_sent_pending")) {
            requestRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("status", "friend");
                        requestRef.child(userID).child(mUser.getUid()).removeValue();
                        requestRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    friendRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(ViewFriend.this, "You added friend", Toast.LENGTH_SHORT).show();
                                            currentState = "friend";
                                            btnRequest.setText("Friend");
                                            btnDecline.setText("Unfriend");
                                            btnRequest.setEnabled(false);
                                        }
                                    });

                                    friendRef.child(userID).child(mUser.getUid()).updateChildren(hashMap);

                                }
                            }
                        });
                    }
                }
            });

            if (currentState.equals("friend")) {
                btnRequest.setText("Friend");
                currentState = "friend";
                btnDecline.setText("Unfriend");
                btnRequest.setEnabled(false);
            }
        }


    }

    private void LoadUser(){
        userPartner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    prfileImageUrl = String.valueOf(snapshot.child("avatar").getValue());
                    username = String.valueOf(snapshot.child("email").getValue());
                    txtUsername.setText(username);
                }
                else {
                    Toast.makeText(ViewFriend.this,"Data not Found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriend.this,""+error.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }

        });

    }



}

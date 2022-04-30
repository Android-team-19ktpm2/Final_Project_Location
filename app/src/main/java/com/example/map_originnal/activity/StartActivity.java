package com.example.map_originnal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.map_originnal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StartActivity extends Activity {

    Button btn_signup_email, btn_signup_gg;
    TextView tv_signin;

    FirebaseAuth auth;
    FirebaseUser current_user;
    DatabaseReference onlineRef, offlineRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        auth= FirebaseAuth.getInstance();
        current_user = auth.getCurrentUser();

        tv_signin = findViewById(R.id.start_tv_signin);
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this,SignInActivity.class));
            }
        });

        btn_signup_email =findViewById(R.id.start_btn_signup_email);
        btn_signup_gg =findViewById(R.id.start_btn_signup_google);

        btn_signup_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this,SignUpActivity.class));
            }
        });

        btn_signup_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        if (current_user != null) {
            String onlineAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

            //set online
            onlineRef = FirebaseDatabase.getInstance().getReference("Users").child(current_user.getUid()).child("online");
            onlineRef.setValue("True////" + onlineAt);

            //set event listener offline
            offlineRef = FirebaseDatabase.getInstance().getReference("Users/"+current_user.getUid()+"/online");
            offlineRef.onDisconnect().setValue("False////" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}


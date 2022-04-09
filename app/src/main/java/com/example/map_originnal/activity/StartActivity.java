package com.example.map_originnal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.map_originnal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends Activity {

    Button btn_signup_email, btn_signup_gg;
    TextView tv_signin;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        auth= FirebaseAuth.getInstance();

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
}


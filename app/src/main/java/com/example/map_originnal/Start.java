package com.example.map_originnal;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Start extends AppCompatActivity {


    TextView txtLogin;
    Button btnSignUpEmail,btnSignUpGG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Intent main = new Intent(getApplicationContext(),MainActivity.class);
        txtLogin =findViewById(R.id.txtLogin);
        btnSignUpEmail = findViewById(R.id.btnSignUpEmail);
        btnSignUpGG = findViewById(R.id.btnSignUpGG);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.putExtra("CONDITION","LOGIN");
                startActivity(main);
                finish();
            }
        });
        btnSignUpGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.putExtra("CONDITION","SIGNUP_GG");
                startActivity(main);
                finish();
            }
        });
        btnSignUpEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.putExtra("CONDITION","SIGNUP_EMAIL");
                startActivity(main);
                finish();
            }
        });

    }
}
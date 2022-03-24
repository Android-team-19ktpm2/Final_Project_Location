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

        txtLogin =findViewById(R.id.txtLogin);
        btnSignUpEmail = findViewById(R.id.btnSignUpEmail);
        btnSignUpGG = findViewById(R.id.btnSignUpGG);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(main);
                finish();
            }
        });

    }
}
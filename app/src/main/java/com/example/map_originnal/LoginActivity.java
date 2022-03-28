package com.example.map_originnal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText edt_sign_in_email,edt_sign_in_pwd;
    Button btn_sign_in;
    TextView btn_sign_in_forgot,btn_sign_in_sign_up;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_sign_in_email=findViewById(R.id.edt_sign_in_email);
        edt_sign_in_pwd=findViewById(R.id.edt_sign_in_pwd);

        btn_sign_in=findViewById(R.id.btn_sign_in);
        btn_sign_in_forgot=findViewById(R.id.btn_sign_in_forgot);
        btn_sign_in_sign_up=findViewById(R.id.btn_sign_in_sign_up);


        auth=FirebaseAuth.getInstance();

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btn_sign_in_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }

    private void signIn()
    {
        String email= edt_sign_in_email.getText().toString();
        String pwd= edt_sign_in_pwd.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            edt_sign_in_email.setError("Email is required!");
            edt_sign_in_email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pwd))
        {
            edt_sign_in_pwd.setError("Password is required!");
            edt_sign_in_pwd.requestFocus();
            return;
        }

        auth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, "Welcome back "+email, Toast.LENGTH_SHORT).show();
                    //Redirect Main Activity
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
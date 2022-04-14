package com.example.map_originnal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.map_originnal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SignInActivity extends Activity {

    ImageButton btn_back;
    Button btn_signin;
    EditText edt_email,edt_pwd;
    TextView btn_reset_pwd,btn_signup;

    FirebaseAuth auth;
    DatabaseReference reference;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        auth=FirebaseAuth.getInstance();

        edt_email=findViewById(R.id.signin_edt_email);
        edt_pwd=findViewById(R.id.signin_edt_pwd);

        btn_signin=findViewById(R.id.signin_btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=edt_email.getText().toString();
                String pwd=edt_pwd.getText().toString();

                if(TextUtils.isEmpty(email))
                {
                    edt_email.setError("Email is required");
                    edt_email.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pwd))
                {
                    edt_pwd.setError("Password is required");
                    edt_pwd.requestFocus();
                    return;
                }
                signIn(email,pwd);
            }
        });

        btn_reset_pwd = findViewById(R.id.signin_btn_reset_pwd);
        btn_reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,ResetPasswordActivity.class));
            }
        });

        btn_signup = findViewById(R.id.signin_btn_signup);btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });

        btn_back = findViewById(R.id.signin_btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    public void signIn(String email, String pwd)
    {
        auth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent intent =new Intent(SignInActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
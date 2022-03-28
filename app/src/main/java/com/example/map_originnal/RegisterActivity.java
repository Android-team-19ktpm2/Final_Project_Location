package com.example.map_originnal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {
    EditText edt_email,edt_pwd, edt_re_pwd;
    Button btn_create;
    TextView btn_login;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_email=findViewById(R.id.edt_sign_up_email);
        edt_pwd=findViewById(R.id.edt_sign_up_pwd);
        edt_re_pwd =findViewById(R.id.edt_sign_up_re_pwd);

        btn_create=findViewById(R.id.btn_sign_up_create);

        btn_login=findViewById(R.id.btn_sign_up_login);

        auth=FirebaseAuth.getInstance();

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                //Redirect Login Activity
                return;
            }
        });
    }

    private void createAccount()
    {
        String email= edt_email.getText().toString();
        String pwd= edt_pwd.getText().toString();
        String re_pwd= edt_re_pwd.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            edt_email.setError("Email is required!");
            edt_email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pwd))
        {
            edt_pwd.setError("Password is required!");
            edt_pwd.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(re_pwd))
        {
            edt_re_pwd.setError("Re-password is required!");
            edt_re_pwd.requestFocus();
            return;
        }

        if (!pwd.equals(re_pwd))
        {
            edt_re_pwd.setError("Password does not match!");
            edt_re_pwd.requestFocus();
            return;
        }

        auth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    //Redirect Login Activity
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText edt_email,edt_pwd, edt_re_pwd;
    Button btn_create;
    TextView btn_login;

    FirebaseAuth auth;
    DatabaseReference reference;
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
                    FirebaseUser user=auth.getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

                    HashMap<String,String> userData = new HashMap<>();
                    userData.put("id",user.getUid());
                    userData.put("username",edt_email.getText().toString());
                    userData.put("avatar","default");

                    reference.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                //Redirect to Login Screen
                                Toast.makeText(RegisterActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
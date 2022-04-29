package com.example.map_originnal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.map_originnal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends Activity {
    Button btn_signup;
    ImageButton btn_back;

    EditText edt_full_name,edt_email, edt_pwd, edt_phone;
    CheckBox cb_term;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth=FirebaseAuth.getInstance();

        edt_full_name=findViewById(R.id.signup_edt_full_name);
        edt_phone=findViewById(R.id.signup_edt_phone);
        edt_email=findViewById(R.id.signup_edt_email);
        edt_pwd=findViewById(R.id.signup_edt_password);

        cb_term = findViewById(R.id.signup_cb_term);

        btn_signup=findViewById(R.id.signup_btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String full_name=edt_full_name.getText().toString();
                String phone=edt_phone.getText().toString();
                String email=edt_email.getText().toString();
                String pwd=edt_pwd.getText().toString();

                if(TextUtils.isEmpty(full_name))
                {
                    edt_full_name.setError("First name is required");
                    edt_full_name.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(phone))
                {
                    edt_phone.setError("Phone is required");
                    edt_phone.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(email))
                {
                    edt_email.setError("Email is required");
                    edt_email.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(pwd))
                {
                    edt_pwd.setError("Password is required");
                    edt_pwd.requestFocus();
                    return;
                }

                if (!cb_term.isChecked())
                {
                    cb_term.setError("You must tick here");
                    cb_term.requestFocus();
                    return;
                }

                auth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user=auth.getCurrentUser();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

                            HashMap<String,String> data= new HashMap<>();
                            data.put("id",user.getUid());
                            data.put("avatar","default");
                            data.put("full_name",edt_full_name.getText().toString());
                            data.put("phone",edt_phone.getText().toString());
                            data.put("dob","...");
                            data.put("email",edt_email.getText().toString());
                            data.put("online","False////Undefine");

                            reference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(SignUpActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btn_back = findViewById(R.id.signup_btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}
package com.example.map_originnal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText edt_email;
    Button btn_send;
    ImageButton btn_back;
    TextView btn_reset_pwd;

    FirebaseAuth auth;
    FirebaseUser current_user;
    DatabaseReference reference;

    String email;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        auth = FirebaseAuth.getInstance();

        edt_email = findViewById(R.id.reset_password_edt_email);

        btn_send = findViewById(R.id.reset_password_btn_reset);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edt_email.getText().toString();
//                reference = FirebaseDatabase.getInstance().getReference("Users");
//
//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot snapshot: dataSnapshot.getChildren())
//                        {
//                            if (user)
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });


                if(TextUtils.isEmpty(email))
                {
                    edt_email.setError("Email is required");
                    edt_email.requestFocus();
                }

                else {
                    resetPassword(email);
                }
            }
        });

        btn_back = findViewById(R.id.reset_password_btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ResetPasswordActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    public void resetPassword(String email)
    {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(ResetPasswordActivity.this, "Sent successful, please check your email", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(ResetPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
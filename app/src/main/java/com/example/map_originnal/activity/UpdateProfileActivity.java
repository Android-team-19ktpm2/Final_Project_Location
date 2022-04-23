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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.map_originnal.R;
import com.example.map_originnal.fragment.ProfileFragment;
import com.example.map_originnal.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText edt_first_name, edt_last_name, edt_dob, edt_email, edt_phone;
    Button btn_apply;
    ImageButton btn_back;

    RadioGroup gender_group;
    RadioButton male,female;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        edt_first_name = findViewById(R.id.update_edt_first_name);
        edt_last_name = findViewById(R.id.update_edt_last_name);
        edt_dob = findViewById(R.id.update_edt_dob);
        edt_email = findViewById(R.id.update_edt_mail);
        edt_phone = findViewById(R.id.update_edt_phone);

        btn_apply = findViewById(R.id.update_btn_apply);
        btn_back = findViewById(R.id.update_btn_back);

        gender_group = findViewById(R.id.update_radio_group_gender);
        male = findViewById(R.id.radio_male);
        female = findViewById(R.id.female);

        auth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                user.display();

                edt_first_name.setText(user.getFirst_name());
                edt_last_name.setText(user.getLast_name());

/*                if (user.getDob().equals(".unknown."))
                    edt_dob.setText("");
                else
                    edt_dob.setText(user.getDob());*/

                edt_email.setText(user.getEmail());
                edt_phone.setText(user.getPhone());

/*                if (user.getPhone().equals(".unknown."))
                    edt_phone.setText("");
                else
                    edt_phone.setText(user.getDob());*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first_name = edt_first_name.getText().toString();
                if (TextUtils.isEmpty(first_name))
                {
                    edt_first_name.setError("First name is required");
                    edt_first_name.requestFocus();
                    return;
                }


                String last_name = edt_last_name.getText().toString();
                if (TextUtils.isEmpty(last_name))
                {
                    edt_last_name.setError("Last name is required");
                    edt_last_name.requestFocus();
                    return;
                }

                String dob = edt_dob.getText().toString();


                String email = edt_email.getText().toString();
                if (TextUtils.isEmpty(email))
                {
                    edt_email.setError("Email is required");
                    edt_email.requestFocus();
                    return;
                }


                String phone = edt_phone.getText().toString();


                update(first_name, last_name, dob, email, phone);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateProfileActivity.this, MainActivity.class));
            }
        });

        gender_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
            }
        });

    }

    void update(String first_name , String last_name, String dob, String email, String phone)
    {
/*        if(TextUtils.isEmpty(dob))
            dob = ".unknown.";
        if(TextUtils.isEmpty(phone))
            phone = ".unknown.";

        data.put("dob", dob);
        data.put("phone", phone);
        data.put("online", "True");*/

        reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("email");
        reference.setValue(email);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("first_name");
        reference.setValue(first_name);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("last_name");
        reference.setValue(last_name);

    }
}
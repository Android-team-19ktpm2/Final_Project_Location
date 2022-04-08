package com.example.map_originnal.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.map_originnal.R;
import com.example.map_originnal.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {


    EditText edt_sign_in_email,edt_sign_in_pwd;
    Button btn_sign_in;
    TextView btn_sign_in_forgot,btn_sign_in_sign_up;

    FirebaseAuth auth;
    MainActivity main;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    Toast.makeText(getActivity(), "Welcome back "+email, Toast.LENGTH_SHORT).show();
                    main.onMsgFromFragToMain("Login-Frag","Profile-Frag");
                }
                else
                {
                    Toast.makeText(getActivity(), task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        main = (MainActivity)getActivity();

        View v =  inflater.inflate(R.layout.fragment_login, container, false);
        edt_sign_in_email=v.findViewById(R.id.edt_sign_in_email);
        edt_sign_in_pwd=v.findViewById(R.id.edt_sign_in_pwd);

        btn_sign_in=v.findViewById(R.id.btn_sign_in);
        btn_sign_in_forgot=v.findViewById(R.id.btn_sign_in_forgot);
        btn_sign_in_sign_up=v.findViewById(R.id.btn_sign_in_sign_up);


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
                main.onMsgFromFragToMain("Login-Frag","Register-Frag");
            }
        });

        return v;
    }
}
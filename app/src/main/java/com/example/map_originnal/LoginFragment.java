package com.example.map_originnal;

import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    EditText edt_sign_in_email,edt_sign_in_pwd;
    Button btn_sign_in;
    TextView btn_sign_in_forgot,btn_sign_in_sign_up;

    FirebaseAuth auth;

    MainActivity main;
    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

//                    Intent intent= new Intent(LoginActivity.this,ChatActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
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
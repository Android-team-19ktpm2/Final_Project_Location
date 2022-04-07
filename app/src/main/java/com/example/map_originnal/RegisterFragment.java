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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText edt_email,edt_pwd, edt_re_pwd;
    Button btn_create;
    TextView btn_login;

    FirebaseAuth auth;
    DatabaseReference reference;
    private MainActivity main;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        main = (MainActivity)getActivity();


        View v = inflater.inflate(R.layout.fragment_register, container, false);



        edt_email=v.findViewById(R.id.edt_sign_up_email);
        edt_pwd=v.findViewById(R.id.edt_sign_up_pwd);
        edt_re_pwd =v.findViewById(R.id.edt_sign_up_re_pwd);

        btn_create=v.findViewById(R.id.btn_sign_up_create);

        btn_login=v.findViewById(R.id.btn_sign_up_login);

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
//                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                main.onMsgFromFragToMain("Register-Frag","Login-Frag");
                //Redirect Login Activity
                return;
            }
        });

        return v;
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
                                Toast.makeText(main, "Successful", Toast.LENGTH_SHORT).show();
//                                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();
                            }
                            else
                            {
                                Toast.makeText(main, "Sign up failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(main, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}
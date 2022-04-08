package com.example.map_originnal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.map_originnal.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MainActivity main;


    TextView txtLogin;
    Button btnSignUpEmail,btnSignUpGG;

    FirebaseAuth auth;

    public StartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
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
        View v = inflater.inflate(R.layout.fragment_start, container, false);
        main = (MainActivity)getActivity();


        txtLogin =v.findViewById(R.id.txtLogin);
        btnSignUpEmail = v.findViewById(R.id.btnSignUpEmail);
        btnSignUpGG = v.findViewById(R.id.btnSignUpGG);

        auth=FirebaseAuth.getInstance();

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("Start-Frag","Login-Frag");
            }
        });

//        btnSignUpGG.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                main.putExtra("CONDITION","SIGNUP_GG");
//                startActivity(main);
//                finish();
//            }
//        });
//        btnSignUpEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                main.putExtra("CONDITION","SIGNUP_EMAIL");
//                startActivity(main);
//                finish();
//            }
//        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null)
        {
            main.onMsgFromFragToMain("Start-Frag","Profile-Frag");
        }


    }
}
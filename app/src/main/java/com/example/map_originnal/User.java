package com.example.map_originnal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User extends Fragment {
    ImageView profile_image_avatar;
    ImageButton profile_image_edit,profile_image_favour,profile_image_list,profile_image_logout;
    MainActivity main;
    TextView txtEmail;


    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    public User(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout layout_user = (LinearLayout) inflater.inflate(R.layout.activity_user, container, false);
        main = (MainActivity)getActivity();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();




        profile_image_edit= layout_user.findViewById(R.id.profile_image_edit);
        profile_image_edit.setImageResource(R.drawable.edit);

        profile_image_favour=layout_user.findViewById(R.id.profile_image_favour);
        profile_image_favour.setImageResource(R.drawable.heart);

        profile_image_list=layout_user.findViewById(R.id.profile_image_list);
        profile_image_list.setImageResource(R.drawable.list);

        txtEmail = layout_user.findViewById(R.id.profile_tv_mail);


        profile_image_logout=layout_user.findViewById(R.id.profile_image_logout);
        profile_image_logout.setImageResource(R.drawable.logout);

        profile_image_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                main.onMsgFromFragToMain("User-Frag","Start-Frag");
            }
        });
        profile_image_avatar=layout_user.findViewById(R.id.profile_image_avatar);
        profile_image_avatar.setImageResource(R.drawable.avatar);


        if (firebaseUser != null){
            txtEmail.setText(firebaseUser.getEmail());

        }

        profile_image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("User-Frag","Profile");
            }
        });
        return layout_user;

    }

}
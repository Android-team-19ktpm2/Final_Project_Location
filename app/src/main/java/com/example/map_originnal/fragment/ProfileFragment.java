package com.example.map_originnal.fragment;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.map_originnal.R;
import com.example.map_originnal.activity.FindFriend;
import com.example.map_originnal.activity.MainActivity;
import com.example.map_originnal.activity.StartActivity;
import com.example.map_originnal.activity.ViewListFriend;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    ImageView profile_image_avatar;
    ImageButton profile_image_edit,profile_image_favour,profile_image_list,profile_image_logout,findFriend;
    MainActivity main;
    TextView txtEmail,btn_logout, txtFindFriend,viewAllFriend;


    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    public ProfileFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout layout_user = (LinearLayout) inflater.inflate(R.layout.fragment_profile, container, false);

        addControls(layout_user);

        return layout_user;

    }

    private void addControls(LinearLayout layout_user) {
        main = (MainActivity)getActivity();

        // định danh user
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
        profile_image_avatar=layout_user.findViewById(R.id.profile_image_avatar);
        profile_image_avatar.setImageResource(R.drawable.avatar);
        findFriend = layout_user.findViewById(R.id.btnFindFriend);
        txtFindFriend = layout_user.findViewById(R.id.txtFindFriend);
        btn_logout = layout_user.findViewById(R.id.profile_btn_logout);
        viewAllFriend = layout_user.findViewById(R.id.viewAllFriend);
        String email = firebaseUser.getEmail();
        txtEmail.setText(email);
        txtFindFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FindFriend.class);
                startActivity(intent);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getContext(), StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        viewAllFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewListFriend.class);
                startActivity(intent);
            }
        });
    }

}
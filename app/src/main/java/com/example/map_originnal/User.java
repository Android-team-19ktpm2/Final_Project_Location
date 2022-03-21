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

public class User extends Fragment {
    ImageView profile_image_avatar;
    ImageButton profile_image_edit,profile_image_favour,profile_image_list,profile_image_logout;

    public User(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout layout_user = (LinearLayout) inflater.inflate(R.layout.activity_user, container, false);

        profile_image_edit= layout_user.findViewById(R.id.profile_image_edit);
        profile_image_edit.setImageResource(R.drawable.edit);

        profile_image_favour=layout_user.findViewById(R.id.profile_image_favour);
        profile_image_favour.setImageResource(R.drawable.heart);

        profile_image_list=layout_user.findViewById(R.id.profile_image_list);
        profile_image_list.setImageResource(R.drawable.list);

        profile_image_logout=layout_user.findViewById(R.id.profile_image_logout);
        profile_image_logout.setImageResource(R.drawable.logout);

        profile_image_avatar=layout_user.findViewById(R.id.profile_image_avatar);
        profile_image_avatar.setImageResource(R.drawable.avatar);


        profile_image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile profile = new Profile();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profile).commit();
            }
        });
        return layout_user;

    }
}
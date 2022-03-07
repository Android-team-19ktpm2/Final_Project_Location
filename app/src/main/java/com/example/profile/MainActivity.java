package com.example.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView profile_image_edit,profile_image_avatar,profile_image_phone,profile_image_mail,profile_image_favour,profile_image_list,profile_image_logout,profile_image_map,profile_image_family,profile_image_home,profile_image_profile;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_image_avatar=findViewById(R.id.profile_image_profile);
        profile_image_avatar.setImageResource(R.drawable.nav_profile);

        profile_image_edit=findViewById(R.id.profile_image_edit);
        profile_image_edit.setImageResource(R.drawable.edit);

        profile_image_favour=findViewById(R.id.profile_image_favour);
        profile_image_favour.setImageResource(R.drawable.heart);

        profile_image_list=findViewById(R.id.profile_image_list);
        profile_image_list.setImageResource(R.drawable.list);

        profile_image_logout=findViewById(R.id.profile_image_logout);
        profile_image_logout.setImageResource(R.drawable.logout);

        profile_image_map=findViewById(R.id.profile_image_map);
        profile_image_map.setImageResource(R.drawable.nav_map);

        profile_image_family=findViewById(R.id.profile_image_family);
        profile_image_family.setImageResource(R.drawable.nav_list);

        profile_image_home=findViewById(R.id.profile_image_home);
        profile_image_home.setImageResource(R.drawable.nav_home);

        profile_image_avatar=findViewById(R.id.profile_image_avatar);
        profile_image_avatar.setImageResource(R.drawable.avatar);

        /*Favour*//*
        profile_image_favour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        *//*List*//*
        profile_image_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        *//*Log out*//*
        profile_image_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        *//*Map *//*
        profile_image_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        *//*Family*//*
        profile_image_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        *//*Home*//*
        profile_image_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        *//*Profile*//*
        profile_image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
*/
    }
}
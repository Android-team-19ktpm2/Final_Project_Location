package com.example.map_originnal.fragment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.map_originnal.R;
import com.example.map_originnal.activity.FindFriend;
import com.example.map_originnal.activity.FriendRequest;

public class HomeFragment extends Fragment {


    ConstraintLayout txtFindFriend;
    ImageView viewAllFriend;
    public HomeFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        txtFindFriend = v.findViewById(R.id.txtFindFriend);
        viewAllFriend = v.findViewById(R.id.viewAllFriend);

        viewAllFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FriendRequest.class);
                startActivity(intent);
            }
        });
        txtFindFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FindFriend.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
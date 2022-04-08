package com.example.map_originnal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListFamilys extends Fragment {

    private ArrayList<MemberFamily> objects;
    RecyclerView lvFamilys;
    MainActivity main;
    LinearLayout circleImageView;
    CircleImageView addMember ;
    public ListFamilys(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout layout_list_family = (LinearLayout)  inflater.inflate(R.layout.list_family, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL, false);
        addMember = layout_list_family.findViewById(R.id.btnAddMember);
        main = (MainActivity) getActivity();
        objects = giaLapDuLieu();
        lvFamilys = layout_list_family.findViewById(R.id.lvFamily);
        circleImageView = layout_list_family.findViewById(R.id.imgAddFamily);
        lvFamilys.setLayoutManager(layoutManager);
        AdapterListFamily adapterListFamily = new AdapterListFamily(getActivity(), objects);
        lvFamilys.setAdapter(adapterListFamily);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("List-Family","START_FIND_FRIEND");


            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("List-Family","Dialog-AddMember");
                System.out.println("12341333333333333333333333333333");
            }
        });
        return layout_list_family;

    }

    private ArrayList<MemberFamily> giaLapDuLieu() {
        ArrayList<MemberFamily> a = new ArrayList<>();

        a.add(new MemberFamily("Son","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        a.add(new MemberFamily("Anh","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        a.add(new MemberFamily("Vu","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        a.add(new MemberFamily("Viet","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        a.add(new MemberFamily("Son","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        a.add(new MemberFamily("Anh","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        a.add(new MemberFamily("Vu","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        a.add(new MemberFamily("Viet","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        a.add(new MemberFamily("Son","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        a.add(new MemberFamily("Anh","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        a.add(new MemberFamily("Vu","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));
        a.add(new MemberFamily("Viet","http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg"));

        return a;
    }
}
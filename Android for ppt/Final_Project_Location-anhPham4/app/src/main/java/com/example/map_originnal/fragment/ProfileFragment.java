package com.example.map_originnal.fragment;

import androidx.annotation.NonNull;
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
import com.example.map_originnal.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    ImageView profile_image_avatar;
    ImageButton profile_image_edit,profile_image_favour,profile_image_logout;
    MainActivity main;
    TextView txtEmail,btn_logout, txtFindFriend,viewAllFriend,txtName,profile_families;


    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    User current_user;
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





        profile_image_edit= layout_user.findViewById(R.id.profile_image_edit);
        profile_image_edit.setImageResource(R.drawable.edit);
        profile_image_favour=layout_user.findViewById(R.id.profile_image_favour);
        profile_image_favour.setImageResource(R.drawable.heart);
        txtEmail = layout_user.findViewById(R.id.profile_tv_mail);
        profile_image_logout=layout_user.findViewById(R.id.profile_image_logout);
        profile_image_logout.setImageResource(R.drawable.logout);
        profile_image_avatar=layout_user.findViewById(R.id.profile_image_avatar);
        profile_image_avatar.setImageResource(R.drawable.avatar);
        btn_logout = layout_user.findViewById(R.id.profile_btn_logout);
        txtName = layout_user.findViewById(R.id.profile_tv_name);
        profile_families = layout_user.findViewById(R.id.profile_families);


        // định danh user
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null){
            String email = firebaseUser.getEmail();

            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    current_user = snapshot.getValue(User.class);
                    txtEmail.setText(email);
                    txtName.setText(current_user.getLast_name()+" "+current_user.getFirst_name());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        }


        profile_image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialogChangeProfile = new BottomSheetDialog(
                        main, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(main.getApplicationContext()).inflate(
                        R.layout.dialog_change_profile,
                        (LinearLayout) main.findViewById(R.id.bottomSheetContainer)
                );
                bottomSheetDialogChangeProfile.setContentView(bottomSheetView);
                bottomSheetDialogChangeProfile.show();
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
    }

}
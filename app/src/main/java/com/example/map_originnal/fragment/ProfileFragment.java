package com.example.map_originnal.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.map_originnal.R;
import com.example.map_originnal.activity.MainActivity;
import com.example.map_originnal.activity.SignInActivity;
import com.example.map_originnal.activity.SignUpActivity;
import com.example.map_originnal.activity.StartActivity;
import com.example.map_originnal.activity.UpdateProfileActivity;
import com.example.map_originnal.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileFragment extends Fragment {
    MainActivity main;

    Button btn_back, btn_logout, btn_update;
    ImageView avatar;
    TextView tv_email,tv_dob, tv_full_name,tv_phone;

    DatabaseReference reference;
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

        tv_full_name = layout_user.findViewById(R.id.profile_tv_full_name);
        tv_email = layout_user.findViewById(R.id.profile_tv_email);
        tv_phone = layout_user.findViewById(R.id.profile_tv_phone);
        tv_dob = layout_user.findViewById(R.id.profile_tv_dob);

        btn_back = layout_user.findViewById(R.id.profile_btn_back);
        btn_logout = layout_user.findViewById(R.id.profile_btn_log_out);
        btn_update = layout_user.findViewById(R.id.profile_btn_update);

        avatar = layout_user.findViewById(R.id.profile_image_avatar);
        /*profile_image_avatar.setImageResource(R.drawable.avatar);*/


        // firebase user
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null){
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    current_user = snapshot.getValue(User.class);
                    tv_email.setText(current_user.getEmail());
                    tv_full_name.setText(current_user.getLast_name()+" "+current_user.getFirst_name());
/*                    avatar.setImageResource(current_user.getAvatar());*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else
        {
            Intent intent = new Intent(getContext(), StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


        avatar.setOnClickListener(new View.OnClickListener() {
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

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UpdateProfileActivity.class));
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

    void update(String field, String value)
    {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child(field);

        reference.setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
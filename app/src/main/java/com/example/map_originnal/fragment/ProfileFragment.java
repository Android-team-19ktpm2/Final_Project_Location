package com.example.map_originnal.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.map_originnal.R;
import com.example.map_originnal.activity.MainActivity;
import com.example.map_originnal.activity.StartActivity;
/*import com.example.map_originnal.activity.UpdateProfileActivity;*/
import com.example.map_originnal.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileFragment extends Fragment {
    MainActivity main;
    private static final int PICK_IMAGE_REQUEST = 1;

    //Display widgets
    Uri mUri;
    ImageView avatar;
    TextView tv_email, tv_dob, tv_full_name,tv_phone, tv_ocp;

    //Basic buttons
    Button btn_logout, btn_update;
    ImageButton btn_back;


    //pencil buttons
    ImageButton btn_pencil_full_name, btn_pencil_dob, btn_pencil_ocp, btn_pencil_email, btn_pencil_phone;


    //check buttons
    ImageButton btn_check_full_name, btn_check_dob, btn_check_ocp, btn_check_email, btn_check_phone;


    //Edit texts
    EditText edt_full_name, edt_dob, edt_ocp, edt_email, edt_phone;

    //Firebase
    DatabaseReference reference;
    UploadTask upload;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;

    //Animations
    ProgressDialog progressDialog;
    AlphaAnimation appearAnimation;
    AlphaAnimation disappearAnimation;

    User current_user;

    public ProfileFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout layout_user = (LinearLayout) inflater.inflate(R.layout.fragment_profile, container, false);

        main = (MainActivity)getActivity();

        //authorized check
        AuthorizedCheck();

        //basic widgets
        widgetInit(layout_user);

        //buttons
        btnInit();

        btn_check_full_name.setVisibility(View.INVISIBLE);
        return layout_user;
    }

    private void widgetInit(LinearLayout layout_user)
    {
        tv_full_name = layout_user.findViewById(R.id.profile_tv_full_name);
        /*        tv_email = layout_user.findViewById(R.id.profile_tv_email);*/
        tv_phone = layout_user.findViewById(R.id.profile_tv_phone);
        tv_dob = layout_user.findViewById(R.id.profile_tv_dob);
        tv_ocp = layout_user.findViewById(R.id.profile_tv_ocp);

        btn_back = layout_user.findViewById(R.id.profile_btn_back);
        btn_logout = layout_user.findViewById(R.id.profile_btn_log_out);
        btn_update = layout_user.findViewById(R.id.profile_btn_update);

        //pencil buttons
        btn_pencil_full_name = layout_user.findViewById(R.id.profile_btn_pencil_full_name);
        btn_pencil_dob = layout_user.findViewById(R.id.profile_btn_pencil_dob);
        btn_pencil_ocp = layout_user.findViewById(R.id.profile_btn_pencil_ocp);
        /*        btn_pencil_email = layout_user.findViewById(R.id.profile_btn_pencil_email);*/
        btn_pencil_phone = layout_user.findViewById(R.id.profile_btn_pencil_phone);

        //check buttons
        btn_check_full_name = layout_user.findViewById(R.id.profile_btn_check_full_name);
        btn_check_dob = layout_user.findViewById(R.id.profile_btn_check_dob);
        btn_check_ocp = layout_user.findViewById(R.id.profile_btn_check_ocp);
        /*        btn_check_email = layout_user.findViewById(R.id.profile_btn_check_email);*/
        btn_check_phone = layout_user.findViewById(R.id.profile_btn_check_phone);

        //edit texts
        edt_full_name = layout_user.findViewById(R.id.profile_edt_full_name);
        edt_dob = layout_user.findViewById(R.id.profile_edt_dob);
        edt_ocp = layout_user.findViewById(R.id.profile_edt_ocp);
        /*        edt_email = layout_user.findViewById(R.id.profile_edt_email);*/
        edt_phone = layout_user.findViewById(R.id.profile_edt_phone);

        avatar = layout_user.findViewById(R.id.profile_image_avatar);
    }

    private void AuthorizedCheck()
    {
        // firebase
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null){
            String default_avatar = "data:image/png;base64,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";

            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            storageReference = FirebaseStorage.getInstance().getReference(firebaseUser.getUid()+".jpg");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progressDialog = new ProgressDialog(main);
                    progressDialog.setTitle("Loading...");
                    progressDialog.show();

                    current_user = snapshot.getValue(User.class);
                    tv_full_name.setText(current_user.getFull_name());
                    tv_phone.setText(current_user.getPhone());
                    tv_ocp.setText(current_user.getOcp());

                    if (!current_user.getDob().equals("..."))
                        tv_dob.setText(current_user.getDob());

                    if (current_user.getAvatar().equals("default"))
                        Glide.with(getActivity()).load(default_avatar).into(avatar);
                    else
                        Glide.with(getActivity()).load(snapshot.child("avatar").getValue().toString()).into(avatar);

                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
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
    }

    private void btnInit() {
/*        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });*/

        edt_dob.setInputType(InputType.TYPE_NULL);
        edt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDiaglog(edt_dob);
            }
        });


        //firebase storage
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        //edit full name
        btn_pencil_full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //invisible
                tv_full_name.setVisibility(View.INVISIBLE);;
                btn_pencil_full_name.setVisibility(View.INVISIBLE);;

                //visible
                edt_full_name.setVisibility(View.VISIBLE);
                btn_check_full_name.setVisibility(View.VISIBLE);


                //set current value
                edt_full_name.setText(tv_full_name.getText().toString());
            }
        });

        btn_check_full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_val = edt_full_name.getText().toString();

                if (TextUtils.isEmpty(new_val))
                {
                    edt_full_name.setError("Full name is required");
                    edt_full_name.requestFocus();
                    return;
                }

                //invisible
                edt_full_name.setVisibility(View.INVISIBLE);;
                btn_check_full_name.setVisibility(View.INVISIBLE);;

                //visible
                tv_full_name.setVisibility(View.VISIBLE);
                btn_pencil_full_name.setVisibility(View.VISIBLE);

                //update
                update("full_name", new_val);
            }
        });

        //edit dob
        btn_pencil_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //invisible
                tv_dob.setVisibility(View.INVISIBLE);;
                btn_pencil_dob.setVisibility(View.INVISIBLE);;

                //visible
                edt_dob.setVisibility(View.VISIBLE);
                btn_check_dob.setVisibility(View.VISIBLE);

                //set current value
                edt_dob.setText(tv_dob.getText().toString());
            }
        });

        btn_check_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_val = edt_dob.getText().toString();

                //invisible
                edt_dob.setVisibility(View.INVISIBLE);;
                btn_check_dob.setVisibility(View.INVISIBLE);;

                //visible
                tv_dob.setVisibility(View.VISIBLE);
                btn_pencil_dob.setVisibility(View.VISIBLE);

                //update
                if (!TextUtils.isEmpty(new_val))
                    update("dob", new_val);
            }
        });

        //edit occupation
        btn_pencil_ocp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //invisible
                tv_ocp.setVisibility(View.INVISIBLE);;
                btn_pencil_ocp.setVisibility(View.INVISIBLE);;

                //visible
                edt_ocp.setVisibility(View.VISIBLE);
                btn_check_ocp.setVisibility(View.VISIBLE);

                //set current value
                edt_ocp.setText(tv_ocp.getText().toString());
            }
        });

        btn_check_ocp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_val = edt_ocp.getText().toString();

                //invisible
                edt_ocp.setVisibility(View.INVISIBLE);;
                btn_check_ocp.setVisibility(View.INVISIBLE);;

                //visible
                tv_ocp.setVisibility(View.VISIBLE);
                btn_pencil_ocp.setVisibility(View.VISIBLE);

                //update
                if (!TextUtils.isEmpty(new_val))
                    update("ocp", new_val);
            }
        });


        //edit phone
        btn_pencil_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //invisible
                tv_phone.setVisibility(View.INVISIBLE);;
                btn_pencil_phone.setVisibility(View.INVISIBLE);;

                //visible
                edt_phone.setVisibility(View.VISIBLE);
                btn_check_phone.setVisibility(View.VISIBLE);

                //set current value
                edt_phone.setText(tv_phone.getText().toString());
            }
        });

        btn_check_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_val = edt_phone.getText().toString();

                if (TextUtils.isEmpty(new_val))
                {
                    edt_phone.setError("Phone is required");
                    edt_phone.requestFocus();
                    return;
                }

                //invisible
                edt_phone.setVisibility(View.INVISIBLE);;
                btn_check_phone.setVisibility(View.INVISIBLE);;

                //visible
                tv_phone.setVisibility(View.VISIBLE);
                btn_pencil_phone.setVisibility(View.VISIBLE);

                //update
                update("phone", new_val);
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


    void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.getData() != null)
        {
            mUri = data.getData();
            uploadImage();
        }
    }

    void uploadImage()
    {
        progressDialog = new ProgressDialog(main);
        progressDialog.setTitle("Changing...");
        progressDialog.show();

        storageReference.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        reference.child("avatar").setValue(uri.toString());
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(main, "Successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(main, "Failed", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });


    }

    void showDateDiaglog(final EditText edt_dob)
    {
        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                edt_dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            }
        };

        // Create DatePickerDialog (Spinner Mode):
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                dateSetListener, 2022, 4, 29);

        // Show
        datePickerDialog.show();
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
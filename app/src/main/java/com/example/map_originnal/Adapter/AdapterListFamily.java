package com.example.map_originnal.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.map_originnal.R;
import com.example.map_originnal.activity.ChatActivity;
import com.example.map_originnal.activity.MainActivity;
import com.example.map_originnal.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

//
public class AdapterListFamily extends RecyclerView.Adapter<AdapterListFamily.ViewHolder>  {

    private static final String TAG = "AdapterListFamily";
    private ArrayList<User> users;
    private Activity context;


    TextView txtTenDetail,txtActiveDetail,txtDiaChiDetail;
    ImageButton imgChat,imgLocation;
    ImageView imgAvatarDetail,imgFriendStatus;

    String defaultAvatar = "https://res.cloudinary.com/imag/image/upload/v1651484190/apploction/download_r06nnc.jpg\n";

    public AdapterListFamily( Activity context, ArrayList<User> users) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_family,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (users.get(position).getAvatar().equals("default"))
            Glide.with(context).load(defaultAvatar).into(holder.user_avatar);
        else
            Glide.with(context).load(users.get(position).getAvatar()).into(holder.user_avatar);

        holder.txtTen.setText(users.get(position).getFull_name());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (getStatusUser(users.get(position).getOnline()).equals("Online")){
                holder.user_status.setBackgroundColor(R.color.colorGreen);
            }else {
                holder.user_status.setImageResource(R.color.light_gray);
            }
        }
        holder.ln_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.dialog_info_friend,
                        (LinearLayout) context.findViewById(R.id.info_container));

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

                txtDiaChiDetail = bottomSheetView.findViewById(R.id.txtDiaChiDetail);
                txtTenDetail = bottomSheetView.findViewById(R.id.txtTenDetail);
                txtActiveDetail = bottomSheetView.findViewById(R.id.txtActiveDetail);
                imgChat = bottomSheetView.findViewById(R.id.imgChat);
                imgLocation = bottomSheetView.findViewById(R.id.imgLocation);
                imgAvatarDetail = bottomSheetView.findViewById(R.id.imgAvatarDetail);
                imgFriendStatus = bottomSheetView.findViewById(R.id.friend_status);

                txtTenDetail.setText(users.get(position).getFull_name());
                txtDiaChiDetail.setText(getAddressUser(users.get(position).getLat_X(),users.get(position).getLong_Y()));

                txtActiveDetail.setText(getStatusUser(users.get(position).getOnline()));
                Glide.with(context).load(users.get(position).getAvatar()).into(imgAvatarDetail);
                imgChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("user",users.get(position));
                        context.startActivity(intent);
                    }
                });
                imgLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (users.get(position).getLat_X()!=null || users.get(position).getLong_Y()!=null){
                            LatLng loc = new LatLng(Double.valueOf(users.get(position).getLat_X()), Double.valueOf(users.get(position).getLong_Y()));
                            MainActivity main = (MainActivity) context;
                            main.onLocationFromAdapToMain("Adapter",loc);
                        }else{
                            Toast.makeText(context,"Not find location friend",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                if (getStatusUser(users.get(position).getOnline()).equals("Online")){
                    imgFriendStatus.setImageResource(R.drawable.user_status1);
                }else {
                    imgFriendStatus.setImageResource(R.drawable.user_status);
                }



            }
        });

    }

    private String getAddressUser(String lat_x, String long_y) {
        String Address="";

        if (lat_x == null || long_y == null){
            Address = "Not Address";
        }else {
            System.out.println(lat_x+long_y);
            Geocoder geocoder = new Geocoder(context);
            try {
                List<android.location.Address> a = geocoder.getFromLocation(Double.valueOf(lat_x),Double.valueOf(long_y),1);
                if (!a.isEmpty()){
                    Address = a.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return Address;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Circle
        ImageView user_avatar, user_status;
        TextView txtTen;
        ConstraintLayout ln_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_avatar = itemView.findViewById(R.id.user_avatar);
            user_status = itemView.findViewById(R.id.user_status);
            txtTen = itemView.findViewById(R.id.txtTen);
            ln_button = itemView.findViewById(R.id.ln_button);
        }
    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getStatusUser(String status) {
        if (status.split("////")[0].equals("False")) {
            if (!status.split("////")[1].equals("Undefine")) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date offline = null, now = null;
                try {
                    offline = formatter.parse(status.split("////")[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    now = formatter.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long time_diff = now.getTime() - offline.getTime();

                long day_diff = TimeUnit.MILLISECONDS.toDays(time_diff) % 365;
                if (day_diff == 0) {
                    long hour_diff = TimeUnit.MILLISECONDS.toHours(time_diff) % 24;
                    if (hour_diff == 0) {
                        long min_diff = TimeUnit.MILLISECONDS.toMinutes(time_diff) % 60;
                        if (min_diff == 0)
                            return "Online";
                        else
                            return "Active " + min_diff + " minutes ago";

                    } else
                        return "Active " + hour_diff + " hours ago";
                } else
                    return "Active " + day_diff + " days ago";

            } else
                return "";
        }
        return "Online";
    }


}

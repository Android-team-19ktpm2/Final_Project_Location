package com.example.map_originnal.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.map_originnal.R;
import com.example.map_originnal.activity.ChatActivity;
import com.example.map_originnal.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
    ImageButton imgChat;
    ImageView imgAvatarDetail;

    String urlDisplay ="data:image/png;base64,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";

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(urlDisplay).into(holder.imgAvartar);
        holder.txtTen.setText(users.get(position).getFirst_name() + users.get(position).getLast_name());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (getStatusUser(users.get(position).getOnline()).equals("Online")){
                holder.isActive.setBackgroundColor(Color.GREEN);
            }else {
                holder.isActive.setBackgroundColor(Color.RED);
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
                imgAvatarDetail = bottomSheetView.findViewById(R.id.imgAvatarDetail);


                txtTenDetail.setText(users.get(position).getFirst_name() + " "+ users.get(position).getLast_name());
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



            }
        });

    }

    private String getAddressUser(String lat_x, String long_y) {
        String Address="";

        if (lat_x.isEmpty() || long_y.isEmpty()){
            Address = "Not Address";
        }else {
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
        ImageView imgAvartar, isActive;
        TextView txtTen;
        LinearLayout ln_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvartar = itemView.findViewById(R.id.imgAvatar);
            isActive = itemView.findViewById(R.id.isActive);
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

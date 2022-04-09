package com.example.map_originnal.Adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.map_originnal.R;
import com.example.map_originnal.activity.MainActivity;
import com.example.map_originnal.model.User;

import java.util.ArrayList;

//
public class AdapterListFamily extends RecyclerView.Adapter<AdapterListFamily.ViewHolder>  {

    private static final String TAG = "AdapterListFamily";
    private ArrayList<User> users;
    private Activity context;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String urlDisplay = users.get(position).getAvatar();
        Glide.with(context).load(urlDisplay).into(holder.imgAvartar);
        holder.txtTen.setText(users.get(position).getFirst_name() + users.get(position).getLast_name());
        holder.ln_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                System.out.println(holder.txtTen.getText());
                ((MainActivity) context).onMsgFromFragToMain("List-Family","Back");
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Circle
        ImageView imgAvartar;
        TextView txtTen;
        LinearLayout ln_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvartar = itemView.findViewById(R.id.imgAvatar);
            txtTen = itemView.findViewById(R.id.txtTen);
            ln_button = itemView.findViewById(R.id.ln_button);
        }
    }


    @Override
    public int getItemCount() {
        return users.size();
    }


}

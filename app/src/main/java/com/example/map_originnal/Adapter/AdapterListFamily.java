package com.example.map_originnal.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.map_originnal.R;
import com.example.map_originnal.activity.MainActivity;
import com.example.map_originnal.model.MemberFamily;

import java.util.ArrayList;

//
public class AdapterListFamily extends RecyclerView.Adapter<AdapterListFamily.ViewHolder>  {

    private static final String TAG = "AdapterListFamily";
    private ArrayList<MemberFamily> memberFamily;
    private Activity context;

    public AdapterListFamily( Activity context, ArrayList<MemberFamily> memberFamily) {
        this.memberFamily = memberFamily;
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
        Log.d(TAG, "onBindViewHolder: called");

        String urldisplay = memberFamily.get(position).getImg();
        Glide.with(context).load(urldisplay).into(holder.imgAvartar);
        holder.txtTen.setText(memberFamily.get(position).getTen());
        holder.ln_button.setOnClickListener(new View.OnClickListener() {
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
        return memberFamily.size();
    }


}

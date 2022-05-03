package com.example.map_originnal.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.map_originnal.R;
import com.example.map_originnal.model.User;

import java.util.ArrayList;


public class AdapterReqFriend extends ArrayAdapter {

    Context context;
    int resource;
    ArrayList<User> objects;

    public AdapterReqFriend(@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource =resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View v = inflater.inflate(resource,null);

        TextView txtUsername = v.findViewById(R.id.txtUsername);
        TextView txtEmail = v.findViewById(R.id.txtEmail);

        txtEmail.setText(objects.get(position).getEmail());
        txtUsername.setText(objects.get(position).getFull_name());

        return v;
    }
}

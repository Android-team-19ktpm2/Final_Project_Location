package com.example.map_originnal;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter {

    private Activity context;
    private int resource ;
    private ArrayList<MemberFamily> objects;


    public Adapter(@NonNull Activity context, int resource, @NonNull ArrayList<MemberFamily>  objects) {
        super(context, resource, objects);


        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource,null);
        TextView txtMa = row.findViewById(R.id.txtMa);
        ImageView img = row.findViewById(R.id.img);

      //  System.out.println(objects.get(position).getMa());
        txtMa.setText(objects.get(position).getTen());
        img.setImageResource(R.drawable.contact);

        return row;
    }
}

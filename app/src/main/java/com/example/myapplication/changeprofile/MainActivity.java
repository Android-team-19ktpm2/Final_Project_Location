package com.example.myapplication.changeprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonShow = findViewById(R.id.buttonShow);
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        MainActivity.this,R.style.BottomSheetDialogTheme
                );
                View bottomSheetView  = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.layout_bottom_sheet,
                                (LinearLayout) findViewById(R.id.bottomSheetContainer)
                );
               bottomSheetView.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Toast.makeText(MainActivity.this,"Share",Toast.LENGTH_SHORT).show();
                       bottomSheetDialog.dismiss();
                   }
               });
               bottomSheetDialog.setContentView(bottomSheetView);
               bottomSheetDialog.show();
            }
        });
    }
}
package com.example.alwaydisplaymenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    Button btn_main_menu,btn_view_mode;
    View main_menu,view_mode;
    Boolean show_main_menu=false;
    Boolean show_view_mode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_main_menu=findViewById(R.id.btn_main_menu);
        btn_view_mode=findViewById(R.id.btn_view_mode);

        main_menu=findViewById(R.id.main_menu);
        view_mode=findViewById(R.id.view_mode);

        main_menu.animate().alpha(0.0f);
        view_mode.animate().alpha(0.0f);

        btn_main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show_main_menu) {
                    btn_main_menu.animate().alpha(1f).setDuration(250);
                    main_menu.animate().alpha(0.0f).setDuration(250);
                    show_main_menu=false;
                }
                else {
                    btn_main_menu.animate().alpha(0.5f).setDuration(250);
                    main_menu.animate().alpha(1f).setDuration(250);
                    show_main_menu=true;
                }
            }
        });


        btn_view_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show_view_mode) {
                    btn_view_mode.animate().alpha(1f).setDuration(250);
                    view_mode.animate().alpha(0.0f).setDuration(250);
                    show_view_mode=false;
                }
                else {
                    btn_view_mode.animate().alpha(0.5f).setDuration(250);
                    view_mode.animate().alpha(1.0f).setDuration(250);
                    show_view_mode=true;
                }
            }
        });

    }
}
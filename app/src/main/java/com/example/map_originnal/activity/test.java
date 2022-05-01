package com.example.map_originnal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.map_originnal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class test extends AppCompatActivity {

    //Floating menu widgets
    private FloatingActionButton btn_extend, btn_hide_map, btn_search, btn_family, btn_view_mode;
    private TextView tv_search, tv_family, tv_view_mode;

    //Floating menu animation
    private Animation fl_menu_extend, fl_menu_close;

    //Floating men flag
    private boolean isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //Button
        btn_extend = findViewById(R.id.fl_menu_btn_extend);
        btn_search = findViewById(R.id.fl_menu_btn_search);
        btn_family = findViewById(R.id.fl_menu_btn_family);
        btn_view_mode = findViewById(R.id.fl_menu_btn_view_mode);


        //Text
        tv_search = findViewById(R.id.fl_menu_textview_search);
        tv_family = findViewById(R.id.fl_menu_textview_family);
        tv_view_mode = findViewById(R.id.fl_menu_textview_view_mode);


        //Animations
        fabAnimationInit();


        //Floating menu
        fabInit();

        isOpen = false;
    }

    void fabAnimationInit()
    {
        fl_menu_extend = AnimationUtils.loadAnimation(this, R.anim.fl_menu_extend);
        fl_menu_close = AnimationUtils.loadAnimation(this, R.anim.fl_menu_close);
    }

    void fabInit()
    {
        btn_extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isOpen)
                {
                    btn_search.setAnimation(fl_menu_close);
                    btn_family.setAnimation(fl_menu_close);
                    btn_view_mode.setAnimation(fl_menu_close);

                    tv_search.setVisibility(View.INVISIBLE);
                    tv_family.setVisibility(View.INVISIBLE);
                    tv_view_mode.setVisibility(View.INVISIBLE);

                    isOpen = false;
                }
                else
                {
                    btn_search.setAnimation(fl_menu_extend);
                    btn_family.setAnimation(fl_menu_extend);
                    btn_view_mode.setAnimation(fl_menu_extend);

                    tv_search.setVisibility(View.VISIBLE);
                    tv_family.setVisibility(View.VISIBLE);
                    tv_view_mode.setVisibility(View.VISIBLE);

                    isOpen = true;
                }

                //Restart animation
                btn_family.getAnimation().start();
                btn_search.getAnimation().start();

            }
        });

        btn_hide_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
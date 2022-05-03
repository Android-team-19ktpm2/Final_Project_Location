package com.example.map_originnal.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.map_originnal.Adapter.ChatAdapter;
import com.example.map_originnal.GPSTracker;
import com.example.map_originnal.R;
import com.example.map_originnal.model.Message;
import com.example.map_originnal.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {
    ImageButton btn_back,btn_share_location,btn_send;

    ImageView img_avatar;

    TextView tv_username,tv_status;

    EditText edt_input_msg;

    Intent intent;
    ListView listview_message;
    ChatAdapter adapter;

    FirebaseAuth auth;
    FirebaseUser current_user;

    DatabaseReference msgReference;
    DatabaseReference userReference;

    ValueEventListener msgListener;
    ValueEventListener userListener;

    ExecutorService executorService;
    Future longRunningTaskFuture;

    User enemy_user;
    GPSTracker mGPS = null;
    ArrayList<Message> messages;
    String default_avatar ="data:image/png;base64,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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mGPS = new GPSTracker(this);
        img_avatar =findViewById(R.id.chat_circle_main_avatar);

        tv_username =findViewById(R.id.chat_tv_username);
        tv_status = findViewById(R.id.chat_tv_status);

        edt_input_msg = findViewById(R.id.chat_edt_input);

        btn_back=findViewById(R.id.chat_btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_share_location = findViewById(R.id.chat_btn_share_location);
        btn_share_location.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                sendCurrentLocation();
            }
        });

        btn_send = findViewById(R.id.chat_btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edt_input_msg.getText()))
                    sendMessage();
            }
        });


        listview_message =findViewById(R.id.chat_listview_chat);
        listview_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        auth = FirebaseAuth.getInstance();

        intent=getIntent();
        enemy_user= (User) intent.getSerializableExtra("user");
        current_user=auth.getCurrentUser();

        msgReference = FirebaseDatabase.getInstance().getReference("Users").child(enemy_user.getId());
        msgReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                tv_username.setText(user.getFull_name());

                if (user.getAvatar().equals("default"))
                {
                    /*img_avatar.setImageResource(R.mipmap.ic_launcher_round);*/
                    Glide.with(ChatActivity.this).load(default_avatar).into(img_avatar);
                }
                else
                    Glide.with(ChatActivity.this).load(default_avatar).into(img_avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        executorService = Executors.newSingleThreadExecutor();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    userReference = FirebaseDatabase.getInstance().getReference("Users/"+enemy_user.getId());
                    userListener=userReference.addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            enemy_user = snapshot.getValue(User.class);
                            tv_status.setText(getStatusUser(enemy_user.getOnline()));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    synchronized (this) {
                        try {
                            wait(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        longRunningTaskFuture = executorService.submit(runnable);

        messages = new ArrayList<Message>();
        loadMessages();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMessage()
    {
        current_user= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,String> message_data = new HashMap<>();
        message_data.put("src",current_user.getUid());
        message_data.put("dst",enemy_user.getId());
        message_data.put("content",edt_input_msg.getText().toString());
        String sendAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        message_data.put("sendAt",sendAt);

        reference.child("Messages").push().setValue(message_data);
        edt_input_msg.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendCurrentLocation()
    {
        current_user= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,String> message_data = new HashMap<>();
        message_data.put("src",current_user.getUid());
        message_data.put("dst",enemy_user.getId());
        message_data.put("content",edt_input_msg.getText().toString());


        if (mGPS!=null){
            if(mGPS.canGetLocation() ){
                mGPS.getLocation();
                message_data.put("location",mGPS.getLatitude() + " " + mGPS.getLongitude());
            }else{
                System.out.println("Unable");
            }
        }



        String sendAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        message_data.put("sendAt",sendAt);

        reference.child("Messages").push().setValue(message_data);
        edt_input_msg.setText("");

    }

    public void loadCurrentLocation()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Location");
        System.out.println(reference);
    }

    public void loadMessages()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Messages");
        msgListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null)
                        if (message.getSrc().equals(current_user.getUid()) && message.getDst().equals(enemy_user.getId()) || message.getSrc().equals(enemy_user.getId()) && message.getDst().equals(current_user.getUid()))
                            messages.add(message);
                }
                adapter =new ChatAdapter(ChatActivity.this,R.layout.adapter_custom_left_chat,messages,current_user.getUid(),enemy_user.getId());
                listview_message.setAdapter(adapter);
                listview_message.setSelection(adapter.getCount() - 1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    @Override
    protected void onStop() {
        super.onStop();
        longRunningTaskFuture.cancel(true);
        userReference.removeEventListener(userListener);
        msgReference.removeEventListener(msgListener);
    }
}
package com.example.myapplication.changeprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class historyLog extends Activity {
    TextView txtMsg;
    String[] items = {"Data-0", "Data-1", "Data-2", "Data-3", "Data-4", "Data-5", "Data-6", "Data-7" };


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_log);

        txtMsg = (TextView) findViewById(R.id.txtMsg);
        Button buttonHide = findViewById(R.id.buttonShow);
        buttonHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                bottomSheetDialog.show();
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(historyLog.this);
                builderSingle.setIcon(R.drawable.breathtaking);
                builderSingle.setTitle("Lịch sử");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(historyLog.this, android.R.layout.select_dialog_item);
                arrayAdapter.add("Đường chưa đặt tên, Xuân Thạnh, Thống Nhất, Đồng Nai, Việt Nam");
                arrayAdapter.add("Đường chùa Tịnh Quang Tổ 10, ấp Ngô Quyền, Thị trấn Bầu Hàm 2, Thống Nhất, Đồng Nai, Việt Nam");
                arrayAdapter.add("Quốc lộ 1A, xã Bàu Hàm 2, huyện Thống Nhất, Đồng Nai, Việt Nam");
                arrayAdapter.add("Đường Lê Lợi, phường Bến Thành, quận 1, TP HCM");
                arrayAdapter.add("135 Nam Kỳ Khởi Nghĩa, Bến Nghé, Quận 1, TP HCM");
                arrayAdapter.add("01 Công Xã Paris, Bến Nghé, Quận 1, TP HCM");
                arrayAdapter.add("2 Nguyễn Bỉnh Khiêm, Quận 1, TP HCM");
                arrayAdapter.add("TL15, Phú Hiệp, Củ Chi, TP HCM");
                arrayAdapter.add("Đường Nguyễn Huệ, quận 1, TP HCM");
                arrayAdapter.add("03 Nguyễn Bỉnh Khiêm, Bến Nghé, Quận 1, TP HCM");
                arrayAdapter.add("02 Khu Him Lam, quận 7, TP HCM");
                arrayAdapter.add("19-25 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP HCM");
                arrayAdapter.add("3 Hòa Bình, phường 3, quận 11, TP HCM");
                arrayAdapter.add("Số 125 Công xã Paris, Bến Nghé, Quận 1, TPHCM");
                arrayAdapter.add("720A Điện Biên Phủ, Quận Bình Thạnh, TP HCM");
                arrayAdapter.add("Số 7 đường Công Trường Lam Sơn, phường Bến Nghé, Quận 1, TP HCM");
                arrayAdapter.add("Số 202 đường Võ Thị Sáu, phường 7, Quận 3, thành phố Hồ Chí Minh");
                arrayAdapter.add("Số 1 Nguyễn Tất Thành, Phường 12, Quận 4, Thành phố Hồ Chí Minh");
                arrayAdapter.add("120 Xa lộ Hà Nội, phường Tân Phú, Quận 9, thành phố Hồ Chí Minh");
                arrayAdapter.add("Số 02 – 04 đường số 9, KDC Him Lam, phường Tân Hưng, Quận 7, thành phố Hồ Chí Minh");
                arrayAdapter.add("1147 Bình Quới, phường 28, Quận Bình Thạnh, thành phố Hồ Chí Minh");
                arrayAdapter.add("Khu du lịch 30/4, đường Thạnh Thới, Long Hà, Cần Giờ, thành phố Hồ Chí Minh");
                arrayAdapter.add("81 Nguyễn Xiển, Long Bình, Quận 9, thành phố Hồ Chí Minh");
                arrayAdapter.add("Bùi Viện – Phạm Ngũ Lão – Đề Thám, Quận 1, thành phố Hồ chí Minh");



                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(historyLog.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");

                        builderInner.show();
                    }
                });
                builderSingle.show();
            }
        });
    }

}
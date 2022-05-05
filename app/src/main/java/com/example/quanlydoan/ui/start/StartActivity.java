package com.example.quanlydoan.ui.start;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.PrefsHelper;
import com.example.quanlydoan.ui.AppConstants;
import com.example.quanlydoan.ui.BaseActivity;

import java.util.List;
import java.util.Locale;

public class StartActivity extends BaseActivity {
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setControl();
        setEvent();

    }

    private void setEvent() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Kiểm tra nếu người dùng đã đăng nhập rồi thì mở activity home còn không thì mở activity login
                if (PrefsHelper.getInstance(getApplicationContext()).getCurrentUser() != null) {
//                    Đoạn này dùng để chuyển từ StartActivity sang MenuActivity
                    Intent intent = new Intent(StartActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setControl() {
        btnStart = findViewById(R.id.btnStart);
    }

}
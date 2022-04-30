package com.example.quanlydoan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.quanlydoan.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        setContentView(R.layout.activity_start);
    }
}
package com.example.quanlydoan.ui.foodinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.quanlydoan.R;

public class FoodInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        setControl();
        setEvent();
    }

    private void setControl() {
    }

    private void setEvent() {
    }
}
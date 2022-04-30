package com.example.quanlydoan.ui.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quanlydoan.R;
import com.example.quanlydoan.ui.home.HomeActivity;

public class StartActivity extends AppCompatActivity {
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        setContentView(R.layout.activity_start);
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        btnStart = findViewById(R.id.btnStart);
    }
}
package com.example.quanlydoan.ui.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.PrefsHelper;
import com.example.quanlydoan.ui.BaseActivity;
import com.example.quanlydoan.ui.home.HomeActivity;

public class StartActivity extends BaseActivity {
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
                if(PrefsHelper.getInstance(getApplicationContext()).getCurrentUser() != null){
                    Intent intent = new Intent(StartActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void setControl() {
        btnStart = findViewById(R.id.btnStart);
    }
}
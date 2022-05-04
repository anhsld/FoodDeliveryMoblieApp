package com.example.quanlydoan.ui.start;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.PrefsHelper;
import com.example.quanlydoan.data.model.Order;
import com.example.quanlydoan.data.model.User;
import com.example.quanlydoan.ui.AppConstants;
import com.example.quanlydoan.ui.BaseActivity;
import com.example.quanlydoan.ui.register.RegisterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity {
    Button btnLogin;
    EditText editTextLoginUsername, editTextLoginPassword;
    TextView btnLoginToRegister;

    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setControl();
        setEvent();
    }

    private void setControl() {
        btnLogin = findViewById(R.id.btnLogin);
        editTextLoginUsername = findViewById(R.id.editTextLoginUsername);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        btnLoginToRegister = findViewById(R.id.btnLoginToRegister);
    }

    private void setEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()){
                    login(String.valueOf(editTextLoginUsername.getText()),
                            String.valueOf(editTextLoginPassword.getText()));
                }
                else{
                    showMessage("Cannot empty!");
                }
            }
        });

        btnLoginToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkInput() {
        if (editTextLoginUsername.getText().toString().equals("")) return false;
        if (editTextLoginPassword.getText().toString().equals("")) return false;
        return true;
    }

    private void login(String username, String password) {
        showLoading();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference(AppConstants.USER_REF).child(username);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getPassword().equals(password)) {
                        Log.e(TAG, user.getEmail());

//                        Lưu thông tin người dùng vào prefsHelper
                        PrefsHelper prefsHelper = PrefsHelper.getInstance(getApplicationContext());
                        prefsHelper.setCurrentUser(user);
                        Order order = new Order();
                        prefsHelper.setCurrentCart(order);

                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showMessage("Wrong password");
                    }
                } else {
                    showPopupMessage("Login failed!", R.raw.err);
                }
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showPopupMessage("Login failed!", R.raw.err);
                hideLoading();
            }
        });
    }
}
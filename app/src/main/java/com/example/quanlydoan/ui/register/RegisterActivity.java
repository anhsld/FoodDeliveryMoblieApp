package com.example.quanlydoan.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextRegisterFullname, editTextRegisterUsername, editTextRegisterPasswordRetype, editTextRegisterPassword;
    TextView btnRegister;


    private final String TAG = "Tam";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        setControl();
        setEvent();
    }

    private void setControl() {
        editTextRegisterFullname = findViewById(R.id.editTextRegisterFullname);
        editTextRegisterUsername = findViewById(R.id.editTextRegisterUsername);
        editTextRegisterPasswordRetype = findViewById(R.id.editTextRegisterPasswordRetype);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void notice(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    private boolean checkInput() {
        if (editTextRegisterFullname.getText().toString().equals("") || editTextRegisterUsername.getText().toString().equals("") || editTextRegisterPasswordRetype.getText().toString().equals("") || editTextRegisterPassword.getText().toString().equals("")) {
            notice("Thông tin không được trống");
            return false;
        }
        if (!editTextRegisterPassword.getText().toString().equals(editTextRegisterPasswordRetype.getText().toString())){
            notice("Mật khẩu nhập lại không trùng khớp");
            return false;
        }

        return true;
    }

    private void setEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    checkUserExisted(editTextRegisterUsername.getText().toString());
                }
            }
        });
    }

    public void register(String fullname, String username, String password) {
        Uri file = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getResources().getResourcePackageName(R.drawable.customer)
                + '/' + getResources().getResourceTypeName(R.drawable.customer)
                + '/' + getResources().getResourceEntryName(R.drawable.customer));
        User user = new User(fullname, username, password, "", "", "", "");


        //upload file to firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference ref = storageRef.child("avatar/" + user.getUsername());
        UploadTask uploadTask = ref.putFile(file);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //get url image
                Uri downloadUri = task.getResult();
                user.setAvatar(downloadUri.toString());
                user.setUserId(user.getUsername());

                //Now insert user
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = database.getReference("user").child(user.getUsername());
                usersRef.setValue(user);
                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user1 = dataSnapshot.getValue(User.class);
                        notice("Đăng ký thành công");
                        Log.e(TAG, "Value is: " + user1.getFullName());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                        notice("Đã có lỗi khi đăng ký");
                    }
                });
            } else {
            }
        });
    }

    public void checkUserExisted(String username) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("user").child(username);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    notice("Người dùng đã tồn tại");
                    Log.e(TAG, user.getEmail());
                } else {
                    register(
                            editTextRegisterFullname.getText().toString(),
                            editTextRegisterUsername.getText().toString(),
                            editTextRegisterPassword.getText().toString()
                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //ignored
            }
        });
    }
}
package com.example.quanlydoan.ui.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.PrefsHelper;
import com.example.quanlydoan.data.model.User;
import com.example.quanlydoan.ui.AppConstants;
import com.example.quanlydoan.ui.BaseActivity;
import com.example.quanlydoan.ui.start.LoginActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileActivity extends BaseActivity {
    TextView btnProfileChooseAvatar, btnProfileLogout, btnProfileUpdate;
    ShapeableImageView imgProfileAvatar;
    EditText editTextProfileFullname, editTextProfilePhone, editTextProfileEmail, editTextProfileAddress, editTextProfilePassword, editTextProfileRetypePassword;

    private final String TAG = "ProfileActivity";

    private User user;
    private boolean chonHinh = false;
    Uri profileImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setControl();
        fillData();
        setEvent();
    }

    private void setEvent() {
        btnProfileChooseAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, AppConstants.REQUEST_CODE_FOLDER);
        });

        btnProfileLogout.setOnClickListener(view -> {
            PrefsHelper.getInstance(getApplicationContext()).removeCurrentUser();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        btnProfileUpdate.setOnClickListener(view -> {
            if(checkInput())updateProfile();
        });
    }

    private void setControl() {
        btnProfileChooseAvatar = findViewById(R.id.btnProfileChooseAvatar);
        imgProfileAvatar = findViewById(R.id.imgProfileAvatar);
        btnProfileLogout = findViewById(R.id.btnProfileLogout);
        editTextProfileFullname = findViewById(R.id.editTextProfileFullname);
        editTextProfilePhone = findViewById(R.id.editTextProfilePhone);
        editTextProfileEmail = findViewById(R.id.editTextProfileEmail);
        editTextProfileAddress = findViewById(R.id.editTextProfileAddress);
        editTextProfilePassword = findViewById(R.id.editTextProfilePassword);
        editTextProfileRetypePassword = findViewById(R.id.editTextProfileRetypePassword);
        btnProfileUpdate = findViewById(R.id.btnProfileUpdate);
    }

    private void fillData() {
        user = PrefsHelper.getInstance(getApplicationContext()).getCurrentUser();
        Picasso.get().load(user.getAvatar()).into(imgProfileAvatar);
        editTextProfileFullname.setText(user.getFullName());
        editTextProfilePhone.setText(user.getPhone());
        editTextProfileEmail.setText(user.getEmail());
        editTextProfileAddress.setText(user.getAddress());
        editTextProfilePassword.setText(user.getPassword());
        editTextProfileRetypePassword.setText(user.getPassword());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //lấy hình từ folder
        if (requestCode == AppConstants.REQUEST_CODE_FOLDER) {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                profileImgUri = data.getData();
                chonHinh = true;
                Picasso.get().load(uri).into(imgProfileAvatar);
            }
        }
    }

    public void updateProfile() {
        user.setFullName(editTextProfileFullname.getText().toString());
        user.setPhone(editTextProfilePhone.getText().toString());
        user.setEmail(editTextProfileEmail.getText().toString());
        user.setAddress(editTextProfileAddress.getText().toString());
        user.setPassword(editTextProfilePassword.getText().toString());

        //upload file to firebase storage
        if (chonHinh) {
            uploadImage(user, profileImgUri);
        } else {
            updateUser(user);
        }

    }

    private boolean checkInput() {
        if (editTextProfileFullname.getText().toString().trim().equals("")) {
            showMessage("Fullname required");
            return false;
        }
        String regexStr = "^0[0-9]{9}$";
        if (!editTextProfilePhone.getText().toString().trim().equals("") &&
                !editTextProfilePhone.getText().toString().matches(regexStr)) {
            showMessage("Phone number is incorrect format!");
            return false;
        }

        regexStr = "^[A-Za-z0-9._%+-]+@[a-z]+\\.[a-z]{2,6}$";
        if (!editTextProfileEmail.getText().toString().trim().equals("") &&
                !editTextProfileEmail.getText().toString().matches(regexStr)) {
            showMessage("Email is incorrect format!");
            return false;
        }

//        Thêm kiểm tra mật khẩu trống và 2 mật khẩu khớp nhau không
        if(editTextProfilePassword.getText().toString().trim().equals("")
                || editTextProfileRetypePassword.getText().toString().trim().equals("")){
            showMessage("Password cannot empty");
            return false;
        }

        if(editTextProfilePassword.getText().toString().trim().equals(editTextProfileRetypePassword.getText().toString().trim())){
            showMessage("Retype password not correct");
            return false;
        }

        return true;
    }

    private void uploadImage(User user, Uri fileUri) {
        showLoading();

        StorageReference ref = FirebaseStorage.getInstance().getReference()
                .child("avatar/" + user.getUsername());
        UploadTask uploadTask = ref.putFile(fileUri);
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

                //Now insert user
                updateUser(user);
            }
        });
    }

    private void updateUser(User user) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(AppConstants.USER_REF).child(user.getUsername());
        ref.setValue(user);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                Log.e(TAG, "Value is: " + user1.getFullName());
                PrefsHelper.getInstance(getApplicationContext()).setCurrentUser(user);
                showPopupMessage("Update profile successful!", R.raw.success);
                hideLoading();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
                showMessage("Đã có lỗi khi cập nhật thông tin cá nhân");
                hideLoading();
            }
        });
    }

}
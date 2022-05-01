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

public class ProfileActivity extends AppCompatActivity {
    TextView btnProfileChooseAvatar, btnProfileLogout, btnProfileUpdate;
    ShapeableImageView imgProfileAvatar;
    EditText editTextProfileFullname, editTextProfilePhone, editTextProfileEmail, editTextProfileAddress, editTextProfilePassword, editTextProfileRetypePassword;


    final int REQUEST_CODE_FOLDER = 123;
    boolean chonHinh = false;
    final String TAG = "TEST";
    Uri profileImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        setControl();
        fillData();
        setEvent();
    }

    private void setEvent() {
        btnProfileChooseAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });

        btnProfileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefsHelper.getInstance(getApplicationContext()).removeCurrentUser();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
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
        Picasso.get().load(PrefsHelper.getInstance(getApplicationContext()).getCurrentUser().getAvatar()).into(imgProfileAvatar);
        editTextProfileFullname.setText(PrefsHelper.getInstance(getApplicationContext()).getCurrentUser().getFullName());
        editTextProfilePhone.setText(PrefsHelper.getInstance(getApplicationContext()).getCurrentUser().getPhone());
        editTextProfileEmail.setText(PrefsHelper.getInstance(getApplicationContext()).getCurrentUser().getEmail());
        editTextProfileAddress.setText(PrefsHelper.getInstance(getApplicationContext()).getCurrentUser().getAddress());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //lấy hình từ folder
        if (requestCode == REQUEST_CODE_FOLDER) {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                try {
                    profileImgUri = data.getData();
                    chonHinh = true;
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imgProfileAvatar.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void notice(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void updateProfile() {
        String fullname = editTextProfileFullname.getText().toString();
        String phone = editTextProfilePhone.getText().toString();
        String email = editTextProfileEmail.getText().toString();
        String address = editTextProfileAddress.getText().toString();
        String password = editTextProfilePassword.getText().toString();
        User user = PrefsHelper.getInstance(getApplicationContext()).getCurrentUser();
        user.setFullName(fullname);
        user.setPhone(phone);
        user.setEmail(email);
        user.setAddress(address);
        user.setPassword(password);

        //upload file to firebase storage
        if (chonHinh) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference ref = storageRef.child("avatar/" + user.getUsername());
            UploadTask uploadTask = ref.putFile(profileImgUri);
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
                    updateProfileNoPhoto(user);
                } else {
                }
            });
        } else {
            updateProfileNoPhoto(user);
        }

    }

    private void updateProfileNoPhoto(User user) {
        //Now insert user
        user.setUserId(user.getUsername());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("user").child(user.getUsername());
        usersRef.setValue(user);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                Log.e(TAG, "Value is: " + user1.getFullName());
                PrefsHelper.getInstance(getApplicationContext()).setCurrentUser(user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
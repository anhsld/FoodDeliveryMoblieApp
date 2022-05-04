package com.example.quanlydoan.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.model.User;
import com.example.quanlydoan.ui.AppConstants;
import com.example.quanlydoan.ui.BaseActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends BaseActivity {
    EditText editTextRegisterFullname, editTextRegisterUsername, editTextRegisterPasswordRetype, editTextRegisterPassword;
    TextView btnRegister;

    private final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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


    private boolean checkInput() {
        if (editTextRegisterFullname.getText().toString().trim().equals("")
                || editTextRegisterUsername.getText().toString().trim().equals("")
                || editTextRegisterPasswordRetype.getText().toString().trim().equals("")
                || editTextRegisterPassword.getText().toString().trim().equals("")) {
            showMessage("Info cannot empty");
            return false;
        }
        if (editTextRegisterUsername.getText().toString().contains(" ") ||
                editTextRegisterPassword.getText().toString().contains(" ") ||
                editTextRegisterPasswordRetype.getText().toString().contains(" ")) {
            showMessage("Username and password cannot have space");
            return false;
        }
        if (!editTextRegisterPassword.getText().toString().equals(editTextRegisterPasswordRetype.getText().toString())) {
            showMessage("Retype-password not incorrect");
            return false;
        }
        return true;
    }

    private void setEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    showLoading();
                    checkUserExisted(editTextRegisterUsername.getText().toString());
                }
            }
        });
    }

    private void checkUserExisted(String username) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance()
                .getReference(AppConstants.USER_REF).child(username);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hideLoading();
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    showMessage("This username has been registed");
                    Log.e(TAG, user.getEmail());
                } else {
                    register(editTextRegisterFullname.getText().toString(),
                            editTextRegisterUsername.getText().toString(),
                            editTextRegisterPassword.getText().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //ignored
            }
        });
    }

    public void register(String fullname, String username, String password) {
        Uri fileUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getResources().getResourcePackageName(R.drawable.customer)
                + '/' + getResources().getResourceTypeName(R.drawable.customer)
                + '/' + getResources().getResourceEntryName(R.drawable.customer));
        User user = new User(fullname, username, password, "", "", "", "");

        uploadImage(user, fileUri);
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
                insertUser(user);
            }
        });
    }

    private void insertUser(User user) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(AppConstants.USER_REF).child(user.getUsername());
        ref.setValue(user);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                hideLoading();
                showMessage("Register successful!!!");
                Log.e(TAG, "Value is: " + user1.getFullName());
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    finish();
                }, 1000);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
                showMessage("Đã có lỗi khi đăng ký");
                hideLoading();
            }
        });
    }

}
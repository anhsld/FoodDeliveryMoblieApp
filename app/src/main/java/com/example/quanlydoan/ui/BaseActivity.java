package com.example.quanlydoan.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydoan.R;
import com.example.quanlydoan.ui.messagedialog.MessageDialog;
import com.google.android.material.snackbar.Snackbar;


public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private int taskInProcess = 0;

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void showPopup(String message, int animation) {
        MessageDialog messageDialog = new MessageDialog(this);
        messageDialog.showMessage(message, animation);
    }
    
    public void showLoading() {
        hideLoading();
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

     
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }


    public void onError(int resId) {
        onError(getString(resId));
    }

    public void onError(String message) {
        if (message != null) {
            showSnackBar(message);
        } else {
            showSnackBar("Something wrong");
        }
    }

    public void showMessage(String message) {
        showSnackBar(message);
    }

    public void showMessage(int resId) {
        showMessage(getString(resId));
    }

    public void showPopupMessage(String message, int animation) {
        showPopup(message, animation);
    }

    public void showPopupMessage(int resId, int animation) {
        showPopupMessage(getString(resId), animation);
    }

    public void startMultiProcess() {
        if (++taskInProcess == 1) showLoading();
    }
    public void endMultiProcess() {
        if (--taskInProcess == 0) hideLoading();
    }
}
package com.example.quanlydoan.ui.messagedialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.*;


import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.example.quanlydoan.R;
import com.example.quanlydoan.ui.AppConstants;

public class MessageDialog extends Dialog {
    TextView txtMessage;
    LottieAnimationView animationView;

    public MessageDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
        txtMessage = findViewById(R.id.txt_message);
        animationView = findViewById(R.id.animationView);
    }

    protected void setup() {
        setContentView(R.layout.message_dialog);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().windowAnimations = R.style.ZoomAnimation;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void showMessage(String message, int animation) {
        show();
        txtMessage.setText(message);
        animationView.setAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            dismiss();
        }, AppConstants.DIALOG_POPUP_TIME);
    }
}
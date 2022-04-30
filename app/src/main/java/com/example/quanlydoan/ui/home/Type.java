package com.example.quanlydoan.ui.home;

import android.widget.ImageView;

import java.io.Serializable;

public class Type implements Serializable {
    ImageView img;
    String name;

    public Type() {
    }

    public Type(ImageView img, String name) {
        this.img = img;
        this.name = name;
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

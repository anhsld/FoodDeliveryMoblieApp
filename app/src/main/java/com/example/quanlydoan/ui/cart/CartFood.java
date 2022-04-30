package com.example.quanlydoan.ui.cart;

import java.io.Serializable;

public class CartFood implements Serializable {
    String name, image;
    double price;
    int amount;

    public CartFood() {
    }

    public CartFood(String name, String image, double price, int amount) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

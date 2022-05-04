package com.example.quanlydoan.data.model;

import java.io.Serializable;

public class CartFood implements Serializable {
    Food food;
    int amount;

    public CartFood() {
    }

    public CartFood(Food food, int amount) {
        this.food = food;
        this.amount = amount;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

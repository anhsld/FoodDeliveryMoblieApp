package com.example.quanlydoan.ui.home;

import java.io.Serializable;

public class Food implements Serializable {
    String image;
    String descripton;

    public Food(String image, String descripton, String name, int id, Double price) {
        this.image = image;
        this.descripton = descripton;
        this.name = name;
        this.id = id;
        this.price = price;
    }

    String name;
    int id;
    Double price;

    public Food() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescripton() {
        return descripton;
    }

    public void setDescripton(String descripton) {
        this.descripton = descripton;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

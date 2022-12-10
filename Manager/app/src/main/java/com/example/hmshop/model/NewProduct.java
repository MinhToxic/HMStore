package com.example.hmshop.model;

import java.io.Serializable;

public class NewProduct implements Serializable {
    int id;
    String nameproduct;
    String image;
    String price;
    String description;
    boolean Loai;

    public boolean getLoai() {
        return Loai;
    }

    public void setLoai(boolean loai) {
        Loai = loai;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

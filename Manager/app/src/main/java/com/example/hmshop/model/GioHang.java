package com.example.hmshop.model;

public class GioHang {
    int idsp;
    String nameProduct;
    long pricePrice;
    String imgimage;
    int soluong;

    public GioHang() {
    }

    public int getIdsp() {
        return idsp;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public long getPricePrice() {
        return pricePrice;
    }

    public void setPricePrice(long pricePrice) {
        this.pricePrice = pricePrice;
    }

    public String getImgimage() {
        return imgimage;
    }

    public void setImgimage(String imgimage) {
        this.imgimage = imgimage;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}

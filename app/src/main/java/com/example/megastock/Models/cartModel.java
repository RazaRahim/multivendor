package com.example.megastock.Models;

public class cartModel {
    String name,brand,type, price,quantity, description,url,prodkey,no;

    public cartModel() {
    }

    public cartModel(String name, String brand, String type, String price, String quantity, String description, String url, String prodkey, String no) {
        this.name = name;
        this.brand = brand;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.url = url;
        this.prodkey = prodkey;
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProdkey() {
        return prodkey;
    }

    public void setProdkey(String prodkey) {
        this.prodkey = prodkey;
    }
}

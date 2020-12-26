package com.example.megastock.Models;

public class getproductmodel {

    public getproductmodel(){


    }

    String brand,name,type,quantity, price, description,url;

    public getproductmodel(String brand, String name, String type,String quantity, String price, String description, String url) {
        this.brand = brand;
        this.name = name;
        this.type = type;
        this.quantity=quantity;
        this.price = price;
        this.description = description;
        this.url = url;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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
}

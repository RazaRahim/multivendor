package com.example.megastock.Models;

public class pendingordermodel {

    String brand,name,type,quantity,price, description,url,no,date,address,city,time;


    public pendingordermodel() {

    }

    public pendingordermodel(String brand, String name, String type, String quantity, String price, String description, String url, String no, String date, String address, String city, String time) {
        this.brand = brand;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.url = url;
        this.no = no;
        this.date = date;
        this.address = address;
        this.city = city;
        this.time = time;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


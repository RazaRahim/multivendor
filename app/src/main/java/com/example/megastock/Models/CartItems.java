package com.example.megastock.Models;

public class CartItems {
    private String Name, Price, Quantity, Id,url;
    public int total_price;

//    public int getTotal_price() {
//        return total_price;
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public CartItems() {
    }

//    public CartItems(String name, String price, String quantity, String id) {
//        Name = name;
//        Price = price;
//        Quantity = quantity;
//        Id = id;
//        total_price += Integer.parseInt(price)* Integer.parseInt(quantity);
//    }


    public CartItems(String name, String price, String quantity, String id, String url) {
        Name = name;
        Price = price;
        Quantity = quantity;
        Id = id;
        this.url = url;
    }
}

package com.example.megastock.Utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.megastock.Models.buyermodel;
import com.example.megastock.Models.productmodel;
import com.example.megastock.Models.sellermodel;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by AliAh on 20/02/2018.
 */

public class SharedPrefs {


    private SharedPrefs() {

    }


    public static void setLoggedInAs(String token,Context context) {
        preferenceSetter("setLoggedInAs", token,context);
    }

    public static String getLoggedInAs(Context context) {
        return preferenceGetter("setLoggedInAs",context);
    }

    public static void setBuyermodel(buyermodel model,Context context) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("BuyerModel", json,context);
    }


    public static buyermodel getBuyermodel(Context context) {
        Gson gson = new Gson();
        buyermodel model = gson.fromJson(preferenceGetter("BuyerModel",context), buyermodel.class);
        return model;
    }



    public static void setSellerModel(sellermodel model,Context context) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("SellerModel", json,context);
    }

    public static void setProductModel(productmodel model, Context context) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("ProductModel", json,context);
    }



    public static void preferenceSetter(String key, String value,Context context) {
        SharedPreferences pref = context.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static sellermodel getSellerModel(Context context) {
        Gson gson = new Gson();
        sellermodel model = gson.fromJson(preferenceGetter("SellerModel",context), sellermodel.class);
        return model;
    }

//    public static void preferenceSetter(String key, String value) {
//        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString(key, value);
//        editor.apply();
//    }

    public static String preferenceGetter(String key,Context context) {
        SharedPreferences pref;
        String value = "";
        pref = context.getSharedPreferences("user", MODE_PRIVATE);
        value = pref.getString(key, "");
        return value;
    }



    public static void logout(Context context) {
        SharedPreferences pref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

}

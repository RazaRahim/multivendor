package com.example.megastock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.megastock.Models.pendingordermodel;
import com.example.megastock.Models.productmodel;
import com.example.megastock.R;
import com.example.megastock.ui.buyer.activity_product_detail;

import java.util.ArrayList;


public class showpendingproductAdapter extends RecyclerView.Adapter<showpendingproductAdapter.productviewHolder> {

    Context context;
    ArrayList<pendingordermodel> productmodels;
    ArrayList<String> keyss;
    String name,address,city,date,time,phone,status;

    //its constructior for show products in buyer activity


    public showpendingproductAdapter(Context context, ArrayList<pendingordermodel> productmodels, ArrayList<String> keyss, String name, String address, String city, String date, String time, String phone, String status) {
        this.context = context;
        this.productmodels = productmodels;
        this.keyss = keyss;
        this.name = name;
        this.address = address;
        this.city = city;
        this.date = date;
        this.time = time;
        this.phone = phone;
        this.status = status;
    }

    @NonNull
    @Override
    public productviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pendingdetaitl, parent, false);
        return new showpendingproductAdapter.productviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productviewHolder holder, final int position) {
        final pendingordermodel model = productmodels.get(position);

        Glide.with(holder.productimage.getContext()).load(model.getUrl()).into(holder.productimage);
        holder.name.setText(model.getName());
        holder.quantity.setText("Qty: ".concat(model.getNo()));
        holder.address.setText(address);
        holder.Name.setText(name);
        holder.city.setText(city);
        holder.date.setText(date);
        holder.time.setText(time);
        holder.phoneno.setText(phone);


        int a = Integer.parseInt(model.getPrice());
        int b = Integer.parseInt(model.getNo());
        int pric = a*b;

        holder.price.setText("Rs: ".concat(String.valueOf(pric)));

        holder.delivered.setVisibility(View.GONE);
        holder.wait.setVisibility(View.GONE);
        if(status.equalsIgnoreCase("Undelivered")){
            holder.wait.setVisibility(View.VISIBLE);
        }else if(status.equalsIgnoreCase("delivered")){
            holder.wait.setVisibility(View.GONE);
            holder.delivered.setVisibility(View.VISIBLE);
        }else {
            holder.wait.setVisibility(View.VISIBLE);
            holder.delivered.setVisibility(View.GONE);
        }


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, activity_product_detail.class);
//                intent.putExtra("name",model.getName());
//                intent.putExtra("image",model.getUrl());
//                intent.putExtra("Desc",model.getDescription());
//                intent.putExtra("price",model.getPrice());
//                intent.putExtra("quantity",model.getQuantity());
//                intent.putExtra("Key",keyss.get(position));
//                intent.putExtra("Brand",model.getBrand());
//
//
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return productmodels.size();
    }
    class  productviewHolder extends RecyclerView.ViewHolder{

        TextView name,quantity,price,Name,address,city,date,time,phoneno;
        ImageView productimage,wait,delivered;

        public productviewHolder(@NonNull View itemView) {
            super(itemView);

            name= itemView.findViewById(R.id.product_name);
            quantity= itemView.findViewById(R.id.product_quantity);
            price= itemView.findViewById(R.id.product_price);
            productimage=itemView.findViewById(R.id.product_image);
            Name=itemView.findViewById(R.id.name);
            address=itemView.findViewById(R.id.address);
            city=itemView.findViewById(R.id.city);
            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
            phoneno=itemView.findViewById(R.id.PhoneNo);
            wait=itemView.findViewById(R.id.wait);
            delivered=itemView.findViewById(R.id.delivered);
        }
    }

}

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
import com.example.megastock.Models.productmodel;
import com.example.megastock.R;
import com.example.megastock.ui.buyer.activity_product_detail;

import java.util.ArrayList;


public class showproductAdapter extends RecyclerView.Adapter<showproductAdapter.productviewHolder> {

    Context context;
    ArrayList<productmodel> productmodels;
    ArrayList<String> keyss;

    //its constructior for show products in buyer activity
    public showproductAdapter(Context context, ArrayList<productmodel> productmodels,ArrayList<String> keyss) {
        this.context = context;
        this.productmodels = productmodels;
        this.keyss=keyss;
    }

    @NonNull
    @Override
    public productviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialouge2, parent, false);
        return new showproductAdapter.productviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productviewHolder holder, final int position) {
        final productmodel model = productmodels.get(position);

        Glide.with(holder.productimage.getContext()).load(model.getUrl()).into(holder.productimage);
        holder.name.setText(model.getName());
        holder.quantity.setText("Qty: ".concat(model.getQuantity()));
        holder.price.setText("Rs: ".concat(model.getPrice()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_product_detail.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("image",model.getUrl());
                intent.putExtra("Desc",model.getDescription());
                intent.putExtra("price",model.getPrice());
                intent.putExtra("quantity",model.getQuantity());
                intent.putExtra("Key",keyss.get(position));
                intent.putExtra("Brand",model.getBrand());


                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productmodels.size();
    }
    class  productviewHolder extends RecyclerView.ViewHolder{

        TextView name,quantity,price;
        ImageView productimage;

        public productviewHolder(@NonNull View itemView) {
            super(itemView);

            name= itemView.findViewById(R.id.product_name);
            quantity= itemView.findViewById(R.id.product_quantity);
            price= itemView.findViewById(R.id.product_price);
            productimage=itemView.findViewById(R.id.product_image);
        }
    }

}

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
import com.example.megastock.Models.odersmodel;
import com.example.megastock.Models.productmodel;
import com.example.megastock.R;
import com.example.megastock.orderstatus;
import com.example.megastock.ui.buyer.activity_product_detail;

import java.util.ArrayList;


public class showorderAdapter extends RecyclerView.Adapter<showorderAdapter.productviewHolder> {

    Context context;
    ArrayList<odersmodel> productmodels;
    ArrayList<String> keyss;

    //its constructior for show products in buyer activity
    public showorderAdapter(Context context, ArrayList<odersmodel> ordermodel, ArrayList<String> keyss) {
        this.context = context;
        this.productmodels = ordermodel;
        this.keyss=keyss;
    }

    @NonNull
    @Override
    public productviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlist, parent, false);
        return new showorderAdapter.productviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productviewHolder holder, final int position) {
        final odersmodel model = productmodels.get(position);

//        Glide.with(holder.productimage.getContext()).load(model.getUrl()).into(holder.productimage);
        holder.name.setText(model.getName());
//        holder.quantity.setText("Qty: ".concat(model.getQuantity()));
        holder.price.setText("Rs: ".concat(model.getTotalPrice()));

        holder.status.setText(model.getState());
        holder.no.setText(position+1+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, orderstatus.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("address",model.getAddress());
                intent.putExtra("city",model.getCity());
                intent.putExtra("date",model.getDate());
                intent.putExtra("phone",model.getPhone());
                intent.putExtra("Key",keyss.get(position));
                intent.putExtra("time",model.getTime());
                intent.putExtra("totalprice",model.getTotalPrice());
                intent.putExtra("status",model.getState());
//                intent.putExtra("Brand",model.ge);


                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productmodels.size();
    }
    class  productviewHolder extends RecyclerView.ViewHolder{

        TextView name,status,price,no;


        public productviewHolder(@NonNull View itemView) {
            super(itemView);

            name= itemView.findViewById(R.id.product_name);
            status= itemView.findViewById(R.id.status);
            price= itemView.findViewById(R.id.product_price);
            no=itemView.findViewById(R.id.id);
        }
    }

}

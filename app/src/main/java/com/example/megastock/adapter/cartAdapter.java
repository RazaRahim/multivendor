package com.example.megastock.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.megastock.Interface.ItemClickListener;
import com.example.megastock.Models.CartItems;
import com.example.megastock.Models.cartModel;
import com.example.megastock.Models.productmodel;
import com.example.megastock.R;
import com.example.megastock.Utils.SharedPrefs;
import com.example.megastock.ui.buyer.activity_product_detail;
import com.example.megastock.ui.buyer.cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.cartViewHolder> {
    Context context;
    ArrayList<cartModel> cartModels;

    public cartAdapter(Context context, ArrayList<cartModel> cartModels) {
        this.context = context;
        this.cartModels = cartModels;
    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);
        return new cartAdapter.cartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cartViewHolder holder, int position) {
        final cartModel model = new cartModel();
        holder.cart_name.setText(model.getName());
        holder.cart_price.setText(model.getPrice());
        holder.cart_quantity.setText(model.getQuantity());

//        holder.cart_number_button.setNumber(model.getQuantity());
//        int price = Integer.parseInt(model.getPrice());
//        int quan = Integer.parseInt(model.getQuantity());

//        int total_price = 0;
//        total_price += price*quan;
//        txt_total_price.setText(String.valueOf(total_price));
//        Toast.makeText(context,"Total Price is: "+total_price, Toast.LENGTH_LONG).show();

        holder.btn_del_cart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("This item will be deleted from your cart.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("cart").child(SharedPrefs.getBuyermodel(context).getPhone()).child(model.getNo());
                                ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context,"Removed From Cart",Toast.LENGTH_LONG).show();
                                        Snackbar.make(view, " Removed From Cart", Snackbar.LENGTH_LONG)
                                                .setAction("Undo", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        context.startActivity(new Intent(context, cart.class));
                                                    }
                                                }).show();
                                    }
                                });
                            }
                        });
                builder.setTitle("Remove From Cart?");
                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    class cartViewHolder extends RecyclerView.ViewHolder{
        public TextView cart_name, cart_price, cart_quantity;
        public ElegantNumberButton cart_number_button;
        ImageView btn_del_cart;

        public cartViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_name = itemView.findViewById(R.id.cart_item_name);
            cart_price = itemView.findViewById(R.id.cart_item_price);
            cart_quantity = itemView.findViewById(R.id.cart_item_quantity);
            cart_number_button = itemView.findViewById(R.id.cart_number_button);
            btn_del_cart = itemView.findViewById(R.id.btn_del_cart);

        }

    }
}

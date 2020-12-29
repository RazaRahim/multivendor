package com.example.megastock.ui.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.megastock.CartViewHolder;
import com.example.megastock.Models.CartItems;
import com.example.megastock.Models.cartModel;
import com.example.megastock.Models.productmodel;
import com.example.megastock.R;
import com.example.megastock.Utils.SharedPrefs;
import com.example.megastock.adapter.cartAdapter;
import com.example.megastock.adapter.showproductAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import info.hoang8f.widget.FButton;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class cart extends AppCompatActivity {


    SharedPreferences prefs;
    public String Quant;
    String username = "";

    StringBuffer buf_name, buf_price, buf_quantity;

    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<CartItems, CartViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;

    FButton btn_placeorder;
    int total_price = 0;

    TextView txt_total_price;


    @Override
    protected void onStart() {
        super.onStart();

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        adapter.startListening();
//    }

    @Override
    protected void onRestart() {
        super.onRestart();

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        total_price = 0;
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        total_price = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        buf_name = new StringBuffer();
        buf_price = new StringBuffer();
        buf_quantity = new StringBuffer();

        txt_total_price = findViewById(R.id.txt_total_price);
        btn_placeorder = findViewById(R.id.btn_placeorder);
        btn_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startPayment();
            }
        });

        recyclerView = findViewById(R.id.recycler_menu_cart);
//        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        fetch_cart();
    }


    private void fetch_cart() {

        Query query = FirebaseDatabase.getInstance().getReference().child("cart").child(SharedPrefs.getBuyermodel(cart.this).getPhone());

        FirebaseRecyclerOptions<CartItems> options =
                new FirebaseRecyclerOptions.Builder<CartItems>()
                        .setQuery(query, new SnapshotParser<CartItems>() {
                                    @NonNull
                                    @Override
                                    public CartItems parseSnapshot(@NonNull DataSnapshot snapshot) {


                                        return new
                                                CartItems(snapshot.child("name").getValue().toString(),
                                                snapshot.child("price").getValue().toString(),
                                                snapshot.child("no").getValue().toString(),
                                                snapshot.child("prodkey").getValue().toString());

                                    }
                                }
                        )
                        .build();

        adapter = new FirebaseRecyclerAdapter<CartItems, CartViewHolder>(options) {
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);
                CartViewHolder productViewHolder = new CartViewHolder(view);
                return productViewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder productViewHolder, final int position, final CartItems products) {
                productViewHolder.cart_name.setText(products.getName());
                productViewHolder.cart_price.setText(products.getPrice());
                productViewHolder.cart_quantity.setText(products.getQuantity());
                productViewHolder.cart_number_button.setNumber(products.getQuantity());
                int price = Integer.parseInt(products.getPrice());
                int quan = Integer.parseInt(products.getQuantity());
                total_price += price*quan;
                txt_total_price.setText(String.valueOf(total_price));
                Toast.makeText(getApplicationContext(),"Total Price is: "+total_price, Toast.LENGTH_LONG).show();
//                txt_total_price.setText(String.valueOf(products.getTotal_price()));
//                update number button on change
//                Quant =  productViewHolder.cart_number_button.getNumber();
//                DatabaseReference cart_ref;
//                cart_ref = FirebaseDatabase.getInstance().getReference("cart");
//                cart_ref.child(SharedPrefs.getBuyermodel(cart.this).getPhone()).child(products.getId()).child("no").setValue(Quant);
//
////
////
//                int pric = Integer.parseInt(products.getPrice());
//                int qua = Integer.parseInt(Quant);
//                total_price += pric*qua;
//                Toast.makeText(getApplicationContext(),"Total Price is: "+total_price, Toast.LENGTH_LONG).show();

                productViewHolder.btn_del_cart.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(cart.this);
                        builder.setMessage("This item will be deleted from your cart.")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("cart").child(SharedPrefs.getBuyermodel(cart.this).getPhone()).child(products.getId());
                                        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(),"Removed From Cart",Toast.LENGTH_LONG).show();
                                                Snackbar.make(view, " Removed From Cart", Snackbar.LENGTH_LONG)
                                                        .setAction("Undo", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                startActivity(new Intent(cart.this, cart.class));
                                                            }
                                                        }).show();
                                                recreate();
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

        };

//        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}
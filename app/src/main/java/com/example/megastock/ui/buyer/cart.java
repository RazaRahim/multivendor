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
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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


    public String Quant;

    StringBuffer buf_name, buf_price, buf_quantity;

    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<CartItems, CartViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;

    Button btn_placeorder;
    public static int total_price = 0;

    TextView txt_total_price;
    FirebaseRecyclerOptions<CartItems> options = null;

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
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        buf_name = new StringBuffer();
        buf_price = new StringBuffer();
        buf_quantity = new StringBuffer();

        txt_total_price = findViewById(R.id.txt_total_price);
        btn_placeorder = findViewById(R.id.btn_placeorder);

        recyclerView = findViewById(R.id.recycler_menu_cart);
//        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        fetch_cart();

        getTotalPrice();
//        final String txtT = txt_total_price.getText().toString();



    }

        private void getTotalPrice() {

        FirebaseDatabase.getInstance().getReference().child("cart").child("UserView").child(SharedPrefs.getBuyermodel(cart.this).getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        total_price = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            cartModel cartItems = dataSnapshot.getValue(cartModel.class);
                            assert cartItems != null;
                            total_price += Integer.parseInt(cartItems.getPrice())*Integer.parseInt(cartItems.getNo());
                        }

                        txt_total_price.setText(String.valueOf(total_price));
                        final String art = String.valueOf(total_price);
                        btn_placeorder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =new Intent(cart.this,choose_payment_method.class);
                                intent.putExtra("totalprice", art);
                                startActivity(intent);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    private void fetch_cart() {

        Query query = FirebaseDatabase.getInstance().getReference().child("cart").child("UserView").child(SharedPrefs.getBuyermodel(cart.this).getPhone());

        options =
                new FirebaseRecyclerOptions.Builder<CartItems>()
                        .setQuery(query, new SnapshotParser<CartItems>() {
                                    @NonNull
                                    @Override
                                    public CartItems parseSnapshot(@NonNull DataSnapshot snapshot) {
                                        return new
                                                CartItems(snapshot.child("name").getValue().toString(),
                                                snapshot.child("price").getValue().toString(),
                                                snapshot.child("no").getValue().toString(),
                                                snapshot.child("prodkey").getValue().toString(),
                                                snapshot.child("url").getValue().toString());

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
            protected void onBindViewHolder(@NonNull final CartViewHolder productViewHolder, final int position, final CartItems products) {

                productViewHolder.cart_name.setText(products.getName());
                int tempy = Integer.parseInt(products.getQuantity())*Integer.parseInt(products.getPrice());
                productViewHolder.cart_price.setText(String.valueOf(tempy));
                productViewHolder.cart_quantity.setText(products.getQuantity());
                Glide.with(productViewHolder.proImage.getContext()).load(products.getUrl()).into(productViewHolder.proImage);
                productViewHolder.cart_number_button.setNumber(products.getQuantity());

                productViewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Quant =  productViewHolder.cart_number_button.getNumber();
                        DatabaseReference cart_ref;
                cart_ref = FirebaseDatabase.getInstance().getReference("cart").child("UserView");
                cart_ref.child(SharedPrefs.getBuyermodel(cart.this).getPhone()).child(products.getId()).child("no").setValue(Quant);

                notifyDataSetChanged();

                getTotalPrice();

                    }
                });

                productViewHolder.btn_del_cart.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(cart.this);
                        builder.setMessage("This item will be deleted from your cart.")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("cart").child("UserView").child(SharedPrefs.getBuyermodel(cart.this).getPhone()).child(products.getId());
                                        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("cart").child("AdminView").child(SharedPrefs.getBuyermodel(cart.this).getPhone()).child(products.getId());
                                                    ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(getApplicationContext(), "Removed From Cart", Toast.LENGTH_LONG).show();
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
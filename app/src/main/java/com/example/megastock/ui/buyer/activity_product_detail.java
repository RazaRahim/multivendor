package com.example.megastock.ui.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.megastock.Models.cartModel;
import com.example.megastock.Models.productmodel;
import com.example.megastock.R;
import com.example.megastock.Utils.SharedPrefs;
import com.example.megastock.ui.Seller.sellersignup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_product_detail extends AppCompatActivity {
    TextView prod_name,prod_brand, prod_price, prod_desc,prod_quantity,pro_Key;
    ImageView prod_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String proName,brand,proPrice,proDescc,proImage,proQuantity,proKey,No;
    Task<Void> cart_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        pro_Key = findViewById(R.id.prokey);

        prod_name = (TextView) findViewById(R.id.product_details_name);
        prod_brand = findViewById(R.id.product_details_brand);
        prod_price = (TextView) findViewById(R.id.product_details_price);
        prod_desc = (TextView) findViewById(R.id.detail);
        prod_image = (ImageView)findViewById(R.id.product_details_image);
        prod_quantity = (TextView) findViewById(R.id.pro_quantity);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);

        btnCart = findViewById(R.id.btn_cart);
        numberButton = findViewById(R.id.number_button);

        proName = getIntent().getStringExtra("name");
        brand = getIntent().getStringExtra("Brand");
        proImage = getIntent().getStringExtra("image");
        proDescc = getIntent().getStringExtra("Desc");
        proPrice = getIntent().getStringExtra("price");
        proQuantity = getIntent().getStringExtra("quantity");
        proKey = getIntent().getStringExtra("Key");

        Toast.makeText(this, ""+proKey, Toast.LENGTH_SHORT).show();

        Glide.with(activity_product_detail.this)
                .load(proImage)
                .into(prod_image);
        prod_name.setText(proName);
        prod_brand.setText(brand);
        prod_price.setText("Rs: "+proPrice);
        prod_desc.setText(proDescc);
        prod_quantity.setText("Qty: "+proQuantity);
        pro_Key.setText(proKey);


        btnCart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(final View view) {
//        int noo = Integer.parseInt(numberButton.getNumber());
//        int ppp = Integer.parseInt(proPrice);
//        int pri = ppp*noo;
//        No = String.valueOf(pri);


        final cartModel model = new cartModel(
                proName,brand,proName,proPrice,proQuantity,proDescc,proImage,proKey,numberButton.getNumber()
        );
        cart_ref=FirebaseDatabase.getInstance().getReference().child("cart").child("UserView").child(SharedPrefs.getBuyermodel(activity_product_detail.this).getPhone())
                .child(proKey).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            cart_ref=FirebaseDatabase.getInstance().getReference().child("cart").child("AdminView").child(SharedPrefs.getBuyermodel(activity_product_detail.this).getPhone())
                                    .child(proKey).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Snackbar.make(view, model.getName() + " added to cart", Snackbar.LENGTH_LONG)
                                                        .setAction("View Cart", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                startActivity(new Intent(activity_product_detail.this, cart.class));
                                                            }
                                                        }).show();
                                            }
                                        }
                                    });

                        }

                    }
                });

    }
});


    }
}
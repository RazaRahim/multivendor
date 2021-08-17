package com.example.megastock.ui.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.megastock.Models.cartModel;
import com.example.megastock.Models.productmodel;
import com.example.megastock.R;
import com.example.megastock.Splash;
import com.example.megastock.Utils.SharedPrefs;
import com.example.megastock.ui.Seller.SellerEdit;
import com.example.megastock.ui.Seller.order;
import com.example.megastock.ui.Seller.sellersignup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_product_detail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView prod_name,prod_brand, prod_price, prod_desc,prod_quantity,pro_Key;
    ImageView prod_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String proName,brand,proPrice,proDescc,proImage,proQuantity,proKey,No,prostatus;
    Task<Void> cart_ref;
    private Toolbar toolbarr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolbarr = (Toolbar) findViewById(R.id.toolbar4);
        TextView mTitle = (TextView) toolbarr.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbarr);
        mTitle.setText(toolbarr.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle.setText("Add to Cart");

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
        prostatus = "Undelivered";

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
                proName,brand,proName,proPrice,proQuantity,proDescc,proImage,proKey,numberButton.getNumber(),prostatus
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

        initDrawer();
    }

    private void initDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbarr, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        NavigationView navigationView = (NavigationView)  findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        CircleImageView image = headerView.findViewById(R.id.imageView);
        TextView navUsername = (TextView) headerView.findViewById(R.id.name_drawer);
        TextView navSubtitle = (TextView) headerView.findViewById(R.id.subtitle_drawer);
        if (SharedPrefs.getLoggedInAs(activity_product_detail.this).equalsIgnoreCase("Buyer")) {
            Glide.with(activity_product_detail.this).load(SharedPrefs.getBuyermodel(activity_product_detail.this).getPicUrl()).into(image);

            navUsername.setText("Welcome, " + SharedPrefs.getBuyermodel(activity_product_detail.this).getName());
        } else if (SharedPrefs.getLoggedInAs(activity_product_detail.this).equalsIgnoreCase("Seller")) {
            Glide.with(activity_product_detail.this).load(SharedPrefs.getSellerModel(activity_product_detail.this).getPicUrl()).into(image);

            navUsername.setText("Welcome, " + SharedPrefs.getSellerModel(activity_product_detail.this).getName());

        } else {
            Glide.with(activity_product_detail.this).load(R.drawable.logo).into(image);
            navUsername.setText("Admin");

        }
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            startActivity(new Intent(activity_product_detail.this, showsellers.class));
            finish();

        } else if (id == R.id.nav_profile) {
            if (SharedPrefs.getLoggedInAs(activity_product_detail.this).equalsIgnoreCase("Seller")) {
                startActivity(new Intent(activity_product_detail.this, SellerEdit.class));
            } else if (SharedPrefs.getLoggedInAs(activity_product_detail.this).equalsIgnoreCase("Buyer")) {
                startActivity(new Intent(activity_product_detail.this, BuyerEdit.class));

            }
//            startActivity(new Intent(MainActivity.this, Profile.class));

        } else if (id == R.id.cart) {
            startActivity(new Intent(activity_product_detail.this, cart.class));
        }

        else if (id == R.id.order) {
            startActivity(new Intent(activity_product_detail.this, order.class));

        }

        else if (id == R.id.nav_faqs) {
//            startActivity(new Intent(MainActivity.this, AddFaq.class));


        } else if (id == R.id.nav_signuout) {
            SharedPrefs.logout(activity_product_detail.this);
            Intent i = new Intent(activity_product_detail.this, Splash.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

}
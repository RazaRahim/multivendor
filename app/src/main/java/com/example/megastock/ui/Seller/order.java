package com.example.megastock.ui.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.megastock.Models.buyermodel;
import com.example.megastock.Models.odersmodel;
import com.example.megastock.Models.productmodel;
import com.example.megastock.Models.sellermodel;
import com.example.megastock.R;
import com.example.megastock.Splash;
import com.example.megastock.Utils.SharedPrefs;
import com.example.megastock.adapter.shopeAdapter;
import com.example.megastock.adapter.showbuyerorderAdapter;
import com.example.megastock.adapter.showorderAdapter;
import com.example.megastock.adapter.showproductAdapter;
import com.example.megastock.ui.buyer.BuyerEdit;
import com.example.megastock.ui.buyer.cart;
import com.example.megastock.ui.buyer.showproducts;
import com.example.megastock.ui.buyer.showsellers;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class order extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView order;
    private ArrayList<sellermodel> buyer = new ArrayList<>();
    ArrayList<String> keyi = new ArrayList<>();
    ArrayList<String> keys = new ArrayList<>();
    showbuyerorderAdapter adapter;
    private Toolbar toolbarr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        keys = getIntent().getStringArrayListExtra("keys");

        toolbarr = (Toolbar) findViewById(R.id.toolbar6);
        TextView mTitle = (TextView) toolbarr.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbarr);
        mTitle.setText(toolbarr.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle.setText("Order List");

        order = findViewById(R.id.order);

        order.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
//        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Seller");

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Buyer");
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String key = dataSnapshot.getKey();

                    sellermodel mode = dataSnapshot.getValue(sellermodel.class);
                        buyer.add(mode);


                    keyi.add(key);
                    Toast.makeText(getApplicationContext(), ""+keyi, Toast.LENGTH_LONG).show();
                }
//                       try {

                adapter = new showbuyerorderAdapter(order.this,buyer);
                adapter.notifyDataSetChanged();
                order.setAdapter(adapter);


//                       }
//                       catch (Exception e){

//                           Toast.makeText(showproducts.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

//                       }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        initDrawer();

    }

    private void initDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbarr, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        CircleImageView image = headerView.findViewById(R.id.imageView);
        TextView navUsername = (TextView) headerView.findViewById(R.id.name_drawer);
        TextView navSubtitle = (TextView) headerView.findViewById(R.id.subtitle_drawer);
        if (SharedPrefs.getLoggedInAs(order.this).equalsIgnoreCase("Buyer")) {
            Glide.with(order.this).load(SharedPrefs.getBuyermodel(order.this).getPicUrl()).into(image);

            navUsername.setText("Welcome, " + SharedPrefs.getBuyermodel(order.this).getName());
        } else if (SharedPrefs.getLoggedInAs(order.this).equalsIgnoreCase("Seller")) {
            Glide.with(order.this).load(SharedPrefs.getSellerModel(order.this).getPicUrl()).into(image);

            navUsername.setText("Welcome, " + SharedPrefs.getSellerModel(order.this).getName());

        } else {
            Glide.with(order.this).load(R.drawable.logo).into(image);
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

          onBackPressed();
          finish();

        } else if (id == R.id.nav_profile) {
            if (SharedPrefs.getLoggedInAs(order.this).equalsIgnoreCase("Seller")) {
                startActivity(new Intent(order.this, SellerEdit.class));
            } else if (SharedPrefs.getLoggedInAs(order.this).equalsIgnoreCase("Buyer")) {
                startActivity(new Intent(order.this, BuyerEdit.class));

            }
//            startActivity(new Intent(MainActivity.this, Profile.class));

        } else if (id == R.id.cart) {
            startActivity(new Intent(order.this, cart.class));
        }

        else if (id == R.id.order) {
            startActivity(new Intent(order.this, order.class));

        }

        else if (id == R.id.nav_faqs) {
//            startActivity(new Intent(MainActivity.this, AddFaq.class));


        } else if (id == R.id.nav_signuout) {
            SharedPrefs.logout(order.this);
            Intent i = new Intent(order.this, Splash.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

}
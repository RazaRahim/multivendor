package com.example.megastock.ui.buyer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.megastock.Models.productmodel;
import com.example.megastock.Models.sellermodel;
import com.example.megastock.R;
import com.example.megastock.Splash;
import com.example.megastock.Utils.SharedPrefs;
import com.example.megastock.adapter.shopeAdapter;
import com.example.megastock.adapter.showproductAdapter;
import com.example.megastock.ui.Seller.SellerEdit;
import com.example.megastock.ui.Seller.order;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class showproducts extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView2;
    showproductAdapter adapter;
    private ArrayList<productmodel> productList = new ArrayList<>();
    String  sellerphone,dealername;
    private Toolbar toolbarr;
    DatabaseReference databaseReference;
    ArrayList<String> keyi = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        sellerphone = getIntent().getStringExtra("phoneNo");
        dealername = getIntent().getStringExtra("Dealername");

        toolbarr = (Toolbar) findViewById(R.id.toolbar3);
        TextView mTitle = (TextView) toolbarr.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbarr);
        mTitle.setText(toolbarr.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle.setText("Products of "+dealername);


        recyclerView2 = findViewById(R.id.recyclerviewm);
        recyclerView2.setLayoutManager(new GridLayoutManager(this,2));




        Toast.makeText(this, ""+sellerphone, Toast.LENGTH_LONG).show();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Products").child(sellerphone);
               databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       for (DataSnapshot dataSnapshot : snapshot.getChildren())
                       {
                           String key = dataSnapshot.getKey();

                           productmodel model = dataSnapshot.getValue(productmodel.class);
                           productList.add(model);
                           keyi.add(key);
                           Toast.makeText(showproducts.this, ""+keyi, Toast.LENGTH_LONG).show();

                       }
//                       try {

                           adapter = new showproductAdapter(showproducts.this,productList,keyi);
                           recyclerView2.setAdapter(adapter);

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
        NavigationView navigationView = (NavigationView)  findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        CircleImageView image = headerView.findViewById(R.id.imageView);
        TextView navUsername = (TextView) headerView.findViewById(R.id.name_drawer);
        TextView navSubtitle = (TextView) headerView.findViewById(R.id.subtitle_drawer);
        if (SharedPrefs.getLoggedInAs(showproducts.this).equalsIgnoreCase("Buyer")) {
            Glide.with(showproducts.this).load(SharedPrefs.getBuyermodel(showproducts.this).getPicUrl()).into(image);

            navUsername.setText("Welcome, " + SharedPrefs.getBuyermodel(showproducts.this).getName());
        } else if (SharedPrefs.getLoggedInAs(showproducts.this).equalsIgnoreCase("Seller")) {
            Glide.with(showproducts.this).load(SharedPrefs.getSellerModel(showproducts.this).getPicUrl()).into(image);

            navUsername.setText("Welcome, " + SharedPrefs.getSellerModel(showproducts.this).getName());

        } else {
            Glide.with(showproducts.this).load(R.drawable.logo).into(image);
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
            if (SharedPrefs.getLoggedInAs(showproducts.this).equalsIgnoreCase("Seller")) {
                startActivity(new Intent(showproducts.this, SellerEdit.class));
            } else if (SharedPrefs.getLoggedInAs(showproducts.this).equalsIgnoreCase("Buyer")) {
                startActivity(new Intent(showproducts.this, BuyerEdit.class));

            }
//            startActivity(new Intent(MainActivity.this, Profile.class));

        } else if (id == R.id.cart) {
            startActivity(new Intent(showproducts.this, cart.class));
        }

        else if (id == R.id.order) {
            startActivity(new Intent(showproducts.this, order.class));

        }

        else if (id == R.id.nav_faqs) {
//            startActivity(new Intent(MainActivity.this, AddFaq.class));


        } else if (id == R.id.nav_signuout) {
            SharedPrefs.logout(showproducts.this);
            Intent i = new Intent(showproducts.this, Splash.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}

package com.example.megastock.ui.Seller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.megastock.MainActivity;
import com.example.megastock.Models.odersmodel;
import com.example.megastock.R;
import com.example.megastock.Splash;
import com.example.megastock.Utils.SharedPrefs;
import com.example.megastock.adapter.showorderAdapter;
import com.example.megastock.ui.buyer.BuyerEdit;
import com.example.megastock.ui.buyer.cart;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class selleractivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    CardView posts,update_view;
    Button logout;
    private Toolbar toolbarr;
    ArrayList<String> keyi = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbarr = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbarr);
        logout = findViewById(R.id.logout);

        posts = findViewById(R.id.posts);
        update_view = findViewById(R.id.update);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefs.logout(selleractivity.this);
                Intent i = new Intent(selleractivity.this, Splash.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

            }
        });

        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(selleractivity.this, AddProducts.class));
            }
        });

        update_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(selleractivity.this, updateproduct.class);
                startActivity(intent);
            }
        });

        initDrawer();




//        Intent intent = new Intent(selleractivity.this, showproducts.class);
//        String uidphn = SharedPrefs.getSellerModel(selleractivity.this).getPhone();
//        intent.putExtra("sellerPhoneNo", uidphn);
//        startActivity(intent);



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
        if (SharedPrefs.getLoggedInAs(selleractivity.this).equalsIgnoreCase("Buyer")) {
            Glide.with(selleractivity.this).load(SharedPrefs.getBuyermodel(selleractivity.this).getPicUrl()).into(image);

            navUsername.setText("Welcome, " + SharedPrefs.getBuyermodel(selleractivity.this).getName());
        } else if (SharedPrefs.getLoggedInAs(selleractivity.this).equalsIgnoreCase("Seller")) {
            Glide.with(selleractivity.this).load(SharedPrefs.getSellerModel(selleractivity.this).getPicUrl()).into(image);

            navUsername.setText("Welcome, " + SharedPrefs.getSellerModel(selleractivity.this).getName());

        } else {
            Glide.with(selleractivity.this).load(R.drawable.logo).into(image);
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
            // Handle the camera action
        } else if (id == R.id.nav_profile) {
            if (SharedPrefs.getLoggedInAs(selleractivity.this).equalsIgnoreCase("Seller")) {
                startActivity(new Intent(selleractivity.this, SellerEdit.class));
            } else if (SharedPrefs.getLoggedInAs(selleractivity.this).equalsIgnoreCase("Buyer")) {
                startActivity(new Intent(selleractivity.this, BuyerEdit.class));

            }
//            startActivity(new Intent(MainActivity.this, Profile.class));

        }
//        else if (id == R.id.cart) {
//            startActivity(new Intent(selleractivity.this, cart.class));
//
//        }
        else if (id == R.id.order) {
            Intent intent = new Intent(selleractivity.this,order.class);
//            intent.putExtra("keys",keyi);
            startActivity(intent);

        }
        else if (id == R.id.nav_faqs) {
//            startActivity(new Intent(MainActivity.this, AddFaq.class));


        } else if (id == R.id.nav_signuout) {
            SharedPrefs.logout(selleractivity.this);
            Intent i = new Intent(selleractivity.this, Splash.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

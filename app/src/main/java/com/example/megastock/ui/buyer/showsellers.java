package com.example.megastock.ui.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.megastock.Models.productmodel;
import com.example.megastock.Models.sellermodel;
import com.example.megastock.R;
import com.example.megastock.Splash;
import com.example.megastock.Utils.SharedPrefs;
import com.example.megastock.adapter.shopeAdapter;
import com.example.megastock.ui.Seller.SellerEdit;
import com.example.megastock.ui.Seller.order;
import com.example.megastock.ui.Seller.selleractivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CALL_PHONE;

public class showsellers extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
     RecyclerView recyclerView;
     shopeAdapter adapter;
    private ArrayList<sellermodel> itemList = new ArrayList<>();

    private Toolbar toolbarr;
    DatabaseReference databaseReference;
int MY_PERMISSIONS_REQUEST_CALL_PHONE= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showproducts);


        if (ContextCompat.checkSelfPermission(showsellers.this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(showsellers.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }


        toolbarr = (Toolbar) findViewById(R.id.toolbar2);
        TextView mTitle = (TextView) toolbarr.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbarr);
        mTitle.setText(toolbarr.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle.setText("List of All Dealers");


        recyclerView = findViewById(R.id.recyclerviewp);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

               databaseReference=FirebaseDatabase.getInstance().getReference("Seller");
               databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       for (DataSnapshot snapshot : dataSnapshot.getChildren())
                       {
                           sellermodel model = snapshot.getValue(sellermodel.class);
                           itemList.add(model);
                       }
                       adapter = new shopeAdapter(showsellers.this,itemList);
                       recyclerView.setAdapter(adapter);
                       }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });


//               DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Products").child(sellerPhoneNo);
//               databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
//                   @Override
//                   public void onDataChange(@NonNull DataSnapshot snapshot) {
//                       for (DataSnapshot dataSnapshot : snapshot.getChildren())
//                       {
//                           productmodel model = dataSnapshot.getValue(productmodel.class);
//                           productList.add(model);
//                       }
//                       adapter = new shopeAdapter(showproducts.this,productList);
//                       recyclerView.setAdapter(adapter);
//
//                   }
//
//                   @Override
//                   public void onCancelled(@NonNull DatabaseError error) {
//
//                   }
//               });
//


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
        if (SharedPrefs.getLoggedInAs(showsellers.this).equalsIgnoreCase("Buyer")) {
            Glide.with(showsellers.this).load(SharedPrefs.getBuyermodel(showsellers.this).getPicUrl()).into(image);

            navUsername.setText("Welcome, " + SharedPrefs.getBuyermodel(showsellers.this).getName());
        } else if (SharedPrefs.getLoggedInAs(showsellers.this).equalsIgnoreCase("Seller")) {
            Glide.with(showsellers.this).load(SharedPrefs.getSellerModel(showsellers.this).getPicUrl()).into(image);

            navUsername.setText("Welcome, " + SharedPrefs.getSellerModel(showsellers.this).getName());

        } else {
            Glide.with(showsellers.this).load(R.drawable.logo).into(image);
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


            } else if (id == R.id.nav_profile) {
                if (SharedPrefs.getLoggedInAs(showsellers.this).equalsIgnoreCase("Seller")) {
                    startActivity(new Intent(showsellers.this, SellerEdit.class));
                } else if (SharedPrefs.getLoggedInAs(showsellers.this).equalsIgnoreCase("Buyer")) {
                    startActivity(new Intent(showsellers.this, BuyerEdit.class));

                }
//            startActivity(new Intent(MainActivity.this, Profile.class));

            } else if (id == R.id.cart) {
            startActivity(new Intent(showsellers.this, cart.class));
            }

            else if (id == R.id.order) {
                startActivity(new Intent(showsellers.this, order.class));

            }

            else if (id == R.id.nav_faqs) {
//            startActivity(new Intent(MainActivity.this, AddFaq.class));


            } else if (id == R.id.nav_signuout) {
                SharedPrefs.logout(showsellers.this);
                Intent i = new Intent(showsellers.this, Splash.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);


                finishAffinity();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;

    }
}
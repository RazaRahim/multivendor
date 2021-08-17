package com.example.megastock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.megastock.Models.pendingordermodel;
import com.example.megastock.Models.productmodel;
import com.example.megastock.adapter.showpendingproductAdapter;
import com.example.megastock.adapter.showproductAdapter;
import com.example.megastock.ui.buyer.showproducts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class orderstatus extends AppCompatActivity {
    RecyclerView recyclerView2;
    showpendingproductAdapter adapter;
    private ArrayList<pendingordermodel> productList = new ArrayList<>();
    String  sellerphone,productkey,name,address,city,date,time,totalprice,status;
    private Toolbar toolbarr;
    DatabaseReference databaseReference;
    ArrayList<String> keyi = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderstatus);

        recyclerView2 = findViewById(R.id.recyclerviewm);
        recyclerView2.setLayoutManager(new GridLayoutManager(this,2));

        sellerphone = getIntent().getStringExtra("phone");
//        productkey = getIntent().getStringExtra("Key");
        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        city = getIntent().getStringExtra("city");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        totalprice = getIntent().getStringExtra("totalprice");
        status = getIntent().getStringExtra("status");

//        Toast.makeText(getApplicationContext(), ""+productkey, Toast.LENGTH_SHORT).show();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("cart").child("AdminView").child(sellerphone);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String key = dataSnapshot.getKey();

                    pendingordermodel model = dataSnapshot.getValue(pendingordermodel.class);
                    productList.add(model);
                    keyi.add(key);
                    Toast.makeText(orderstatus.this, ""+keyi, Toast.LENGTH_LONG).show();

                }
//                       try {

                adapter = new showpendingproductAdapter(orderstatus.this,productList,keyi,name,address,city,date,time,sellerphone,status);
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


    }
}
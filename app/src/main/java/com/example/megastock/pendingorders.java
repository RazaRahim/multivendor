package com.example.megastock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.megastock.Models.odersmodel;
import com.example.megastock.adapter.showorderAdapter;
import com.example.megastock.ui.Seller.order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class pendingorders extends AppCompatActivity {
    RecyclerView pending;
    showorderAdapter adapter;
    private ArrayList<odersmodel> productList = new ArrayList<>();
    ArrayList<String> keyi = new ArrayList<>();
    String buyerphoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendingorders);
        buyerphoneno = getIntent().getStringExtra("phoneNo");
        pending = findViewById(R.id.pending);
        pending.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Command").child(buyerphoneno);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String key = dataSnapshot.getKey();

                    odersmodel model = dataSnapshot.getValue(odersmodel.class);
                    productList.add(model);
                    keyi.add(key);
                    Toast.makeText(getApplicationContext(), ""+keyi, Toast.LENGTH_LONG).show();
                }

//                       try {

                adapter = new showorderAdapter(pendingorders.this,productList,keyi);
                adapter.notifyDataSetChanged();
                pending.setAdapter(adapter);

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
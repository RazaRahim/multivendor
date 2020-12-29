package com.example.megastock.ui.buyer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.megastock.Models.productmodel;
import com.example.megastock.Models.sellermodel;
import com.example.megastock.R;
import com.example.megastock.adapter.shopeAdapter;
import com.example.megastock.adapter.showproductAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class showproducts extends AppCompatActivity {

    RecyclerView recyclerView2;
    showproductAdapter adapter;
    private ArrayList<productmodel> productList = new ArrayList<>();
    String  sellerphone;
    private Toolbar toolbarr;
    DatabaseReference databaseReference;
    ArrayList<String> keyi = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        recyclerView2 = findViewById(R.id.recyclerviewm);
        recyclerView2.setLayoutManager(new GridLayoutManager(this,2));


        sellerphone = getIntent().getStringExtra("phoneNo");

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



    }

}

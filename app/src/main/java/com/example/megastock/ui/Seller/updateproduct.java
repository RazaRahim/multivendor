package com.example.megastock.ui.Seller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.megastock.Models.getproductmodel;
import com.example.megastock.R;
import com.example.megastock.Utils.SharedPrefs;
import com.example.megastock.adapter.productAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class updateproduct extends AppCompatActivity {

    RecyclerView recyclerView;
    productAdapter adapter;
//    private ArrayList<productmodel> itemList = new ArrayList<>();
//     String userId = SharedPrefs.getSellerModel(updateproduct.this).getShpename();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateproduct);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        String userId = SharedPrefs.getSellerModel(updateproduct.this).getPhone();
        Toast.makeText(this, ""+userId, Toast.LENGTH_SHORT).show();
        FirebaseRecyclerOptions<getproductmodel> options =
                new FirebaseRecyclerOptions.Builder<getproductmodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").child(userId),getproductmodel.class)
                        .build();
//        Toast.makeText(this, ""+options, Toast.LENGTH_LONG).show();
        adapter = new productAdapter(options,updateproduct.this);

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }
    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }

}
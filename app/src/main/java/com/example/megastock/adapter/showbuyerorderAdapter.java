package com.example.megastock.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.megastock.Models.odersmodel;
import com.example.megastock.Models.sellermodel;
import com.example.megastock.R;
import com.example.megastock.orderstatus;
import com.example.megastock.pendingorders;
import com.example.megastock.ui.buyer.showproducts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CALL_PHONE;

public class showbuyerorderAdapter extends RecyclerView.Adapter<showbuyerorderAdapter.viewHolder> {

    Context context;
    ArrayList<sellermodel> sellermodels;
    ArrayList<odersmodel> productList;


    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 16;
    public showbuyerorderAdapter(Context context, ArrayList<sellermodel> sellermodels) {
        this.context=context;
        this.sellermodels=sellermodels;

    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_row, parent, false);
        return new showbuyerorderAdapter.viewHolder(view);
    }





    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
      final sellermodel model = sellermodels.get(position);
//         final odersmodel pr = productList.get(position);

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("cart").child("AdminView");
        databaseReference1.child(model.getPhone()).orderByChild("status").equalTo("Undelivered")
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    long key = snapshot.getChildrenCount();
//                     ArrayList<odersmodel> productList = new ArrayList<>();
//                    odersmodel model = dataSnapshot.getValue(odersmodel.class);
//                    productList.add(model);
//
//                    ArrayList<Long> keye = new ArrayList<>();
//                    keye.add(key);
//                   int number = keye.size();
//                    Toast.makeText(getApplicationContext(), ""+keyi, Toast.LENGTH_LONG).show();
                    holder.numbers.setText(key+"");

                }

//                       try {

//                adapter = new showorderAdapter(pendingorders.this,productList,keyi);
//                adapter.notifyDataSetChanged();
//                pending.setAdapter(adapter);


//                       }
//                       catch (Exception e){

//                           Toast.makeText(showproducts.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

//                       }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        Glide.with(holder.sellerImage.getContext()).load(model.getPicUrl()).into(holder.sellerImage);
        Glide.with(holder.sellerImage.getContext()).load(model.getPicUrl()).into(holder.sellerImage);
        holder.sellerName.setText(model.getName());
        holder.sellershope.setText(model.getShpename());


        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileno = model.getPhone();
                String call  = "tel: "+mobileno.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(call));

                if (ContextCompat.checkSelfPermission(context, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Give permissions for call", Toast.LENGTH_SHORT).show();
                }

            }
        });
holder.whatsapp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        String mobilen = model.getPhone();
        String msg  = "+92 "+mobilen.trim();
        boolean installed = isInstalled("com.whatsapp");
        if(installed)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+msg));
            context.startActivity(intent);
        }
        else
        {
            Toast.makeText(context, "Wahatsapp is not Installed", Toast.LENGTH_SHORT).show();
        }
    }
});


        final String abc = model.getPhone();
        holder.sellerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(context, ""+abc, Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(context, pendingorders.class);
                intent.putExtra("phoneNo",abc);

//                intent.putExtra("name",pr.getName());
//                intent.putExtra("address",pr.getAddress());
//                intent.putExtra("city",pr.getCity());
//                intent.putExtra("date",pr.getDate());
//                intent.putExtra("phone",pr.getPhone());
//                intent.putExtra("Key",keyss.get(position));
//                intent.putExtra("time",model.getTime());
//                intent.putExtra("totalprice",model.getTotalPrice());
//                intent.putExtra("status",model.getState());


                context.startActivity(intent);
            }
        });

    }

    private boolean isInstalled(String s) {
        PackageManager packageManager = context.getPackageManager();
        boolean is_installed;
        try {
            packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            is_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            is_installed=false;
            e.printStackTrace();
        }
        return is_installed;
    }

    @Override
    public int getItemCount() {
        return sellermodels.size();
    }

    class  viewHolder extends RecyclerView.ViewHolder{

        TextView sellerName,sellershope,numbers;
        ImageView phone,whatsapp;
        CircleImageView sellerImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            sellerName= itemView.findViewById(R.id.sellername);
            sellershope= itemView.findViewById(R.id.shopename);
            phone= itemView.findViewById(R.id.call);
            whatsapp= itemView.findViewById(R.id.whatsapp);
            sellerImage=itemView.findViewById(R.id.seller_icon);
            numbers=itemView.findViewById(R.id.numbers);
        }
    }
}

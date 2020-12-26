package com.example.megastock.adapter;

import android.Manifest;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.megastock.Models.sellermodel;
import com.example.megastock.R;
import com.example.megastock.ui.buyer.showproducts;
import com.example.megastock.ui.buyer.showsellers;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CALL_PHONE;

public class shopeAdapter extends RecyclerView.Adapter<shopeAdapter.viewHolder> {

    Context context;
    ArrayList<sellermodel> sellermodels;


    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 16;
    public shopeAdapter(Context context,ArrayList<sellermodel> sellermodels) {
        this.context=context;
        this.sellermodels=sellermodels;

    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_row, parent, false);
        return new shopeAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
      final sellermodel model = sellermodels.get(position);

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
                Intent intent =new Intent(context, showproducts.class);
                intent.putExtra("phoneNo",abc);
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

        TextView sellerName,sellershope;
        ImageView phone,whatsapp;
        CircleImageView sellerImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            sellerName= itemView.findViewById(R.id.sellername);
            sellershope= itemView.findViewById(R.id.shopename);
            phone= itemView.findViewById(R.id.call);
            whatsapp= itemView.findViewById(R.id.whatsapp);
            sellerImage=itemView.findViewById(R.id.seller_icon);
        }
    }
}

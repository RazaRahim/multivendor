package com.example.megastock.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.megastock.Models.getproductmodel;
import com.example.megastock.R;
import com.example.megastock.Utils.GifSizeFilter;
import com.example.megastock.Utils.Glide4Engine;
import com.example.megastock.Utils.SharedPrefs;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class productAdapter extends FirebaseRecyclerAdapter<getproductmodel, productAdapter.PostViewHolder> {

    Context context;
    CircleImageView imagev;
    EditText namev;
    EditText brandv;
    EditText typev;
    EditText quantityv;
    EditText pricev;
    EditText descriptionv;
    Button buttonv;
    private static final int REQUEST_CODE_CHOOSE = 23;
    public productAdapter(@NonNull FirebaseRecyclerOptions<getproductmodel> options,Context context) {
        super(options);
        this.context=context;

    }

    @Override
    protected void onBindViewHolder(@NonNull final PostViewHolder holder, final int position, @NonNull final getproductmodel model) {

        holder.name.setText(model.getName());
        holder.brand.setText("Brand: ".concat(model.getBrand()));
//        holder.type.setText("Type : ".concat(model.getType()));
//        holder.description.setText("Desc : ".concat(model.getDescription()));
        holder.price.setText("Price: ".concat(model.getPrice()));
        Glide.with(holder.image.getContext()).load(model.getUrl()).into(holder.image);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.name.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialouge))
                        .setExpanded(true, 1400)
                        .create();

                View vieeww = dialogPlus.getHolderView();
                imagev = vieeww.findViewById(R.id.picture);
                namev = vieeww.findViewById(R.id.pro_name);
                brandv = vieeww.findViewById(R.id.pro_brand);
                typev = vieeww.findViewById(R.id.pro_type);
                quantityv = vieeww.findViewById(R.id.pro_quantity);
                pricev = vieeww.findViewById(R.id.pro_price);
                descriptionv = vieeww.findViewById(R.id.pro_desc);
                buttonv = vieeww.findViewById(R.id.post);

                Glide.with(context).load(model.getUrl()).into(imagev);
                namev.setText(model.getName());
                brandv.setText(model.getBrand());
                typev.setText(model.getType());
                quantityv.setText(model.getQuantity());
                pricev.setText(model.getPrice());
                descriptionv.setText(model.getDescription());

                final String abc = getRef(position).getKey();
                dialogPlus.show();
                Toast.makeText(context, ""+abc, Toast.LENGTH_SHORT).show();

                imagev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initMatisse();
                    }
                });


                buttonv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("name", namev.getText().toString());
                        map.put("brand", brandv.getText().toString());
                        map.put("type", typev.getText().toString());
                        map.put("quantity",quantityv.getText().toString());
                        map.put("url", model.getUrl());
                        map.put("price", pricev.getText().toString());
                        map.put("description", descriptionv.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("Products")
                                .child(SharedPrefs.getSellerModel(context).getShpename())
                                .child(abc).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialogPlus.dismiss();
                                Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.image.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete......?");
                final String abc = getRef(position).getKey();
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Products")
                                .child(SharedPrefs.getSellerModel(context).getPhone())
                                .child(abc).removeValue();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });




        }

    private void initMatisse() {
        Matisse.from((Activity) context)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .showSingleMediaType(true)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(REQUEST_CODE_CHOOSE);

    }


    @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, parent, false);
                return new PostViewHolder(view);
            }


            class PostViewHolder extends RecyclerView.ViewHolder {

                TextView name, brand, type, price, description;
                CircleImageView image;
                ImageView edit, delete;

                public PostViewHolder(@NonNull View itemView) {
                    super(itemView);

                    image = itemView.findViewById(R.id.product_image);
                    name = itemView.findViewById(R.id.product_name);
                    brand = itemView.findViewById(R.id.product_brand);
//            type = itemView.findViewById(R.id.product_type);
//            description = itemView.findViewById(R.id.description);
                    price = itemView.findViewById(R.id.product_price);
                    edit = itemView.findViewById(R.id.edit);
                    delete = itemView.findViewById(R.id.delete);

                }
            }
        }
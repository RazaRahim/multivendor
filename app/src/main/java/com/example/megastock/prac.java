package com.example.megastock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.megastock.Models.productmodel;
import com.example.megastock.Utils.CompressImage;
import com.example.megastock.Utils.GifSizeFilter;
import com.example.megastock.Utils.Glide4Engine;
import com.example.megastock.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class prac extends AppCompatActivity {
    CircleImageView picture;
    EditText brand,name,type,price,description;
    Button post;
    DatabaseReference mDatabase;
    String imgUrl;
    StorageReference mStorageRef;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private String compressedPath;

    RelativeLayout wholeLayout;
    private List<Uri> mSelected = new ArrayList<>();
    private productmodel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prac);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getpermissions();

        picture = findViewById(R.id.picture);
        wholeLayout = findViewById(R.id.wholeLayout);
        brand = findViewById(R.id.pro_brand);
        name = findViewById(R.id.pro_name);
        type = findViewById(R.id.pro_type);
        price = findViewById(R.id.pro_price);
        description = findViewById(R.id.pro_desc);
        post = findViewById(R.id.post);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        getdataFromDb();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (brand.getText().length() == 0) {
                    brand.setError("Enter Brand");
                } else if (name.getText().length() == 0) {
                    name.setError("Enter Name");
                } else if (type.getText().length() == 0) {
                    type.setError("Enter Type");
                } else if (price.getText().length() == 0) {
                    price.setError("Enter Price");
                } else if (mSelected.size() > 0) {
                        putPictures(compressedPath);
                    Toast.makeText(prac.this, "Select image", Toast.LENGTH_SHORT).show();
//                    CommonUtils.showToast("Select image");
                } else {
                    updateProfile();
                }
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initMatisse();
            }
        });




    }

    private void updateProfile() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name.getText().toString());
//        map.put("whichClass", whichClass.getText().toString());
        map.put("brand", brand.getText().toString());
        map.put("type", type.getText().toString());
        map.put("url", imgUrl);
        map.put("price", price.getText().toString());
        map.put("description", description.getText().toString());
//        map.put("gender", gender);
        mDatabase.child("Products").child(SharedPrefs.getSellerModel(prac.this).getShpename()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                CommonUtils.showToast("Updated");
                Toast.makeText(prac.this, "Updated", Toast.LENGTH_SHORT).show();
                wholeLayout.setVisibility(View.GONE);

            }
        });

    }

    private void getpermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,


        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
        }
    }


    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {

                }
            }
        }
        return true;
    }



//    private void checkUser() {
//        if (studentMap.containsKey(phone.getText().toString())) {
//            Toast.makeText(this, "Username already taken\nPlease login", Toast.LENGTH_SHORT).show();
////            CommonUtils.showToast("Username already taken\nPlease login");
//        } else {
//            if (mSelected.size() > 0) {
//                putPictures(compressedPath);
//            } else {
//                registerUser();
//            }
//
//        }
//    }

    private void initMatisse() {
        Matisse.from(prac.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .showSingleMediaType(true)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);

            CompressImage compressImage = new CompressImage(prac.this);
            Glide.with(prac.this).load(mSelected.get(0)).into(picture);
//            try {
            compressedPath = compressImage.compressImage("" + mSelected.get(0));
//
//            } catch (Exception e) {
//                CommonUtils.showToast(e.getMessage());
//            }


        }

    }





    private void getdataFromDb() {
        mDatabase.child("Products").child(SharedPrefs.getSellerModel(prac.this).getShpename()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    model = dataSnapshot.getValue(productmodel.class);
                    if (model != null) {
                        brand.setText(model.getBrand());
                        name.setText(model.getName());
                        type.setText(model.getType());
                        price.setText(model.getPrice());
                        description.setText(model.getDescription());
//                        shopename.setEnabled(false);
//                        if (model.getGender().equalsIgnoreCase("male")) {
//                            gender = "Male";
//                            male.setChecked(true);
//                        } else {
//                            gender = "Female";
//                            female.setChecked(true);
//                        }

                        imgUrl = model.getUrl();
                        Glide.with(prac.this).load(model.getUrl()).into(picture);
                        SharedPrefs.setProductModel(model,prac.this);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void putPictures(String path) {
        wholeLayout.setVisibility(View.VISIBLE);
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        Uri file = Uri.fromFile(new File(path));

        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imgUrl = uri.toString();
                                updateProfile();
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
//                        CommonUtils.showToast(exception.getMessage());
                        Toast.makeText(prac.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }



}
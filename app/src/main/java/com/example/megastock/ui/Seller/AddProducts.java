package com.example.megastock.ui.Seller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.megastock.Models.productmodel;
import com.example.megastock.R;
import com.example.megastock.Utils.CommonUtils;
import com.example.megastock.Utils.CompressImage;
import com.example.megastock.Utils.GifSizeFilter;
import com.example.megastock.Utils.Glide4Engine;
import com.example.megastock.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProducts extends AppCompatActivity {


    CircleImageView picture;
    EditText brand,name,type,quantity,price,description;
    Button post;
    RelativeLayout wholeLayout;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private List<Uri> mSelected = new ArrayList<>();
    StorageReference mStorageRef;
    DatabaseReference mDatabase;
    private String imgUrl;
    private String compressedPath;



    final String URL = "https://fcm.googleapis.com/fcm/send";
    public RequestQueue mRequestQue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getPermissions();
        this.setTitle("Add Post");

        final  String uid = SharedPrefs.getSellerModel(AddProducts.this).getPhone();
        Toast.makeText(this, ""+uid, Toast.LENGTH_SHORT).show();


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mStorageRef = FirebaseStorage.getInstance().getReference();
//        Toast.makeText(AddPost.this, ""+uid, Toast.LENGTH_LONG).show();
        picture = findViewById(R.id.picture);
        wholeLayout = findViewById(R.id.wholeLayout);
        brand = findViewById(R.id.pro_brand);
        name = findViewById(R.id.pro_name);
        type = findViewById(R.id.pro_type);
        price = findViewById(R.id.pro_price);
        quantity = findViewById(R.id.pro_quantity);
        description = findViewById(R.id.pro_desc);
        post = findViewById(R.id.post);


        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMatisse();
            }
        });

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
                } else if(quantity.getText().length()==0){
                    quantity.setError("Enter Quantity");
                } else if (mSelected.size() == 0) {
                    Toast.makeText(AddProducts.this, "Select image", Toast.LENGTH_SHORT).show();
//                    CommonUtils.showToast("Select image");
                } else {
                    checkUser();
                }
            }
        });

        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("demo");




    }

    private void checkUser() {
        if (mSelected.size() > 0) {
            putPictures(compressedPath);
        } else {
            addproduct();
        }
    }

    private void addproduct() {
    final productmodel model = new productmodel(
            brand.getText().toString(),
            name.getText().toString(),
            type.getText().toString(),
            quantity.getText().toString(),
            price.getText().toString(),
            description.getText().toString(),
            imgUrl);
//        final  String uid = SharedPrefs.getSellerModel(AddPost.this).getShpename();
                 mDatabase.child("Products").child(SharedPrefs.getSellerModel(AddProducts.this).getPhone()).push()
                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            String key = mDatabase.child("Products").push().getKey();
            Toast.makeText(AddProducts.this, ""+key, Toast.LENGTH_SHORT).show();

//            mDatabase.child("Products").child(SharedPrefs.getSellerModel(AddProducts.this).getPhone()).child(key).child("id").setValue(key);


            wholeLayout.setVisibility(View.GONE);
//          Toast.makeText(AddProducts.this, "Product Add Successfully", Toast.LENGTH_SHORT).show();
//          CommonUtils.showToast("Successfully registered");
            startmainactivity();

        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            CommonUtils.showToast(e.getMessage());
        }
    });
}

    private void startmainactivity() {
        startActivity(new Intent(AddProducts.this, selleractivity.class));
        finish();

    }
//
    private void initMatisse() {
        Matisse.from(this)
                .choose(MimeType.ofAll())
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

            CompressImage compressImage = new CompressImage(AddProducts.this);
            Glide.with(AddProducts.this).load(mSelected.get(0)).into(picture);
//            try {
            compressedPath = compressImage.compressImage("" + mSelected.get(0));
//
//            } catch (Exception e) {
//                CommonUtils.showToast(e.getMessage());
//            }


        }

    }

    private void getPermissions() {
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

    private void putPictures(String path) {
        wholeLayout.setVisibility(View.VISIBLE);
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));


        Uri file = Uri.fromFile(new File(path));


        StorageReference riversRef = mStorageRef.child("productphoto").child(imgName);

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
                                addproduct();
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast(exception.getMessage());
                    }
                });


    }


    private void sendNotification() {

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + "demo");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", "Notice For You.");
            notificationObj.put("body", "click to check it out!");


            json.put("notification", notificationObj);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: " + error.networkResponse);
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAehQekhg:APA91bEq73P_Tcm4WN89lGACc41PKWQIKkNgdjKbWFAjPm07DDKhj9JXo204NcaMdHTJdL6fquF6UXr7VBtN80gb-AL7mTIrig1yAaBG3cJGQa60_w53QI1hQgbzQdecLemeLFEtKuUs");
                    return header;
                }
            };
            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

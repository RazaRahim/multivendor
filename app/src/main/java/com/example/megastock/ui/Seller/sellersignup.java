package com.example.megastock.ui.Seller;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.bumptech.glide.Glide;
import com.example.megastock.Models.sellermodel;
import com.example.megastock.R;
import com.example.megastock.Utils.CommonUtils;
import com.example.megastock.Utils.CompressImage;
import com.example.megastock.Utils.GifSizeFilter;
import com.example.megastock.Utils.Glide4Engine;

import com.example.megastock.Utils.SharedPrefs;
import com.example.megastock.ui.LoginActivity;
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

public class sellersignup extends AppCompatActivity {


    CircleImageView image;
    EditText shopename, username, password,password1, phone;

    Button register;
    TextView login;

    DatabaseReference mDatabase;
    private HashMap<String, sellermodel> studentMap = new HashMap<>();
    String imgUrl;
    StorageReference mStorageRef;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private String compressedPath;

    RelativeLayout wholeLayout;
    private List<Uri> mSelected = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_signup);
        getPermissions();

        this.setTitle("Seller register");
        image = findViewById(R.id.image);
        username = findViewById(R.id.username);
        shopename = findViewById(R.id.shopename);
        phone = findViewById(R.id.PhoneNo);
        password = findViewById(R.id.password);
        password1 = findViewById(R.id.password);
        register = findViewById(R.id.signup);
        login = findViewById(R.id.sign_in);
        wholeLayout = findViewById(R.id.wholeLayout);

        mDatabase = FirebaseDatabase.getInstance().getReference();
//        Toast.makeText(this, ""+mDatabase, Toast.LENGTH_LONG).show();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        getStudentsFromDb();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().length() == 0) {
                    username.setError("Enter text");
                } else if (shopename.getText().length() == 0) {
                    shopename.setError("Enter text");
                } else if (password.getText().length() == 0) {
                    password.setError("Enter text");
                } else if (phone.getText().length() == 0) {
                    phone.setError("Enter text");
                } else if (mSelected.size() == 0) {
                    Toast.makeText(sellersignup.this, "Select image", Toast.LENGTH_SHORT).show();
//                    CommonUtils.showToast("Select image");
                } else {
                    checkUser();
                }

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initMatisse();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }



    private void checkUser() {
        if (studentMap.containsKey(phone.getText().toString())) {
            Toast.makeText(this, "Phone No already Register\nPlease login", Toast.LENGTH_SHORT).show();
//            CommonUtils.showToast("Username already taken\nPlease login");
        } else {
            if (mSelected.size() > 0) {
                putPictures(compressedPath);
            } else {
                registerUser();
            }

        }
    }

    private void initMatisse() {
        Matisse.from(sellersignup.this)
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

    private void getStudentsFromDb() {
        mDatabase.child("Seller").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        sellermodel model = snapshot.getValue(sellermodel.class);
                        if (model != null) {
                            studentMap.put(model.getPhone(), model);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);

            CompressImage compressImage = new CompressImage(sellersignup.this);
            Glide.with(sellersignup.this).load(mSelected.get(0)).into(image);
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

        StorageReference riversRef = mStorageRef.child("SellerPhoto").child(imgName);

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
                                registerUser();
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
                        Toast.makeText(sellersignup.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void registerUser() {

        final sellermodel model = new sellermodel(
                username.getText().toString(),
                shopename.getText().toString(),
                phone.getText().toString(),
                password.getText().toString(),
                imgUrl);

        mDatabase.child("Seller").child(phone.getText().toString()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SharedPrefs.setSellerModel(model,sellersignup.this);
                wholeLayout.setVisibility(View.GONE);
                Toast.makeText(sellersignup.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                SharedPrefs.setLoggedInAs("Seller",sellersignup.this);
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
        startActivity(new Intent(sellersignup.this, LoginActivity.class));
        finish();

    }


}

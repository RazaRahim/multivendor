package com.example.megastock.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.megastock.MainActivity;
import com.example.megastock.Models.buyermodel;
import com.example.megastock.Models.sellermodel;
import com.example.megastock.R;
import com.example.megastock.Utils.SharedPrefs;
import com.example.megastock.ui.Seller.selleractivity;
import com.example.megastock.ui.Seller.sellersignup;
import com.example.megastock.ui.buyer.buyersignup;
import com.example.megastock.ui.buyer.showsellers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    RadioButton admin, seller, buyer;
    Button login;
    TextView signup;
    String userType;
    HashMap<String, sellermodel> sellermap = new HashMap<>();
    HashMap<String, buyermodel> buyermap = new HashMap<>();

    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        admin = findViewById(R.id.admin);
        seller = findViewById(R.id.student);
        buyer = findViewById(R.id.teacher);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });

        admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    userType = "admin";
                    username.setHint("Admin Id");

                }

            }
        });
        seller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    userType = "Seller";
                    username.setHint("Phone No");
                }

            }
        });
        buyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    userType = "Buyerr";
                    username.setHint("Phone No");

                }

            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType == null) {
//                    CommonUtils.showToast("Please select login type");
                    Toast.makeText(LoginActivity.this, "Please select login type", Toast.LENGTH_SHORT).show();
                } else if (username.getText().length() == 0) {
                    username.setError("Enter PhoneNo");
                } else if (password.getText().length() == 0) {
                    password.setError("Enter Password");
                } else {
                    checkLogin();
                }
            }
        });


        getsellerdatafromserver();
        getbuyerdatafromserver();

    }

    private void getbuyerdatafromserver() {
        mDatabase.child("Buyer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        buyermodel model = snapshot.getValue(buyermodel.class);
                        if (model != null) {
                            buyermap.put(model.getPhone(), model);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void getsellerdatafromserver() {
        mDatabase.child("Seller").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        sellermodel model = snapshot.getValue(sellermodel.class);
                        if (model != null) {
                            sellermap.put(model.getPhone(), model);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void checkLogin() {


        if (userType.equals("Seller")) {
            if (sellermap.containsKey(username.getText().toString())) {
                sellermodel Sellermodel = sellermap.get(username.getText().toString());
                if (Sellermodel.getPassword().equals(password.getText().toString())) {
                    Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
//                    CommonUtils.showToast("Successfully Logged in");
                    SharedPrefs.setSellerModel(Sellermodel,LoginActivity.this);
                    SharedPrefs.setLoggedInAs("Seller",LoginActivity.this);
                    startActivity(new Intent(LoginActivity.this, selleractivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
//                    CommonUtils.showToast("Wrong password");
                }

            } else {

//                CommonUtils.showToast("Account does not exists");
                Toast.makeText(this, "Account does not exists", Toast.LENGTH_SHORT).show();
            }


        }
        else if (userType.equals("Buyerr")) {
            if (buyermap.containsKey(username.getText().toString())) {
                buyermodel Buyermodel = buyermap.get(username.getText().toString());
                if (Buyermodel.getPassword().equals(password.getText().toString())) {
                    Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
//                    CommonUtils.showToast("Successfully Logged in");
                    SharedPrefs.setBuyermodel(Buyermodel,LoginActivity.this);
                    SharedPrefs.setLoggedInAs("Buyer",LoginActivity.this);



//                    Intent intent = new Intent(LoginActivity.this, showproducts.class);
//                     String uidphn =    .getText().toString();
//                     intent.putExtra("sellerPhoneNo", uidphn);
//                     startActivity(intent);


                    startActivity(new Intent(LoginActivity.this, showsellers.class));
                    finish();
                } else {
//                    CommonUtils.showToast("Wrong password");
                    Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
                }

            }
            else {
//                CommonUtils.showToast("Account does not exists");
                Toast.makeText(this, "Account does not exists", Toast.LENGTH_SHORT).show();
            }


        } else if (userType.equals("admin")) {
            if (username.getText().toString().equalsIgnoreCase("admin") &&
                    password.getText().toString().equalsIgnoreCase("admin123")) {
                //loginasadmin
                SharedPrefs.setLoggedInAs("Admin",LoginActivity.this);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
//                CommonUtils.showToast("Wrong username or password");
            }
        }
        else {
            Toast.makeText(LoginActivity.this, "Please select login type", Toast.LENGTH_SHORT).show();
        }
    }




    private void showAlert() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.alert_dialog_curved, null);

        dialog.setContentView(layout);

        TextView seller = layout.findViewById(R.id.seller);
        TextView buyer = layout.findViewById(R.id.buyer);

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, sellersignup.class));

            }
        });
        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, buyersignup.class));

            }
        });


        dialog.show();

    }


}

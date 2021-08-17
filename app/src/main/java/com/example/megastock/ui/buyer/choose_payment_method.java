package com.example.megastock.ui.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.megastock.R;
import com.example.megastock.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class choose_payment_method extends AppCompatActivity {
    private EditText nameTxt , phoneTxt , addressTxt , cityTxt;
    private Button btn_confirm;
    private String totalPrice = "";
    private String productkey = "";
    private ImageView btn_retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment_method);


        totalPrice = getIntent().getStringExtra("totalprice");
        productkey = getIntent().getStringExtra("productKey");


        btn_confirm = (Button) findViewById(R.id.btn_final);
        btn_retour = (ImageView) findViewById(R.id.retour1);
        nameTxt = (EditText) findViewById(R.id.final_name);
        phoneTxt = (EditText) findViewById(R.id.final_number);
        cityTxt = (EditText) findViewById(R.id.final_city);
        addressTxt = (EditText) findViewById(R.id.final_address);

        nameTxt.setText(SharedPrefs.getBuyermodel(choose_payment_method.this).getName());
        phoneTxt.setText(SharedPrefs.getBuyermodel(choose_payment_method.this).getPhone());
//        phoneTxt.setText(productkey);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });

        btn_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(choose_payment_method.this,cart.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private void verify() {
        if (TextUtils.isEmpty(nameTxt.getText().toString())){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneTxt.getText().toString())){
            Toast.makeText(this, "\n" +
                    "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressTxt.getText().toString())){
            Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityTxt.getText().toString())){
            Toast.makeText(this, "\n" +
                    "Please enter your city",  Toast.LENGTH_SHORT).show();
        }
        else{
            confirm();
        }
    }

    private void confirm() {
        final String saveCurrentDate , saveCurrentTIme ;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTIme = currentTime.format(calendar.getTime());

        final DatabaseReference commandRef = FirebaseDatabase.getInstance().getReference().child("Command")
                .child(SharedPrefs.getBuyermodel(choose_payment_method.this).getPhone()).child(productkey);

        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("totalPrice",totalPrice);
        hashMap.put("name",nameTxt.getText().toString());
        hashMap.put("phone",phoneTxt.getText().toString());
        hashMap.put("address",addressTxt.getText().toString());
        hashMap.put("city",cityTxt.getText().toString());
        hashMap.put("date",saveCurrentDate);
        hashMap.put("time",saveCurrentTIme);
        hashMap.put("state","Undelivered");

        commandRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("cart")
                            .child("UserView").child(SharedPrefs.getBuyermodel(choose_payment_method.this).getPhone())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(choose_payment_method.this, "Command Validate", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(choose_payment_method.this,cart.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}
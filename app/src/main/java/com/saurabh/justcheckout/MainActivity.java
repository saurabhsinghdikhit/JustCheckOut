package com.saurabh.justcheckout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saurabh.justcheckout.authentication.AuthenticationActivity;
import com.saurabh.justcheckout.classes.Product;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RadioButton radioAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        validateUser();
        ImageView cartBtn = findViewById(R.id.btn_main_cart);
        radioAll = findViewById(R.id.radioAll);
        radioAll.setChecked(true);
        cartBtn.setOnClickListener(view ->  {
            startActivity(new Intent(MainActivity.this,CartActivity.class));
        });
        fetchPopularItem();
    }

    private void fetchPopularItem() {
        ArrayList<Product> productList = new ArrayList<>();
        productList.add(new Product("1212","White","SBC","21","22gm","cotton","12","S,M,L"));
        productList.add(new Product("1212","Black","SBC","21","22gm","cotton","12","S,M,L"));
        productList.add(new Product("1212","Red","SBC","21","22gm","cotton","12","S,M,L"));
        productList.add(new Product("1212","Pink","SBC","21","22gm","cotton","12","S,M,L"));
        FirebaseDatabase.getInstance().getReference("products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void validateUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(this, AuthenticationActivity.class));
            finish();
        }
    }
}
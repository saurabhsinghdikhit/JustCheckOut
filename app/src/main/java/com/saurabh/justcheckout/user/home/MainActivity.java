package com.saurabh.justcheckout.user.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.saurabh.justcheckout.user.CartActivity;
import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.user.adapters.TopItemAdapter;
import com.saurabh.justcheckout.user.authentication.AuthenticationActivity;
import com.saurabh.justcheckout.user.classes.Product;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RadioButton radioAll;
    ViewPager2 vPager;
    private RecyclerView.Adapter topItemAdapter;
    ArrayList<String> imageList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        validateUser();
        ImageView cartBtn = findViewById(R.id.btn_main_cart);
        radioAll = findViewById(R.id.radioAll);
        radioAll.setChecked(true);
        cartBtn.setOnClickListener(view ->  {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });
        fetchPopularItem();
    }

    private void fetchPopularItem() {
        ArrayList<Product> productList = new ArrayList<>();
        productList.add(new Product("1212","White","SBC","21","22gm","cotton","12","S,M,L","welcome1"));
        productList.add(new Product("1212","Black","SBC","21","22gm","cotton","12","S,M,L","welcome2"));
        productList.add(new Product("1212","Red","SBC","21","22gm","cotton","12","S,M,L","welcome3"));
        productList.add(new Product("1212","Pink","SBC","21","22gm","cotton","12","S,M,L","welcome1"));
        vPager = findViewById(R.id.home_view_pager);
        topItemAdapter = new TopItemAdapter(productList);
        vPager.setAdapter(topItemAdapter);
        // setting animation for viewing viewpager as motion layout
        vPager.setClipToPadding(false);
        vPager.setClipChildren(false);
        vPager.setOffscreenPageLimit(3);
        vPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        vPager.setPageTransformer(compositePageTransformer);
//        FirebaseDatabase.getInstance().getReference("products")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    private void validateUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(this, AuthenticationActivity.class));
            finish();
        }
    }
}
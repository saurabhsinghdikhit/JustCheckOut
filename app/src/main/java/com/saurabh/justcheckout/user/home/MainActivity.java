package com.saurabh.justcheckout.user.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saurabh.justcheckout.admin.ProductListActivity;
import com.saurabh.justcheckout.user.CartActivity;
import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.user.adapters.AllProductsAdapter;
import com.saurabh.justcheckout.user.adapters.MostPopularItemAdapter;
import com.saurabh.justcheckout.user.adapters.TopItemAdapter;
import com.saurabh.justcheckout.user.authentication.AuthenticationActivity;
import com.saurabh.justcheckout.user.classes.Cart;
import com.saurabh.justcheckout.user.classes.Product;
import com.saurabh.justcheckout.user.introduction.WelcomeScreen;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements TopItemAdapter.ITopItemClickInterface,AllProductsAdapter.IAllProductClickInterface,MostPopularItemAdapter.IMostPopularInterface{
    RadioButton homeRadio;
    ViewPager2 vPager;
    private RecyclerView.Adapter topItemAdapter;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Product> mostProductList = new ArrayList<>();
    RecyclerView allProductRecycler;
    RecyclerView.Adapter allProductAdapter;
    RecyclerView.LayoutManager allProductlayoutManager;
    ArrayList<Product> allProductList = new ArrayList<>();
    ArrayList<Product> productList = new ArrayList<>();
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    CardView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        validateUser();
        // for most popular recyclerview
        recyclerView = findViewById(R.id.most_popular_recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // for all product list recycler view
        allProductRecycler = findViewById(R.id.all_products);
        allProductlayoutManager = new LinearLayoutManager(this);
        allProductRecycler.setLayoutManager(allProductlayoutManager);
        ImageView cartBtn = findViewById(R.id.btn_main_cart);
        homeRadio = findViewById(R.id.radio_home);
        homeRadio.setChecked(true);
        cartBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });
        fetchPopularItem();
        fetchMostPopularItems();
        drawerLayout();
    }

    private void drawerLayout(){
        drawerLayout = findViewById(R.id.drawerLayout);
        content = findViewById(R.id.content);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
            float scaleFactor = 5f;
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
                content.setScaleX( 1 - slideOffset / scaleFactor);
                content.setScaleY(1 - slideOffset / scaleFactor);
                content.setCardElevation(100f);
                content.setRadius(60f);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                content.setCardElevation(0f);
                content.setRadius(0f);
            }
        };
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.setDrawerElevation(0f);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        findViewById(R.id.menu_icon).setOnClickListener(l -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
        ((RadioButton)findViewById(R.id.radio_cart)).setOnClickListener(click->{
            startActivity(new Intent(MainActivity.this,CartActivity.class));
            drawerLayout.close();
        });
        ((RadioButton)findViewById(R.id.radio_logout)).setOnClickListener(click->{
            FirebaseAuth.getInstance().signOut();
            SharedPreferences sharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", "");
            editor.apply();
            drawerLayout.close();
            finishAffinity();
            startActivity(new Intent(MainActivity.this,AuthenticationActivity.class));
        });
    }

    private void fetchMostPopularItems() {
        allProductAdapter = new AllProductsAdapter(allProductList,this::allProductItemClick);
        mAdapter = new MostPopularItemAdapter(mostProductList,this::mostPopularItemClick);
        FirebaseDatabase.getInstance().getReference("products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mostProductList.clear();
                        allProductList.clear();
                        for (DataSnapshot childNode : snapshot.getChildren()) {
                            mostProductList.add(childNode.getValue(Product.class));
                            allProductList.add(childNode.getValue(Product.class));
                        }
                        // most popular
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        // all product
                        allProductRecycler.setAdapter(allProductAdapter);
                        allProductAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchPopularItem() {
        vPager = findViewById(R.id.home_view_pager);
        vPager.setClipToPadding(false);
        vPager.setClipChildren(false);
        vPager.setOffscreenPageLimit(3);
        vPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1-Math.abs(position);
            page.setScaleY(0.85f+r*0.15f);
        });
        vPager.setPageTransformer(compositePageTransformer);
        topItemAdapter = new TopItemAdapter(productList,this::topItemClick);
        FirebaseDatabase.getInstance().getReference("products").orderByChild("topPic").equalTo(true)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        for (DataSnapshot childNode : snapshot.getChildren()) {
                            productList.add(childNode.getValue(com.saurabh.justcheckout.user.classes.Product.class));
                        }
                        vPager.setAdapter(topItemAdapter);
                        topItemAdapter.notifyDataSetChanged();
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
            finishAffinity();
            startActivity(new Intent(this, AuthenticationActivity.class));
        }else{
            SharedPreferences userData = this.getSharedPreferences("userLogin", MODE_PRIVATE);
            if(userData!=null){
                String name = userData.getString("name", "");
                String userType = userData.getString("userType","");
                if(name.equalsIgnoreCase(""))
                {
                    finishAffinity();
                    startActivity(new Intent(this, AuthenticationActivity.class));

                }else{
                    ((TextView)findViewById(R.id.profileName)).setText("Welcome\n"+name);
                }
            }else{
                finishAffinity();
                startActivity(new Intent(this, AuthenticationActivity.class));
            }
        }
    }
    private void setCartItem(ArrayList<Product> passedProductList,int position){
        FirebaseDatabase.getInstance().getReference("carts/"+FirebaseAuth.getInstance().getUid()+"/items").orderByChild("productId").equalTo(passedProductList.get(position).getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount()==0){
                            // user has no cart items
                            Cart cart = new Cart(passedProductList.get(position).getId(), 1,
                                    passedProductList.get(position).getSize().split(",")[0].trim());
                            FirebaseDatabase.getInstance()
                                    .getReference("carts/"+FirebaseAuth.getInstance().getUid()+"/items/")
                                    .child(cart.getProductId())
                                    .setValue(cart).addOnSuccessListener(listener->{
                                        Toast.makeText(getApplicationContext(),"This item has added into your cart",Toast.LENGTH_SHORT).show();
                                    }).addOnFailureListener(failure->{
                                        Toast.makeText(getApplicationContext(),failure.getMessage(),Toast.LENGTH_SHORT).show();
                                    });
                        }else{
                            Toast.makeText(getApplicationContext(),"This item is already in cart",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onResume() {
        super.onResume();
        homeRadio = findViewById(R.id.radio_home);
        homeRadio.setChecked(true);
    }

    @Override
    public void topItemClick(int position) {
        setCartItem(productList,position);
    }

    @Override
    public void allProductItemClick(int position) {
        setCartItem(allProductList,position);
    }

    @Override
    public void mostPopularItemClick(int position) {
        setCartItem(mostProductList,position);
    }
}
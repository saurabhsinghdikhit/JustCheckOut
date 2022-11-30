package com.saurabh.justcheckout.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.admin.classes.Product;
import com.saurabh.justcheckout.admin.classes.ProductListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout adminProductListProgressBar,add_product;
    ArrayList<Product> productList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        recyclerView = findViewById(R.id.product_list_recycler_view);
        adminProductListProgressBar = findViewById(R.id.adminProductListProgressBar);
        add_product = findViewById(R.id.add_product);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adminProductListProgressBar.setVisibility(View.VISIBLE);
        add_product.setOnClickListener(View ->{
            startActivity(new Intent(this,CreateProductActivity.class));
        });
        FirebaseDatabase.getInstance().getReference("products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        adminProductListProgressBar.setVisibility(View.GONE);
                        for (DataSnapshot childNode : snapshot.getChildren()) {
                            productList.add(childNode.getValue(Product.class));
                        }
                        mAdapter = new ProductListAdapter(productList);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        adminProductListProgressBar.setVisibility(View.GONE);
                        Toast.makeText(ProductListActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
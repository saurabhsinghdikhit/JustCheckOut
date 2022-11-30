package com.saurabh.justcheckout.user.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.admin.CreateProductActivity;
import com.saurabh.justcheckout.user.ProductDetailsActivity;
import com.saurabh.justcheckout.user.classes.Product;

import java.util.ArrayList;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.MyViewHolder>{
    private final ArrayList<Product> products;
    public AllProductsAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public AllProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new AllProductsAdapter.MyViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductsAdapter.MyViewHolder holder, int position) {

        String image = products.get(position).getImageUrl();
        Context actContext = holder.itemView.getContext();
        FirebaseStorage.getInstance().getReference()
                .child("products/"+image).getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(actContext).load(uri).placeholder(R.drawable.bag).error(R.drawable.just_check_out).into(holder.imgPic);
                }).addOnFailureListener(e->{
                    Toast.makeText(actContext,"Image error",Toast.LENGTH_SHORT).show();
                });
        holder.name.setText(products.get(position).getName().toUpperCase());
        holder.size.setText(products.get(position).getSize());
        holder.product_list_price.setText(products.get(position).getPrice().toString());
        holder.all_product_list_item.setOnClickListener(view -> {
            Intent intent = new Intent(actContext, ProductDetailsActivity.class);
            intent.putExtra("productId", products.get(position).getId());
            actContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPic;
        TextView name,size,product_list_price;
        CardView all_product_list_item;
        public MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.all_products_item, parent, false));
            imgPic = itemView.findViewById(R.id.all_product_image);
            name = itemView.findViewById(R.id.all_product_name);
            size = itemView.findViewById(R.id.all_product_sizes);
            product_list_price = itemView.findViewById(R.id.all_product_price);
            all_product_list_item = itemView.findViewById(R.id.all_product_list_item);
        }
    }
}

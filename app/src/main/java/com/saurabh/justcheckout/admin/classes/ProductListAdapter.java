package com.saurabh.justcheckout.admin.classes;

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

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder>{
    private final ArrayList<com.saurabh.justcheckout.admin.classes.Product> products;
    AlertDialog.Builder builder;
    public ProductListAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ProductListAdapter.MyViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.MyViewHolder holder, int position) {

        String image = products.get(position).getImageUrl();
        Context actContext = holder.itemView.getContext();
        builder = new AlertDialog.Builder(actContext);
        Glide.with(actContext).load(image).placeholder(R.drawable.bag).error(R.drawable.just_check_out).into(holder.imgPic);
        holder.name.setText(products.get(position).getName().toUpperCase());
        holder.size.setText(products.get(position).getSize());
        holder.product_list_price.setText(products.get(position).getPrice().toString());
        if (products.get(position).getTopPic())
            holder.pinned_item.setImageDrawable(actContext.getDrawable(R.drawable.ic_pin));
        else
            holder.pinned_item.setImageDrawable(actContext.getDrawable(R.drawable.ic_pin_light));
        holder.pinned_item.setOnClickListener(view -> {
            String message =!products.get(position).getTopPic()?"This product will be shown in top pics section"
                    :"This product will be removed from top pic section";
            FirebaseDatabase.getInstance().getReference("products")
                    .child(products.get(position).getId()).child("topPic").setValue(!products.get(position).getTopPic())
                    .addOnSuccessListener(l->{
                        Toast.makeText(actContext,message,Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e->{
                        Toast.makeText(actContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                    });
        });
        holder.deleteItem.setOnClickListener(view -> {
            builder.setTitle("Do you want to remove this product?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",(dialogInterface, i) -> {
                        FirebaseDatabase.getInstance().getReference("products")
                                .child(products.get(position).getId()).removeValue()
                                .addOnSuccessListener(l->{
                                    Toast.makeText(actContext,"Product has been deleted",Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e->{
                                    Toast.makeText(actContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                                });
                    }).setNegativeButton("No",(dialogInterface, i) -> {
                        dialogInterface.cancel();
                    });
            builder.create().show();
        });
        holder.admin_list_item.setOnClickListener(view -> {
            Intent intent = new Intent(actContext, CreateProductActivity.class);
            intent.putExtra("productId", products.get(position).getId());
            actContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPic,pinned_item,deleteItem;
        TextView name,size,product_list_price;
        CardView admin_list_item;
        public MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.admin_product_list_item, parent, false));
            imgPic = itemView.findViewById(R.id.admin_product_list_image);
            name = itemView.findViewById(R.id.admin_product_list_name);
            size = itemView.findViewById(R.id.admin_product_list_size);
            product_list_price = itemView.findViewById(R.id.product_list_price);
            pinned_item = itemView.findViewById(R.id.pinned_item);
            deleteItem = itemView.findViewById(R.id.delete_item);
            admin_list_item = itemView.findViewById(R.id.admin_list_item);
        }
    }
}

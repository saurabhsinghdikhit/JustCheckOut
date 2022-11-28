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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.admin.CreateProductActivity;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder>{
    private final ArrayList<com.saurabh.justcheckout.admin.classes.Product> products;
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
        //int imageId = actContext.getResources().getIdentifier(image, "drawable", actContext.getPackageName());
        //holder.imgPic.setImageResource(imageId);
        holder.name.setText(products.get(position).getName().toUpperCase());
        holder.size.setText(products.get(position).getSize());
        holder.pinned_item.setOnClickListener(view -> {
            Log.i("pinned","pim");
        });
        holder.deleteItem.setOnClickListener(view -> {
            Log.i("delete","delete");
        });
        holder.admin_list_item.setOnClickListener(view -> {
            actContext.startActivity(new Intent(actContext, CreateProductActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPic,pinned_item,deleteItem;
        TextView name,size;
        CardView admin_list_item;
        public MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.admin_product_list_item, parent, false));
            imgPic = itemView.findViewById(R.id.admin_product_list_image);
            name = itemView.findViewById(R.id.admin_product_list_name);
            size = itemView.findViewById(R.id.admin_product_list_size);
            pinned_item = itemView.findViewById(R.id.pinned_item);
            deleteItem = itemView.findViewById(R.id.delete_item);
            admin_list_item = itemView.findViewById(R.id.admin_list_item);
        }
    }
}

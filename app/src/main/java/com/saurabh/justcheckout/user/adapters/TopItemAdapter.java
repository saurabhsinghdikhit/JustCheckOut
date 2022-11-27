package com.saurabh.justcheckout.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.user.classes.Product;

import java.util.ArrayList;

public class TopItemAdapter extends RecyclerView.Adapter<TopItemAdapter.MyViewHolder>{
    private ArrayList<Product> products;

    public TopItemAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public TopItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new TopItemAdapter.MyViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TopItemAdapter.MyViewHolder holder, int position) {
        String image = products.get(position).getImageUrl();
        Context actContext = holder.itemView.getContext();
        int imageId = actContext.getResources().getIdentifier(image, "drawable", actContext.getPackageName());
        holder.imgPic.setImageResource(imageId);
        holder.name.setText(products.get(position).getName());
        holder.price.setText(products.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPic;
        TextView name,price;
        public MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.home_screen_view_pager_layout, parent, false));
            imgPic = itemView.findViewById(R.id.topPickImage);
            name = itemView.findViewById(R.id.topPickName);
            price = itemView.findViewById(R.id.topPickPrice);
        }
    }
}

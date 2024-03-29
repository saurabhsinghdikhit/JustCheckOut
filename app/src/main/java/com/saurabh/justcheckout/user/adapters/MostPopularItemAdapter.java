package com.saurabh.justcheckout.user.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.user.ProductDetailsActivity;
import com.saurabh.justcheckout.user.classes.Product;

import java.util.ArrayList;

public class MostPopularItemAdapter extends RecyclerView.Adapter<MostPopularItemAdapter.MyViewHolder>{
    private ArrayList<Product> products;
    private IMostPopularInterface iMostPopularInterface;
    public MostPopularItemAdapter(ArrayList<Product> products,IMostPopularInterface iMostPopularInterface) {
        this.products = products;
        this.iMostPopularInterface = iMostPopularInterface;
    }

    @NonNull
    @Override
    public MostPopularItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MostPopularItemAdapter.MyViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MostPopularItemAdapter.MyViewHolder holder, int position) {
        String image = products.get(position).getImageUrl();
        Context actContext = holder.itemView.getContext();
        Glide.with(actContext).load(image).placeholder(R.drawable.bag).error(R.drawable.just_check_out).into(holder.imgPic);
        holder.name.setText(products.get(position).getName());
        holder.price.setText(products.get(position).getPrice().toString());
        holder.mostPopularLayout.setOnClickListener(view -> {
            Intent intent = new Intent(actContext, ProductDetailsActivity.class);
            intent.putExtra("productId", products.get(position).getId());
            actContext.startActivity(intent);
        });
        holder.mostPopularCart.setOnClickListener(click->{
            iMostPopularInterface.mostPopularItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPic,mostPopularCart;
        TextView name,price;
        CardView mostPopularLayout;
        public MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.most_popular_items, parent, false));
            imgPic = itemView.findViewById(R.id.most_popular_image);
            name = itemView.findViewById(R.id.most_popular_name);
            price = itemView.findViewById(R.id.most_popular_price);
            mostPopularLayout = itemView.findViewById(R.id.most_popular_layout);
            mostPopularCart = itemView.findViewById(R.id.mostPopularCart);
        }
    }
    public interface IMostPopularInterface {
        void mostPopularItemClick(int position);
    }
}

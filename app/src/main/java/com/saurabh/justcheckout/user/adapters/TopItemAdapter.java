package com.saurabh.justcheckout.user.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class TopItemAdapter extends RecyclerView.Adapter<TopItemAdapter.MyViewHolder>{
    private ArrayList<Product> products;
    private ITopItemClickInterface iTopItemClickInterface;
    public TopItemAdapter(ArrayList<Product> products,ITopItemClickInterface iTopItemClickInterface) {
        this.products = products;
        this.iTopItemClickInterface = iTopItemClickInterface;
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
        FirebaseStorage.getInstance().getReference()
                .child("products/"+image).getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(actContext).load(uri).placeholder(R.drawable.bag).error(R.drawable.just_check_out).into(holder.imgPic);
                }).addOnFailureListener(e->{
                    Toast.makeText(actContext,"Image error",Toast.LENGTH_SHORT).show();
                });
        holder.name.setText(products.get(position).getName());
        holder.price.setText(products.get(position).getPrice().toString());
        holder.roundCardView.setOnClickListener(view -> {
            Intent intent = new Intent(actContext, ProductDetailsActivity.class);
            intent.putExtra("productId", products.get(position).getId());
            actContext.startActivity(intent);
        });
        holder.topPickCart.setOnClickListener(view->{
            iTopItemClickInterface.topItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPic,topPickCart;
        TextView name,price;
        CardView roundCardView;
        public MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.home_screen_view_pager_layout, parent, false));
            imgPic = itemView.findViewById(R.id.topPickImage);
            name = itemView.findViewById(R.id.topPickName);
            price = itemView.findViewById(R.id.topPickPrice);
            roundCardView = itemView.findViewById(R.id.roundCardView);
            topPickCart = itemView.findViewById(R.id.topPickCart);
        }
    }
    public interface ITopItemClickInterface {
        void topItemClick(int position);
    }
}

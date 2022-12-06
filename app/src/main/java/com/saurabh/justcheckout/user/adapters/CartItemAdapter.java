package com.saurabh.justcheckout.user.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.admin.CreateProductActivity;
import com.saurabh.justcheckout.admin.ProductListActivity;
import com.saurabh.justcheckout.user.ProductDetailsActivity;
import com.saurabh.justcheckout.user.classes.Cart;
import com.saurabh.justcheckout.user.classes.Product;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder>{
    private final ArrayList<Cart> cartProducts;
    private final ArrayList<Product> boundedProduct;
    AlertDialog.Builder builder;
    public CartItemAdapter(ArrayList<Cart> cartProducts,ArrayList<Product> boundedProduct) {
        this.cartProducts = cartProducts;
        this.boundedProduct = boundedProduct;
    }
    @NonNull
    @Override
    public CartItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CartItemAdapter.MyViewHolder(layoutInflater, parent);
    }
    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.MyViewHolder holder, int position) {
        Context actContext = holder.itemView.getContext();
        builder = new AlertDialog.Builder(actContext);
        holder.cartItemName.setText(boundedProduct.get(position).getName());
        holder.cartItemPrice.setText(boundedProduct.get(position).getPrice().toString());
        holder.cartItemSize.setText(cartProducts.get(position).getSize());
        holder.cartItemQuantity.setText(String.valueOf(cartProducts.get(position).getQuantity()));
        Glide.with(actContext).load(boundedProduct.get(position).getImageUrl()).placeholder(R.drawable.bag).error(R.drawable.just_check_out).into(holder.cartItemImage);
        holder.cartItemAdd.setOnClickListener(click->{
            if(cartProducts.get(position).getQuantity()+1<=boundedProduct.get(position).getQuantity()){
                Cart newCart = cartProducts.get(position);
                newCart.setQuantity(newCart.getQuantity()+1);
                FirebaseDatabase.getInstance().getReference("carts/"+ FirebaseAuth.getInstance().getUid()+"/items/")
                        .child(boundedProduct.get(position).getId()).setValue(newCart)
                        .addOnFailureListener(e->{
                            Toast.makeText(actContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                        });
            }else{
                Toast.makeText(actContext,"We have only "+boundedProduct.get(position).getQuantity()+" quantity of this product",Toast.LENGTH_SHORT).show();
            }

        });
        holder.cartItemMinus.setOnClickListener(click->{
            if(cartProducts.get(position).getQuantity()-1>0){
                Cart newCart = cartProducts.get(position);
                newCart.setQuantity(newCart.getQuantity()-1);
                FirebaseDatabase.getInstance().getReference("carts/"+ FirebaseAuth.getInstance().getUid()+"/items/")
                        .child(boundedProduct.get(position).getId()).setValue(newCart)
                        .addOnFailureListener(e->{
                            Toast.makeText(actContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                        });
            }else{
                holder.cartItemRemove.callOnClick();
            }

        });
        holder.cartItemRemove.setOnClickListener(click->{
            builder.setTitle("Do you want to remove this product from your cart?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",(dialogInterface, i) -> {
                        FirebaseDatabase.getInstance().getReference("carts/"+ FirebaseAuth.getInstance().getUid()+"/items/")
                                .child(boundedProduct.get(position).getId())
                                .removeValue()
                                .addOnSuccessListener(l->{
                                    Toast.makeText(actContext,"Product has been removed",Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e->{
                                    Toast.makeText(actContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                                });
                    }).setNegativeButton("No",(dialogInterface, i) -> {
                        dialogInterface.cancel();
                    });
            builder.create().show();
        });
    }
    @Override
    public int getItemCount() {
        return boundedProduct.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView cartItemImage;
        TextView cartItemPrice,cartItemSize,cartItemName,cartItemQuantity;
        ImageButton cartItemMinus,cartItemAdd,cartItemRemove;

        public MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.cart_items, parent, false));
            cartItemImage = itemView.findViewById(R.id.cartItemImage);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
            cartItemSize = itemView.findViewById(R.id.cartItemSize);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemMinus = itemView.findViewById(R.id.cartItemMinus);
            cartItemAdd = itemView.findViewById(R.id.cartItemAdd);
            cartItemQuantity = itemView.findViewById(R.id.cartItemQuantity);
            cartItemRemove = itemView.findViewById(R.id.cartItemRemove);
        }
    }
}

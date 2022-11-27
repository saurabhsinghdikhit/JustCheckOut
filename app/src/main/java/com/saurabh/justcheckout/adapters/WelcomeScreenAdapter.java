package com.saurabh.justcheckout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saurabh.justcheckout.R;

import java.util.ArrayList;

public class WelcomeScreenAdapter extends RecyclerView.Adapter<WelcomeScreenAdapter.MyViewHolder>{
    private ArrayList<String> images;

    public WelcomeScreenAdapter(ArrayList<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public WelcomeScreenAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull WelcomeScreenAdapter.MyViewHolder holder, int position) {
        String image = images.get(position);
        Context actContext = holder.itemView.getContext();
        int imageId = actContext.getResources().getIdentifier(image, "drawable", actContext.getPackageName());
        holder.imgPic.setImageResource(imageId);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPic;
        public MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.welcome_screen_view_pager, parent, false));
            imgPic = itemView.findViewById(R.id.pagerImage);
        }



    }
}

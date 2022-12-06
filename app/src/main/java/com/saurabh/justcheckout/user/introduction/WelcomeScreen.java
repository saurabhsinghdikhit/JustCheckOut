package com.saurabh.justcheckout.user.introduction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.user.adapters.WelcomeScreenAdapter;
import com.saurabh.justcheckout.user.authentication.AuthenticationActivity;

import java.util.ArrayList;

public class WelcomeScreen extends AppCompatActivity {
    ViewPager2 vPager;
    Button welcomeBtn;
    ArrayList<String> imageList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_welcome);
        imageList.add("welcome1");
        imageList.add("welcome2");
        imageList.add("welcome3");
        vPager = findViewById(R.id.welcome_view_pager);
        welcomeBtn = findViewById(R.id.welcomeButton);
        RecyclerView.Adapter welcomeScreenAdapter = new WelcomeScreenAdapter(imageList);
        vPager.setAdapter(welcomeScreenAdapter);
        // setting animation for viewing viewpager as motion layout
        vPager.setClipToPadding(false);
        vPager.setClipChildren(false);
        vPager.setOffscreenPageLimit(3);
        vPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        vPager.setPageTransformer(compositePageTransformer);
        welcomeBtn.setOnClickListener(view -> {
            if(vPager.getCurrentItem()>=2){
                startActivity(new Intent(WelcomeScreen.this, AuthenticationActivity.class));
                finish();
            }else{
                vPager.setCurrentItem(vPager.getCurrentItem() + 1);
            }
        });
        vPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position<2){
                    welcomeBtn.setText("Next");
                }else{
                    welcomeBtn.setText("Login");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }
}
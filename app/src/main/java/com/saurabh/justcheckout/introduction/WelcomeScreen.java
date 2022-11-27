package com.saurabh.justcheckout.introduction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.adapters.WelcomeScreenAdapter;
import com.saurabh.justcheckout.classes.ViewPagerAdapter;

import java.util.ArrayList;

public class WelcomeScreen extends AppCompatActivity {
    ViewPager2 vPager;
    private RecyclerView.Adapter welcomeScreenAdapter;
    ArrayList<String> imageList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_welcome);
        imageList.add("welcome1");
        imageList.add("welcome2");
        imageList.add("welcome3");
        vPager = findViewById(R.id.welcome_view_pager);
        welcomeScreenAdapter = new WelcomeScreenAdapter(imageList);
        vPager.setAdapter(welcomeScreenAdapter);
    }
}
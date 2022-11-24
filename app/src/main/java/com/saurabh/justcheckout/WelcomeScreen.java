package com.saurabh.justcheckout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.saurabh.justcheckout.classes.ViewPagerAdapter;

public class WelcomeScreen extends AppCompatActivity {
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_welcome);
        viewPager = findViewById(R.id.welcome_view_pager);

        // setting up the adapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // add the fragments
        viewPagerAdapter.add(new FragmentOne(), "Page 1");
        viewPagerAdapter.add(new FragmentSecond(), "Page 2");
        viewPagerAdapter.add(new FragmentThree(), "Page 3");

        // Set the adapter
        viewPager.setAdapter(viewPagerAdapter);
    }
}
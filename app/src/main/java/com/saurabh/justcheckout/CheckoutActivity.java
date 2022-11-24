package com.saurabh.justcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.os.Bundle;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        // load checkout fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.checkoutFrameLayout,new CheckoutScreenFragment()).commit();
    }
}
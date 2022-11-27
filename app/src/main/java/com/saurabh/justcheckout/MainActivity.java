package com.saurabh.justcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView cartBtn = findViewById(R.id.btn_main_cart);
        cartBtn.setOnClickListener(view ->  {
            startActivity(new Intent(MainActivity.this,CartActivity.class));
        });
    }
}
package com.saurabh.justcheckout.user.introduction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.saurabh.justcheckout.admin.CreateProductActivity;
import com.saurabh.justcheckout.admin.ProductListActivity;
import com.saurabh.justcheckout.user.home.MainActivity;
import com.saurabh.justcheckout.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences userData = this.getSharedPreferences("userLogin", MODE_PRIVATE);
        if(userData!=null){
            String name = userData.getString("name", "");
            String userType = userData.getString("userType","");
            if(name.equalsIgnoreCase(""))
            {
                 setActivity(new WelcomeScreen());

            }else{
                if(userType.equals("admin"))
                    setActivity(new ProductListActivity());
                else
                    setActivity(new MainActivity());
            }
        }else{
            setActivity(new WelcomeScreen());
        }

    }
    void setActivity(Activity activity){
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, activity.getClass()));
            finish();
        },3000);
    }
}
package com.saurabh.justcheckout.user.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.saurabh.justcheckout.R;


public class AuthenticationActivity extends AppCompatActivity {
    TextView loginText,registerText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        loginText = findViewById(R.id.login);
        registerText = findViewById(R.id.register);
        setFragment(new LoginFragment(),true);
        loginText.setOnClickListener(view -> setFragment(new LoginFragment(),true));
        registerText.setOnClickListener(view -> setFragment(new RegisterFragment(),false));
    }
    private void setFragment(Fragment fragment,boolean isLogin){
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        isLogin?R.anim.slide_in_left:R.anim.slide_in_right,
                        isLogin?R.anim.slide_out_right:R.anim.slide_out_left)
                .replace(R.id.loginFrame,fragment).commit();
    }
}
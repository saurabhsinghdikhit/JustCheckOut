package com.saurabh.justcheckout.user.checkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.admin.CreateProductActivity;
import com.saurabh.justcheckout.admin.ProductListActivity;
import com.saurabh.justcheckout.user.CartActivity;
import com.saurabh.justcheckout.user.classes.Cart;
import com.saurabh.justcheckout.user.home.MainActivity;
import com.saurabh.justcheckout.user.introduction.SplashActivity;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class CheckoutActivity extends AppCompatActivity {
    String emailPtr = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    String mobile = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";
    String postalRegex = "/^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d$/";
    Pattern emailPattern = Pattern.compile(emailPtr);
    Pattern mobilePattern = Pattern.compile(mobile);
    Pattern postalPattern = Pattern.compile(postalRegex);
    TextView totalAmountToPay;
    ImageView checkout_back_button,order_placed;
    EditText name,phone,email,street,apartment,city,
            country,postalCode,cardNumber,nameOnCard,expiryMonth,expiryYear,CVV;
    Spinner province;
    Button btn_checkout;
    ArrayList<Cart> cartItems = new ArrayList<>();
    LinearLayout amount_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        initializeControls();
        totalAmountToPay.setText(getIntent().getStringExtra("amountToPay"));
        checkout_back_button.setOnClickListener(click-> super.onBackPressed());
        btn_checkout.setOnClickListener(click-> validateUserInput());
    }

    private void validateUserInput() {
        if(TextUtils.isEmpty(name.getText().toString().trim())){
            name.setText(name.getText().toString().trim());
            name.setError("Name is required");
            return;
        }
        if(TextUtils.isEmpty(email.getText().toString().trim())){
            email.setText(email.getText().toString().trim());
            email.setError("Email is required");
            return;
        }
        if(!emailPattern.matcher(email.getText().toString().trim()).matches()){
            email.setText(email.getText().toString().trim());
            email.setError("Email is not valid");
            return;
        }
        if(TextUtils.isEmpty(phone.getText().toString().trim())){
            phone.setText(phone.getText().toString().trim());
            phone.setError("Mobile is required");
            return;
        }
        if(!mobilePattern.matcher(phone.getText().toString().trim()).matches()){
            phone.setText(phone.getText().toString().trim());
            phone.setError("Mobile is not valid");
            return;
        }
        if(TextUtils.isEmpty(street.getText().toString().trim())){
            street.setText(street.getText().toString().trim());
            street.setError("Street is required");
            return;
        }
        if(TextUtils.isEmpty(apartment.getText().toString().trim())){
            apartment.setText(apartment.getText().toString().trim());
            apartment.setError("Apartment is required");
            return;
        }
        if(TextUtils.isEmpty(city.getText().toString().trim())){
            city.setText(city.getText().toString().trim());
            city.setError("Apartment is required");
            return;
        }
        if(TextUtils.isEmpty(postalCode.getText().toString().trim())){
            postalCode.setText(postalCode.getText().toString().trim());
            postalCode.setError("PostalCode is required");
            return;
        }
        if(postalCode.getText().toString().trim().length()!=6){
            postalCode.setText(postalCode.getText().toString().trim());
            postalCode.setError("PostalCode should be 6 character");
            return;
        }
        if(TextUtils.isEmpty(cardNumber.getText().toString().trim())){
            cardNumber.setText(cardNumber.getText().toString().trim());
            cardNumber.setError("Card Number is required");
            return;
        }
        if(cardNumber.getText().toString().trim().length()!=16){
            cardNumber.setText(cardNumber.getText().toString().trim());
            cardNumber.setError("Card Number should be 16 number");
            return;
        }
        if(TextUtils.isEmpty(nameOnCard.getText().toString().trim())){
            nameOnCard.setText(nameOnCard.getText().toString().trim());
            nameOnCard.setError("Name is required");
            return;
        }
        if(TextUtils.isEmpty(nameOnCard.getText().toString().trim())){
            nameOnCard.setText(nameOnCard.getText().toString().trim());
            nameOnCard.setError("Name on card is required");
            return;
        }
        if(TextUtils.isEmpty(expiryMonth.getText().toString().trim())){
            expiryMonth.setText(expiryMonth.getText().toString().trim());
            expiryMonth.setError("Expiry Month is required");
            return;
        }
        if(Integer.parseInt(expiryMonth.getText().toString().trim())<1 || Integer.parseInt(expiryMonth.getText().toString().trim())>12){
            expiryMonth.setText(expiryMonth.getText().toString().trim());
            expiryMonth.setError("Expiry Month is invalid");
            return;
        }
        if(Integer.parseInt(expiryMonth.getText().toString().trim()) < Calendar.getInstance().get(Calendar.MONTH)
            && Integer.parseInt(expiryYear.getText().toString().trim())== Calendar.getInstance().get(Calendar.YEAR)){
            expiryMonth.setText(expiryMonth.getText().toString().trim());
            expiryMonth.setError("Expiry Month is invalid");
            return;
        }
        if(TextUtils.isEmpty(expiryYear.getText().toString().trim())){
            expiryYear.setText(expiryYear.getText().toString().trim());
            expiryYear.setError("Expiry Year is required");
            return;
        }
        if(Integer.parseInt(expiryYear.getText().toString().trim())< Calendar.getInstance().get(Calendar.YEAR)){
            expiryYear.setText(expiryYear.getText().toString().trim());
            expiryYear.setError("Expiry Year is invalid");
            return;
        }
        if(TextUtils.isEmpty(CVV.getText().toString().trim())){
            CVV.setText(CVV.getText().toString().trim());
            CVV.setError("CVV is required");
            return;
        }
        if(CVV.getText().toString().trim().length()!=3){
            CVV.setText(CVV.getText().toString().trim());
            CVV.setError("CVV is not valid");
            return;
        }
        checkOutForUser();
    }

    private void checkOutForUser() {
        getCartItems();
    }

    private void getCartItems() {
        cartItems.clear();
        FirebaseDatabase.getInstance().getReference("carts/"+ FirebaseAuth.getInstance().getUid()+"/items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childNode : snapshot.getChildren()) {
                            cartItems.add(childNode.getValue(Cart.class));
                        }
                        addDataToRecords();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CheckoutActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addDataToRecords() {
        FirebaseDatabase.getInstance().getReference("orderHistory").child(FirebaseAuth.getInstance().getUid())
                .child("items").setValue(cartItems)
                .addOnSuccessListener(OnSuccessListener->{
                    FirebaseDatabase.getInstance().getReference("carts")
                            .child(FirebaseAuth.getInstance().getUid()).removeValue()
                                .addOnSuccessListener(l->{
                                amount_layout.setVisibility(View.GONE);
                                order_placed.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(() -> {
                                    startActivity(new Intent(CheckoutActivity.this, MainActivity.class));
                                    finishAffinity();
                                },3000);
                            }).addOnFailureListener(e->{
                                Toast.makeText(CheckoutActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(OnFailureListener->{
                    Toast.makeText(CheckoutActivity.this,OnFailureListener.getMessage(),Toast.LENGTH_SHORT).show();
                });
    }

    private void initializeControls() {
        totalAmountToPay = findViewById(R.id.totalAmountToPay);
        checkout_back_button = findViewById(R.id.checkout_back_button);
        name = findViewById(R.id.checkout_name);
        phone = findViewById(R.id.checkout_phone);
        email = findViewById(R.id.checkout_email);
        street = findViewById(R.id.checkout_street);
        apartment = findViewById(R.id.checkout_apartment);
        city = findViewById(R.id.checkout_city);
        province = findViewById(R.id.checkout_province);
        country = findViewById(R.id.checkout_country);
        postalCode = findViewById(R.id.checkout_postalCode);
        cardNumber = findViewById(R.id.checkout_cardNumber);
        nameOnCard = findViewById(R.id.checkout_name_on_card);
        expiryMonth = findViewById(R.id.checkout_expiry_month);
        expiryYear = findViewById(R.id.checkout_expiry_year);
        CVV = findViewById(R.id.checkout_cvv);
        order_placed = findViewById(R.id.order_placed);
        btn_checkout = findViewById(R.id.btn_checkout);
        amount_layout = findViewById(R.id.amount_layout);
        SharedPreferences userData = this.getSharedPreferences("userLogin", MODE_PRIVATE);
        if(userData!=null) {
            String name = userData.getString("name", "");
            String phone = userData.getString("phone", "");
            String email = userData.getString("email","");
            this.name.setText(name);
            this.phone.setText(phone);
            this.email.setText(email);
        }
    }
}
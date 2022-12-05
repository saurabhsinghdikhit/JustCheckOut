package com.saurabh.justcheckout.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.user.classes.Product;
import com.saurabh.justcheckout.user.adapters.CartItemAdapter;
import com.saurabh.justcheckout.user.checkout.CheckoutActivity;
import com.saurabh.justcheckout.user.classes.Cart;
import com.saurabh.justcheckout.user.home.MainActivity;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    ImageView cart_back_button,cartEmpty;
    Button btn_checkout;
    TextView totalAmountToPay,subTotal,taxes,priceWithTaxes,shippingCharges;
    LinearLayout price_layout;
    RecyclerView recyclerViewCartItems;
    RecyclerView.Adapter cartAdapter;
    ArrayList<Cart> cartItems = new ArrayList<>();
    ArrayList<Product> boundedItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initializeElements();
        Button cartBtn = findViewById(R.id.btn_checkout);
        fetchCartItems();
        cartBtn.setOnClickListener(v -> {
            if(boundedItems.size()!=0)
            startActivity(new Intent(CartActivity.this, CheckoutActivity.class).putExtra("amountToPay",totalAmountToPay.getText()));
            else{
                startActivity(new Intent(CartActivity.this, MainActivity.class));
                finishAffinity();
            }

        });
        ImageView cart_back_button = findViewById(R.id.cart_back_button);
        cart_back_button.setOnClickListener(click->{
            super.onBackPressed();
        });
    }

    private void fetchCartItems() {
        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));
        FirebaseDatabase.getInstance().getReference("carts/"+ FirebaseAuth.getInstance().getUid()+"/items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cartItems.clear();
                        for (DataSnapshot childNode : snapshot.getChildren()) {
                            cartItems.add(childNode.getValue(Cart.class));
                        }
                        getProductList(cartItems);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CartActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getProductList(ArrayList<Cart> cartItems){
        boundedItems.clear();
       for (Cart cart:cartItems){
           FirebaseDatabase.getInstance().getReference("products").child(cart.getProductId())
                   .addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           boundedItems.add(snapshot.getValue(Product.class));
                           dataChangeForAdapter();
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           Toast.makeText(CartActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                       }
                   });
       }
       if(cartItems.size()==0){
           dataChangeForAdapter();
       }
    }
    private void dataChangeForAdapter(){
        cartAdapter = new CartItemAdapter(cartItems,boundedItems);
        recyclerViewCartItems.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        changeCartPrices();
    }

    private void changeCartPrices() {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);
        double subTol = 0,taxes= 0,priceAfterTax= 0,shipping= 0,total= 0;
        for (int i = 0 ; i<boundedItems.size();i++){
            subTol += boundedItems.get(i).getPrice()*cartItems.get(i).getQuantity();
        }
        taxes = subTol * 0.13;
        priceAfterTax = subTol + taxes;
        shipping =20.0;
        total=priceAfterTax+shipping;
        subTotal.setText(df.format(subTol));
        this.taxes.setText(df.format(taxes));
        this.priceWithTaxes.setText(df.format(priceAfterTax));
        this.shippingCharges.setText(df.format(shipping));
        this.totalAmountToPay.setText(df.format(total));
        if(boundedItems.size()==0){
            this.btn_checkout.setText("Continue Shopping!");
            this.price_layout.setVisibility(View.GONE);
            this.cartEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void initializeElements() {
        cart_back_button = findViewById(R.id.cart_back_button);
        cartEmpty = findViewById(R.id.cartEmpty);
        btn_checkout = findViewById(R.id.btn_checkout);
        price_layout = findViewById(R.id.price_layout);
        subTotal = findViewById(R.id.subTotal);
        taxes = findViewById(R.id.taxes);
        priceWithTaxes = findViewById(R.id.priceWithTaxes);
        shippingCharges = findViewById(R.id.shippingCharges);
        totalAmountToPay = findViewById(R.id.totalAmountToPay);
        recyclerViewCartItems = findViewById(R.id.recyclerViewCartItems);
    }
}
package com.saurabh.justcheckout.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.admin.classes.Product;
import com.saurabh.justcheckout.user.classes.Cart;
import com.saurabh.justcheckout.user.home.MainActivity;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class ProductDetailsActivity extends AppCompatActivity {
    ImageView backButton,cartButton,productImage;
    TextView productName,productPrice,productDescription,productQuantity,product_detail_material,product_detail_weight;
    RadioButton sizeS,sizeM,sizeL,sizeXL,sizeXXL;
    Button addToCart;
    String productId;
    Product passedProduct;
    LinearLayout productProgressBar;
    RadioGroup radioButtonGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initializeTheControls();
        productId = getIntent().getStringExtra("productId");
        if(productId==null){
            startActivity(new Intent(ProductDetailsActivity.this, MainActivity.class));
            finish();
        }
        else{
            fetchDataByProductId();
        }
        backButton.setOnClickListener(click-> super.onBackPressed());
        cartButton.setOnClickListener(click-> startActivity(new Intent(ProductDetailsActivity.this,CartActivity.class)));
        addToCart.setOnClickListener(click->{
            if(passedProduct.getQuantity()<=0){
                return;
            }
            if(radioButtonGroup.getCheckedRadioButtonId()==-1)
                Toast.makeText(ProductDetailsActivity.this, "Please check size", Toast.LENGTH_SHORT).show();
            else
                addToCart();
        });
    }

    private void fetchDataByProductId() {
        addToCart.setVisibility(View.GONE);
        productProgressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("products").child(productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        populateData(snapshot.getValue(Product.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        productProgressBar.setVisibility(View.GONE);
                        Toast.makeText(ProductDetailsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void populateData(Product product){
        passedProduct = product;
        addToCart.setVisibility(View.VISIBLE);
        productProgressBar.setVisibility(View.GONE);
        productName.setText(product.getName());
        product_detail_material.setText(": "+product.getMaterial());
        productPrice.setText(product.getPrice().toString());
        if(product.getQuantity()>0)
            productQuantity.setText(product.getQuantity()+" items left");
        else{
            addToCart.setText("Out of stock");
            addToCart.setEnabled(false);
            productQuantity.setText("Out of stock!");
            productQuantity.setTextColor(getResources().getColor(R.color.red));
        }

        productDescription.setText(product.getDescription());
        product_detail_weight.setText(": "+product.getWeight());
        String[] sizes = product.getSize().split(",");
        for(int i = 0; i < radioButtonGroup.getChildCount(); i++) {
            View v = radioButtonGroup.getChildAt(i);
            if(v instanceof RadioButton) {
                if(Arrays.asList(sizes).contains(((RadioButton) v).getText()))
                    v.setEnabled(true);
                else
                    v.setVisibility(View.GONE);
            }
        }
        Glide.with(getApplicationContext()).load(passedProduct.getImageUrl()).placeholder(R.drawable.bag).error(R.drawable.just_check_out).into(productImage);
    }
    private void initializeTheControls() {
        backButton = findViewById(R.id.product_detail_back_button);
        cartButton = findViewById(R.id.product_detail_cart_button);
        productImage = findViewById(R.id.product_detail_image);
        productName = findViewById(R.id.product_detail_name);
        productPrice = findViewById(R.id.product_detail_price);
        productDescription = findViewById(R.id.product_detail_description);
        sizeS = findViewById(R.id.product_detail_radio_s);
        sizeM = findViewById(R.id.product_detail_radio_m);
        sizeL = findViewById(R.id.product_detail_radio_l);
        sizeXL = findViewById(R.id.product_detail_radio_xl);
        sizeXXL = findViewById(R.id.product_detail_radio_xxl);
        addToCart = findViewById(R.id.product_detail_add_to_cart);
        productProgressBar = findViewById(R.id.productProgressBar);
        radioButtonGroup = findViewById(R.id.radioButtonGroup);
        productQuantity = findViewById(R.id.product_detail_quantity);
        product_detail_material = findViewById(R.id.product_detail_material);
        product_detail_weight = findViewById(R.id.product_detail_weight);
    }
    private void addToCart(){
        int selectedId = radioButtonGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton)findViewById(selectedId);
        FirebaseDatabase.getInstance().getReference("carts/"+ FirebaseAuth.getInstance().getUid()+"/items").orderByChild("productId").equalTo(passedProduct.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Cart cart = new Cart(passedProduct.getId(), 1,selectedRadioButton.getText().toString());
                        if(snapshot.getChildrenCount()==0){
                            FirebaseDatabase.getInstance()
                                    .getReference("carts/"+FirebaseAuth.getInstance().getUid()+"/items/"+ passedProduct.getId())
                                    .setValue(cart).addOnSuccessListener(listener->{
                                        Toast.makeText(getApplicationContext(),"This item has added into your cart",Toast.LENGTH_SHORT).show();
                                    }).addOnFailureListener(failure->{
                                        Toast.makeText(getApplicationContext(),failure.getMessage(),Toast.LENGTH_SHORT).show();
                                    });
                        }else{
                            Cart cartItem = new Cart();
                            for (DataSnapshot childNode : snapshot.getChildren()) {
                                cartItem=childNode.getValue(Cart.class);
                            }
                            if(Objects.equals(cartItem.getSize(), selectedRadioButton.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "This item is already in cart", Toast.LENGTH_SHORT).show();
                            }else{
                                cart.setQuantity(cartItem.getQuantity());
                                FirebaseDatabase.getInstance()
                                        .getReference("carts/"+FirebaseAuth.getInstance().getUid()+"/items/"+ passedProduct.getId())
                                        .setValue(cart).addOnSuccessListener(listener->{
                                            Toast.makeText(getApplicationContext(),"Size has changed for this product in the cart",Toast.LENGTH_SHORT).show();
                                        }).addOnFailureListener(failure->{
                                            Toast.makeText(getApplicationContext(),failure.getMessage(),Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProductDetailsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
package com.saurabh.justcheckout.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.admin.classes.Product;
import com.saurabh.justcheckout.admin.classes.ProductListAdapter;
import com.saurabh.justcheckout.user.home.MainActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class CreateProductActivity extends AppCompatActivity {
    ImageView product_picture_button,add_Product_Image;
    Uri selectedImage;
    String part_image;
    Button add_product_button;
    EditText add_product_name,add_product_material,add_product_price,add_product_quantity,add_product_weight,add_product_description;
    Spinner weightMeasure,productCategory;
    CheckBox sizeM,sizeL,sizeS,sizeXL,sizeXXL;
    LinearLayout checkboxLinearLayout,adminEditProductProgressBar;
    String sizes="";
    String randomFileName = "";
    boolean isEditFlow = false;
    String productId;
    Product editedProduct = new Product();
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
    // Permissions for accessing the storage
    private static final int PICK_IMAGE_REQUEST = 9544;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private final static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        productId = getIntent().getStringExtra("productId");
        if(productId!=null)
            isEditFlow = true;
        product_picture_button = findViewById(R.id.product_picture_button);
        add_Product_Image = findViewById(R.id.add_Product_Image);
        add_product_button = findViewById(R.id.add_product_button);
        add_product_name = findViewById(R.id.add_product_name);
        add_product_material = findViewById(R.id.add_product_material);
        add_product_price = findViewById(R.id.add_product_price);
        add_product_quantity = findViewById(R.id.add_product_quantity);
        add_product_weight = findViewById(R.id.add_product_weight);
        add_product_description = findViewById(R.id.add_product_description);
        weightMeasure = findViewById(R.id.add_product_weight_measure);
        productCategory = findViewById(R.id.add_product_category);
        sizeS = findViewById(R.id.add_size_s);
        sizeM = findViewById(R.id.add_size_m);
        sizeL = findViewById(R.id.add_size_l);
        sizeXL = findViewById(R.id.add_size_xl);
        sizeXXL = findViewById(R.id.add_size_xxl);
        checkboxLinearLayout = findViewById(R.id.checkboxLinearLayout);
        adminEditProductProgressBar = findViewById(R.id.adminEditProductProgressBar);
        add_product_button.setOnClickListener(view->{
            validateInput();
        });
        product_picture_button.setOnClickListener(view -> {
            addProductImage();
        });
        if(isEditFlow)
            loadDataById(productId);
    }

    private void loadDataById(String productId) {
        product_picture_button.setVisibility(View.GONE);
        adminEditProductProgressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("products").child(productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        adminEditProductProgressBar.setVisibility(View.GONE);
                        editedProduct = snapshot.getValue(Product.class);
                        populateData(editedProduct);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        adminEditProductProgressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateProductActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateData(Product product) {
        if(product==null)
            return;
        add_product_name.setText(product.getName());
        add_product_material.setText(product.getMaterial());
        add_product_price.setText(product.getPrice().toString());
        add_product_quantity.setText(String.valueOf(product.getQuantity()));
        add_product_description.setText(product.getDescription());
        String[] categories = getResources().getStringArray(R.array.category);
        productCategory.setSelection(Arrays.asList(categories).indexOf(product.getCategory()));
        String[] measures = getResources().getStringArray(R.array.weight_measure);
        String weight="",measure="";
        for (int i = 0; i < product.getWeight().length(); i++) {
            if(Character.isDigit(product.getWeight().charAt(i))){
                weight+=product.getWeight().charAt(i);
            }else{
                measure+=product.getWeight().charAt(i);
            }
        }
        add_product_weight.setText(weight);
        weightMeasure.setSelection(Arrays.asList(measures).indexOf(measure.trim()));
        String[] sizes = product.getSize().split(",");
        for(int i = 0; i < checkboxLinearLayout.getChildCount(); i++) {
            View v = checkboxLinearLayout.getChildAt(i);
            if(v instanceof CheckBox) {
                if(Arrays.asList(sizes).contains(((CheckBox) v).getText()))
                    ((CheckBox) v).setChecked(true);
            }
        }
        Glide.with(getApplicationContext()).load(product.getImageUrl()).placeholder(R.drawable.bag).error(R.drawable.just_check_out).into(add_Product_Image);
        add_product_button.setText(R.string.edit_product);
    }

    private void validateInput() {
        if(TextUtils.isEmpty(add_product_name.getText().toString().trim())){
            add_product_name.setText(add_product_name.getText().toString().trim());
            add_product_name.setError("Name is required");
            return;
        }
        if(TextUtils.isEmpty(add_product_material.getText().toString().trim())){
            add_product_material.setText(add_product_material.getText().toString().trim());
            add_product_material.setError("Material is required");
            return;
        }
        if(TextUtils.isEmpty(add_product_price.getText().toString().trim())){
            add_product_price.setText(add_product_price.getText().toString().trim());
            add_product_price.setError("Price is required");
            return;
        }
        if(TextUtils.isEmpty(add_product_quantity.getText().toString().trim())){
            add_product_quantity.setText(add_product_quantity.getText().toString().trim());
            add_product_quantity.setError("Quantity is required");
            return;
        }
        if(TextUtils.isEmpty(add_product_weight.getText().toString().trim())){
            add_product_weight.setText(add_product_weight.getText().toString().trim());
            add_product_weight.setError("Weight is required");
            return;
        }
        if(TextUtils.isEmpty(add_product_description.getText().toString().trim())){
            add_product_description.setText(add_product_description.getText().toString().trim());
            add_product_description.setError("Weight is required");
            return;
        }
        if(selectedImage==null && !isEditFlow){
            Toast.makeText(CreateProductActivity.this,"Image is required",Toast.LENGTH_SHORT).show();
            return;
        }
        sizes = "";
        for(int i = 0; i < checkboxLinearLayout.getChildCount(); i++) {
            View v = checkboxLinearLayout.getChildAt(i);
            if(v instanceof CheckBox) {
               if(((CheckBox)v).isChecked()){
                   sizes+=((CheckBox)v).getText().toString()+",";
               }
            }
        }
        if(Objects.equals(sizes, "")){
            Toast.makeText(CreateProductActivity.this,"Please select at least one size",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isEditFlow)
            addImage();
        else
            updateProduct();
    }

    private void updateProduct() {
        if(selectedImage==null){
            editedProduct = getProductObject();
            sizes = "";
            for(int i = 0; i < checkboxLinearLayout.getChildCount(); i++) {
                View v = checkboxLinearLayout.getChildAt(i);
                if(v instanceof CheckBox) {
                    if(((CheckBox)v).isChecked()){
                        sizes+=((CheckBox)v).getText().toString()+",";
                    }
                }
            }
            editedProduct.setId(productId);
            editedProduct.setSize(sizes.substring(0, sizes.length() - 1));
            FirebaseDatabase.getInstance().getReference("products")
                    .child(editedProduct.getId()).setValue(editedProduct)
                    .addOnSuccessListener(l->{
                        Toast.makeText(CreateProductActivity.this,"Product updated successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateProductActivity.this,ProductListActivity.class));
                        finishAffinity();
                    }).addOnFailureListener(e->{
                        Toast.makeText(CreateProductActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void addImage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        randomFileName = UUID.randomUUID().toString()+".png";
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("products/"+randomFileName);
        reference.putFile(selectedImage)
                .addOnSuccessListener(taskSnapshot -> {
                    reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        progressDialog.dismiss();
                        addProduct(uri.toString());
                    });

                })
                .addOnFailureListener(e -> {// Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(CreateProductActivity.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                });
    }
    private void addProduct(String imageUrl){
        Product product = getProductObject();
        product.setImageUrl(imageUrl);
        FirebaseDatabase.getInstance().getReference("products").child(product.getId()).setValue(product)
                .addOnSuccessListener(OnSuccessListener->{
                    Toast.makeText(CreateProductActivity.this,"Product added successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateProductActivity.this,ProductListActivity.class));
                    finishAffinity();
                }).addOnFailureListener(OnFailureListener->{
                    Toast.makeText(CreateProductActivity.this,OnFailureListener.getMessage(),Toast.LENGTH_SHORT).show();
                });
    }
    Product getProductObject(){
        return new Product(
                UUID.randomUUID().toString(),
                add_product_name.getText().toString().trim(),
                add_product_description.getText().toString().trim(),
                Double.parseDouble((add_product_price.getText()).toString().trim()),
                add_product_weight.getText()+" "+weightMeasure.getSelectedItem().toString().trim(),
                add_product_material.getText().toString().trim(),
                productCategory.getSelectedItem().toString().trim(),
                Integer.parseInt((add_product_quantity.getText()).toString().trim()),
                sizes.substring(0, sizes.length() - 1),
                Objects.equals(randomFileName, "") ?editedProduct.getImageUrl():randomFileName,
                isEditFlow && editedProduct.getTopPic());
    }

    void addProductImage(){
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateProductActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Choose your product picture");
        builder.setItems(options, (dialog, item) -> {
            if (options[item] == "Choose from Gallery") {
                verifyStoragePermissions(CreateProductActivity.this);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Open Gallery"), PICK_IMAGE_REQUEST);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedImage = data.getData();                                                         // Get the image file URI
                String[] imageProjection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, imageProjection, null, null, null);
                if(cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    part_image = cursor.getString(indexImage);                                                    // Get the image file absolute path
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    add_Product_Image.setImageBitmap(bitmap);                                                       // Set the ImageView with the bitmap of the image
                }
            }
        }
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
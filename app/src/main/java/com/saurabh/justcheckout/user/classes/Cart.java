package com.saurabh.justcheckout.user.classes;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Cart implements Serializable {
    private String productId,size;
    private int quantity;

    public void setSize(String size) {
        this.size = size;
    }
    public Cart() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getSize() {
        return size;
    }

    public Cart(String productId, int quantity, String size) {
        this.productId = productId;
        this.quantity = quantity;
        this.size = size;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}

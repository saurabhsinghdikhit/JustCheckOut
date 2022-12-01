package com.saurabh.justcheckout.user.classes;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Cart {
    private String productId;
    private int quantity;

    public Cart(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
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

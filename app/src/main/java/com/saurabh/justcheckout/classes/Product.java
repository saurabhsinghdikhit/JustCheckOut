package com.saurabh.justcheckout.classes;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {
    String id,name,description,price,weight,material,quantity,size;
    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Product(String id, String name, String description, String price, String weight, String material, String quantity, String size) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.material = material;
        this.quantity = quantity;
        this.size = size;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getWeight() {
        return weight;
    }

    public String getMaterial() {
        return material;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }
}

package com.saurabh.justcheckout.admin.classes;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {
    String id,name,description,weight,material,size,imageUrl,category;
    Double price;
    int quantity;
    boolean topPic;
    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    }

    public void setTopPic(boolean topPic) {
        this.topPic = topPic;
    }

    public Product(String id, String name, String description, Double price, String weight, String material, String category, int quantity, String size, String imageUrl, boolean topPic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.material = material;
        this.quantity = quantity;
        this.size = size;
        this.imageUrl = imageUrl;
        this.category = category;
        this.topPic = topPic;
    }

    public boolean getTopPic() {
        return topPic;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
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

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setQuantity(int quantity) {
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

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getWeight() {
        return weight;
    }

    public String getMaterial() {
        return material;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }
}

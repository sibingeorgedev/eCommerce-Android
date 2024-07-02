package com.example.project_2_ecommerce;

public class CartItem {
    private String itemId;
    private String productName;
    private String productUrl;
    private double productPrice;
    private String productDescription;
    private int quantity;

    public CartItem() {
        // Default constructor required for Firebase deserialization
    }

    public CartItem(String itemId, String productName, String productUrl, double productPrice, String productDescription, int quantity) {
        this.itemId = itemId;
        this.productName = productName;
        this.productUrl = productUrl;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.quantity = quantity;
    }

    public String getItemId() {
        return itemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

package com.example.project_2_ecommerce;

import java.io.Serializable;

public class Product implements Serializable {
    private String productName;
    private String productUrl;
    private double productPrice;
    private String productDescription;

    public Product(String productName, String productUrl, double productPrice, String productDescription) {
        this.productName = productName;
        this.productUrl = productUrl;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
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
}

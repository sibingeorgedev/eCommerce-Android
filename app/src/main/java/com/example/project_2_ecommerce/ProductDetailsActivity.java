package com.example.project_2_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailsActivity extends AppCompatActivity {

    String productName;
    String productUrl;
    double productPrice;
    String productDescription;
    private int quantity = 1;
    private TextView quantityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent = getIntent();
        displayProductDetails(intent);
        setQuantityUpdation();
        setAddToCart();
        setCartButtonAction();
        setHomeButtonAction();
        setLogoutButtonAction();
    }

    private void setCartButtonAction() {
        Button logoutButton = findViewById(R.id.btn_cart);
        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailsActivity.this, CartActivity.class));
        });
    }

    private void displayProductDetails(Intent intent) {
        productName = intent.getStringExtra("productName");
        productUrl = intent.getStringExtra("productUrl");
        productPrice = intent.getDoubleExtra("productPrice", 0.0);
        productDescription = intent.getStringExtra("productDescription");

        ImageView productImageView = findViewById(R.id.productImageView);
        TextView productNameTextView = findViewById(R.id.productNameTextView);
        TextView productPriceTextView = findViewById(R.id.productPriceTextView);
        TextView productDescriptionTextView = findViewById(R.id.productDescriptionTextView);

        productNameTextView.setText(productName);
        Glide.with(this).load(productUrl).into(productImageView);
        productPriceTextView.setText(String.valueOf(productPrice));
        productDescriptionTextView.setText(productDescription);
    }

    private void setHomeButtonAction() {
        TextView homeButton = findViewById(R.id.homeButtonDetailsPage);
        homeButton.setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailsActivity.this, MainActivity.class));
        });
    }

    private void setAddToCart() {
        Button addToCartButton = findViewById(R.id.addToCartButton);
        addToCartButton.setOnClickListener(v -> {
            addToCart(productName, productUrl, productPrice, productDescription, quantity);
        });
    }

    private void setLogoutButtonAction() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Button logoutButton = findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                signOut();
            }
        });
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setQuantityUpdation() {
        quantityTextView = findViewById(R.id.quantityTextView);
        Button decreaseQuantityButton = findViewById(R.id.decreaseQuantityButton);
        Button increaseQuantityButton = findViewById(R.id.increaseQuantityButton);

        decreaseQuantityButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityTextView();
            }
        });

        increaseQuantityButton.setOnClickListener(v -> {
            quantity++;
            updateQuantityTextView();
        });
    }

    private void updateQuantityTextView() {
        quantityTextView.setText(String.valueOf(quantity));
    }

    private void addToCart(String productName, String productUrl, double productPrice, String productDescription, int quantity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);

            cartRef.orderByChild("productName").equalTo(productName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            CartItem cartItem = itemSnapshot.getValue(CartItem.class);

                            int updatedQuantity = cartItem.getQuantity() + quantity;
                            itemSnapshot.getRef().child("quantity").setValue(updatedQuantity);
//                            startActivity(new Intent(ProductDetailsActivity.this, CartActivity.class));
                            return;
                        }
                    } else {
                        String cartItemId = cartRef.push().getKey();
                        if (cartItemId != null) {
                            CartItem cartItem = new CartItem(cartItemId, productName, productUrl, productPrice, productDescription, quantity);
                            cartRef.child(cartItemId).setValue(cartItem);
//                            startActivity(new Intent(ProductDetailsActivity.this, CartActivity.class));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Log.e("addToCart", "Failed to add item to cart: " + databaseError.getMessage());
                }
            });
        }
    }
}
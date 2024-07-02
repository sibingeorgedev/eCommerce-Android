package com.example.project_2_ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    RecyclerView.Adapter productsAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Product> productsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mAuth = FirebaseAuth.getInstance();

        fetchProductsFromDB();
        setProductsRecyclerView();
        setLogoutButtonAction();
        setCartButtonAction();
        setHomeButtonAction();
    }

    private void fetchProductsFromDB() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = database.getReference("products");

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsList.clear();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    String productName = productSnapshot.child("productName").getValue(String.class);
                    String productUrl = productSnapshot.child("productUrl").getValue(String.class);
                    double productPrice = productSnapshot.child("productPrice").getValue(Double.class);
                    String productDescription = productSnapshot.child("productDescription").getValue(String.class);

                    Product product = new Product(productName, productUrl, productPrice, productDescription);

                    productsList.add(product);
                }

                productsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductActivity.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setProductsRecyclerView() {
        recyclerView = findViewById(R.id.productsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        productsAdapter = new ProductsAdapter(this, productsList, product -> {
            Intent intent = new Intent(ProductActivity.this, ProductDetailsActivity.class);
            startActivity(intent);
        });
        recyclerView.setAdapter(productsAdapter);

    }

    private void setCartButtonAction() {
        Button logoutButton = findViewById(R.id.btn_cart);
        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(ProductActivity.this, CartActivity.class));
        });
    }

    private void setHomeButtonAction() {
        TextView homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            startActivity(new Intent(ProductActivity.this, MainActivity.class));
        });
    }

    private void setLogoutButtonAction() {
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
        Intent intent = new Intent(ProductActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

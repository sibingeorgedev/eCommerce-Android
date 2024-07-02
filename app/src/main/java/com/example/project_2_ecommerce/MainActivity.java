package com.example.project_2_ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager2 vPager;
    RecyclerView.Adapter adapter;
    ArrayList<FeaturedHotWheels> hotWheelsList = new ArrayList<>();
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        setLoginButtonAction();
        setViewProductsButtonAction();
        setFeaturedProducts();
    }

    private void setLoginButtonAction() {
        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(v -> redirectToLoginActivity());
    }

    private void setViewProductsButtonAction() {
        Button productButton = findViewById(R.id.productButton);
        productButton.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                redirectToProductActivity();
            } else {
                redirectToLoginActivity();
            }
        });

    }

    private void setFeaturedProducts() {
        FeaturedHotWheels hotWheel1 = new FeaturedHotWheels("hotwheel1");
        FeaturedHotWheels hotWheel2 = new FeaturedHotWheels("hotwheel2");
        FeaturedHotWheels hotWheel3 = new FeaturedHotWheels("hotwheel3");

        hotWheelsList.add(hotWheel1);
        hotWheelsList.add(hotWheel2);
        hotWheelsList.add(hotWheel3);

        vPager = findViewById(R.id.vPager);
        adapter = new FeaturedHotWheelsAdapter(hotWheelsList);
        vPager.setAdapter(adapter);
    }

    private void redirectToProductActivity() {
        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}


package com.example.project_2_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import java.text.DecimalFormat;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getCartItems();
        setCheckOutButton();
    }

    private void setCheckOutButton() {
        Button checkOutButton = findViewById(R.id.checkoutButton);
        checkOutButton.setOnClickListener(v -> {
            if (!cartItemList.isEmpty()) {
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            } else {
                Toast.makeText(CartActivity.this, "Please Add Product to Checkout",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCartItems() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);
            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cartItemList = new ArrayList<>();
                    for (DataSnapshot cartItemSnapshot : dataSnapshot.getChildren()) {
                        CartItem cartItem = cartItemSnapshot.getValue(CartItem.class);
                        cartItemList.add(cartItem);
                    }

                    calculateAndDisplayTotal(cartItemList);

                    setCartsRecyclerView(cartItemList, cartRef);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void setCartsRecyclerView(List<CartItem> cartItemList, DatabaseReference cartRef) {
        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItemList, this, cartRef);
        recyclerView.setAdapter(cartAdapter);
    }

    private void calculateAndDisplayTotal(List<CartItem> cartItemList) {
        double totalPrice = 0.0;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        for (CartItem cartItem : cartItemList) {
            totalPrice += cartItem.getProductPrice() * cartItem.getQuantity();
        }

        double tax = totalPrice * 0.13;

        double grandTotal = totalPrice + tax;

        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
        TextView taxTextView = findViewById(R.id.taxTextView);
        TextView grandTotalTextView = findViewById(R.id.grandTotalTextView);

        totalPriceTextView.setText("Cart Items Total Price: $" + decimalFormat.format(totalPrice));
        taxTextView.setText("Tax (13%): $" + decimalFormat.format(tax));
        grandTotalTextView.setText("Grand Total: $" + decimalFormat.format(grandTotal));
    }
}
package com.example.project_2_ecommerce;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public ProductsAdapter(Context context,  List<Product> productsList, OnItemClickListener listener) {
        this.context = context;
        this.productsList = productsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_row_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productsList.get(position);
        holder.bind(product, listener);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("productName", product.getProductName());
            intent.putExtra("productUrl", product.getProductUrl());
            intent.putExtra("productPrice", product.getProductPrice());
            intent.putExtra("productDescription", product.getProductDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView priceTextView;
        private TextView nameTextView;
        private TextView descriptionTextView;
        private Button addToCartButton;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            priceTextView = itemView.findViewById(R.id.price);
            nameTextView = itemView.findViewById(R.id.name);
            descriptionTextView = itemView.findViewById(R.id.description);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }

        public void bind(Product product, OnItemClickListener listener) {
            Glide.with(itemView.getContext())
                    .load(product.getProductUrl())
                    .into(imageView);
            priceTextView.setText(String.valueOf(product.getProductPrice()));
            nameTextView.setText(String.valueOf(product.getProductName()));
            descriptionTextView.setText(String.valueOf(product.getProductDescription()));

            addToCartButton.setOnClickListener(v -> {
                addToCart(product);
            });
        }

        private void addToCart(Product product) {
            String productName = product.getProductName();
            String productUrl = product.getProductUrl();
            double productPrice = product.getProductPrice();
            String productDescription = product.getProductDescription();
            int quantity = 1;

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
                                return;
                            }
                        } else {
                            String cartItemId = cartRef.push().getKey();
                            if (cartItemId != null) {
                                CartItem cartItem = new CartItem(cartItemId, productName, productUrl, productPrice, productDescription, quantity);
                                cartRef.child(cartItemId).setValue(cartItem);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("addToCart", "Failed to add item to cart: " + databaseError.getMessage());
                    }
                });
            }
        }
    }
}

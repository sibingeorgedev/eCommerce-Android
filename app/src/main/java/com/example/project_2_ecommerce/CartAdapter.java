package com.example.project_2_ecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;
    private DatabaseReference cartRef;

    public CartAdapter(List<CartItem> cartItemList, Context context, DatabaseReference cartRef) {
        this.cartItemList = cartItemList;
        this.context = context;
        this.cartRef = cartRef;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.bind(cartItem);
        holder.increaseButton.setOnClickListener(v -> increaseQuantity(cartItem));
        holder.decreaseButton.setOnClickListener(v -> decreaseQuantity(cartItem));
        holder.deleteButton.setOnClickListener(v -> removeItem(cartItem));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    private void increaseQuantity(CartItem cartItem) {
        int newQuantity = cartItem.getQuantity() + 1;
        cartItem.setQuantity(newQuantity);
        cartRef.child(cartItem.getItemId()).setValue(cartItem);
        notifyDataSetChanged();
        updateTotalPrice();
    }

    private void decreaseQuantity(CartItem cartItem) {
        int newQuantity = cartItem.getQuantity() - 1;
        if (newQuantity <= 0) {
            cartRef.child(cartItem.getItemId()).removeValue();
            int position = cartItemList.indexOf(cartItem);
            cartItemList.remove(cartItem);
            notifyItemRemoved(position);
        } else {
            cartItem.setQuantity(newQuantity);
            cartRef.child(cartItem.getItemId()).setValue(cartItem);
        }
        notifyDataSetChanged();
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;
        for (CartItem cartItem : cartItemList) {
            totalPrice += cartItem.getProductPrice() * cartItem.getQuantity();
        }

        double tax = totalPrice * 0.13;
        double grandTotal = totalPrice + tax;

        TextView totalPriceTextView = ((CartActivity) context).findViewById(R.id.totalPriceTextView);
        TextView taxTextView = ((CartActivity) context).findViewById(R.id.taxTextView);
        TextView grandTotalTextView = ((CartActivity) context).findViewById(R.id.grandTotalTextView);

        totalPriceTextView.setText("Cart Items Total Price: $" + String.format("%.2f", totalPrice));
        taxTextView.setText("Tax (13%): $" + String.format("%.2f", tax));
        grandTotalTextView.setText("Grand Total: $" + String.format("%.2f", grandTotal));
    }

    private void removeItem(CartItem cartItem) {
        cartRef.child(cartItem.getItemId()).removeValue();
        int position = cartItemList.indexOf(cartItem);
        cartItemList.remove(cartItem);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        updateTotalPrice();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImageView;
        private TextView productNameTextView;
        private TextView productPriceTextView;
        private TextView quantityTextView;
        private Button increaseButton;
        private Button decreaseButton;
        private ImageButton deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.cartProductImageView);
            productNameTextView = itemView.findViewById(R.id.cartProductNameTextView);
            productPriceTextView = itemView.findViewById(R.id.cartProductPriceTextView);
            quantityTextView = itemView.findViewById(R.id.cartQuantityTextView);
            increaseButton = itemView.findViewById(R.id.cartIncreaseQuantityButton);
            decreaseButton = itemView.findViewById(R.id.cartDecreaseQuantityButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(CartItem cartItem) {
            Glide.with(itemView.getContext()).load(cartItem.getProductUrl()).into(productImageView);
            productNameTextView.setText(cartItem.getProductName());
            productPriceTextView.setText(String.valueOf(cartItem.getProductPrice()));
            quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
        }
    }
}

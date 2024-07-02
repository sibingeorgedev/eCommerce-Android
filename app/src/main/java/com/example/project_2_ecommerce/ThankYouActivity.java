package com.example.project_2_ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ThankYouActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        setBackToProductsButton();
    }

    private void setBackToProductsButton() {
        Button backToProductsButton = findViewById(R.id.backToProductsButton);
        backToProductsButton.setOnClickListener(v -> {
            startActivity(new Intent(ThankYouActivity.this, ProductActivity.class));
        });
    }
}
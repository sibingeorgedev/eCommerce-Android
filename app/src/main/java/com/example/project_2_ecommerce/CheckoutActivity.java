package com.example.project_2_ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CheckoutActivity extends AppCompatActivity {

    private TextInputEditText creditCardEditText;
    private TextWatcher maskCreditCardWatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        setPurchaseButton();
        maskCreditCardDetails();
    }

    private void setPurchaseButton() {
        Button purchaseButton = findViewById(R.id.purchaseButton);
        purchaseButton.setOnClickListener(v -> {
            clearCart();
            startActivity(new Intent(CheckoutActivity.this, ThankYouActivity.class));
        });
    }

    private void clearCart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);
            cartRef.removeValue();
        }
    }

    private void maskCreditCardDetails() {
        creditCardEditText = findViewById(R.id.creditCardEditText);

        maskCreditCardWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                formatCreditCardNumber(s);
            }
        };
        creditCardEditText.addTextChangedListener(maskCreditCardWatcher);
    }

    private void formatCreditCardNumber(Editable s) {
        String creditCardNumber = s.toString().replace(" ", "");

        if (creditCardNumber.length() == 16) {
            StringBuilder maskedNumber = new StringBuilder();

            for (int i = 0; i < 12; i++) {
                maskedNumber.append("*");
            }

            maskedNumber.append(creditCardNumber.substring(12));

            creditCardEditText.removeTextChangedListener(maskCreditCardWatcher);
            creditCardEditText.setText(maskedNumber.toString());
            creditCardEditText.setSelection(maskedNumber.length());
            creditCardEditText.addTextChangedListener(maskCreditCardWatcher);
        }
    }

}
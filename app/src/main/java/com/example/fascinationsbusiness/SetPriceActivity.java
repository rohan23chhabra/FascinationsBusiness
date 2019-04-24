package com.example.fascinationsbusiness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fascinationsbusiness.db.DB;

public class SetPriceActivity extends AppCompatActivity {

    EditText priceText;
    Button priceButton;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_price);

        priceButton = findViewById(R.id.button_set_price);
        priceText = findViewById(R.id.input_price_for_bag);

        Bundle bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("phone");
    }

    public void priceOnClick(View view) {
        int price = Integer.parseInt(priceText.getText().toString());
        DB.getDatabaseReference().child("inventory-owner").child(phoneNumber).child("price")
                .setValue(price);

        Toast.makeText(this, "Price set successfully.", Toast.LENGTH_SHORT).show();
    }
}

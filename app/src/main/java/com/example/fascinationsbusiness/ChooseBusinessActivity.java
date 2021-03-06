package com.example.fascinationsbusiness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ChooseBusinessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_business);
    }

    public void registerInventory(View view) {
        Intent intent = new Intent(ChooseBusinessActivity.this,
                PhoneAuthActivity.class);
        intent.putExtra("owner-type", "inventory");
        startActivity(intent);
    }

    public void registerFoodVendor(View view) {
        Intent intent = new Intent(ChooseBusinessActivity.this,
                PhoneAuthActivity.class);
        intent.putExtra("owner-type", "vendor");
        startActivity(intent);
    }
}

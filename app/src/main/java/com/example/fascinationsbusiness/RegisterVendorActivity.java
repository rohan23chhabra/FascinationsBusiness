package com.example.fascinationsbusiness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterVendorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vendor);
    }

    public void setVendorLocationOnMap(View view) {
        Intent intent = new Intent(RegisterVendorActivity.this,
                SetVendorLocationOnMap.class);
        startActivity(intent);
    }
}

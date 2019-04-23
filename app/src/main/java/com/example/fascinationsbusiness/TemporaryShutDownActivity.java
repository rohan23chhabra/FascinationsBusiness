package com.example.fascinationsbusiness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fascinationsbusiness.db.DBReference;
import com.google.firebase.database.DatabaseReference;

public class TemporaryShutDownActivity extends AppCompatActivity {

    String phoneNumber;
    private DatabaseReference databaseReference;
    Button shut,enable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary_shut_down);
        Bundle bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("phonenumber");
        shut = findViewById(R.id.shut_me);
        enable = findViewById(R.id.enable_me);
    }

    public void onClickShutDown(View view) {
        databaseReference = DBReference.getDatabaseReference();
        databaseReference.child("inventory-owner").child(phoneNumber).child("open").setValue(
                "false");
        enable.setVisibility(View.VISIBLE);
        shut.setVisibility(View.GONE);

    }

    public void onClickEnable(View view) {
        databaseReference = DBReference.getDatabaseReference();
        databaseReference.child("inventory-owner").child(phoneNumber).child("open").setValue(
                "true");
        shut.setVisibility(View.VISIBLE);
        enable.setVisibility(View.GONE);
    }
}

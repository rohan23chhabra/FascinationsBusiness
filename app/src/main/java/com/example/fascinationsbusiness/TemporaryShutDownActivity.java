package com.example.fascinationsbusiness;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fascinationsbusiness.db.DB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class TemporaryShutDownActivity extends AppCompatActivity {

    String phoneNumber;
    private DatabaseReference databaseReference;
    Button shut, enable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary_shut_down);
        Bundle bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("phonenumber");
        shut = findViewById(R.id.shut_me);
        enable = findViewById(R.id.enable_me);
        databaseReference = DB.getDatabaseReference();
        databaseReference.child("inventory-owner").child(phoneNumber).child("open")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String openValue = dataSnapshot.getValue(String.class);
                                if (openValue.equals("true")) {
                                    enable.setVisibility(View.GONE);
                                } else {
                                    shut.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
    }

    public void onClickShutDown(View view) {
        databaseReference = DB.getDatabaseReference();
        databaseReference.child("inventory-owner").child(phoneNumber).child("open").setValue(
                "false");

    }

    public void onClickEnable(View view) {
        databaseReference = DB.getDatabaseReference();
        databaseReference.child("inventory-owner").child(phoneNumber).child("open").setValue(
                "true");
    }
}

package com.example.fascinationsbusiness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fascinationsbusiness.core.InventoryOwner;
import com.example.fascinationsbusiness.util.SessionDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    EditText phoneText;
    Button enterButton;
    Button verifyButton;
    Button discardButton;
    TextView nameView;
    TextView emailView;
    TextView phoneView;
    TextView addressView;
    ImageView imageView;
    InventoryOwner owner;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        phoneText = findViewById(R.id.input_phone);
        enterButton = findViewById(R.id.admin_enter);
        verifyButton = findViewById(R.id.admin_verify);
        discardButton = findViewById(R.id.admin_discard);
        nameView = findViewById(R.id.admin_inventory_owner);
        emailView = findViewById(R.id.admin_inventory_email);
        phoneView = findViewById(R.id.admin_inventory_phone);
        imageView = findViewById(R.id.admin_inventory_image);
        addressView = findViewById(R.id.admin_inventory_address);

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void enterButtonOnClick(View view) {
        final String phoneNumber = phoneText.getText().toString();

        nameView.setVisibility(View.VISIBLE);
        emailView.setVisibility(View.VISIBLE);
        phoneView.setVisibility(View.VISIBLE);
        addressView.setVisibility(View.VISIBLE);
        verifyButton.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        discardButton.setVisibility(View.VISIBLE);

        databaseReference.child("inventory-owner").child(phoneNumber)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override public void onDataChange(
                                    @NonNull DataSnapshot dataSnapshot) {
                                String json =
                                        new Gson().toJson(dataSnapshot
                                                .getValue());
                                Log.i("string-json", json);
                                InventoryOwner owner = new Gson()
                                        .fromJson(json,
                                                InventoryOwner.class);
                                AdminActivity.this.owner = owner;
                                if (owner == null) {
                                    Log.i("nalla", "nalla");
                                    return;
                                }
                                nameView.setText(owner.getName());
                                emailView.setText(owner.getEmail());
                                phoneView.setText(owner.getPhoneNumber());
                                addressView.setText(owner.getAddress());
                                Picasso.get().load(owner.getImageURL())
                                        .into(imageView);
                            }

                            @Override public void onCancelled(
                                    @NonNull DatabaseError databaseError) {

                            }
                        });

    }

    public void verifyButtonOnClick(View view) {
        owner.setVerified("true");
        databaseReference.child("inventory-owner")
                .child(owner.getPhoneNumber())
                .setValue(owner);
        Toast.makeText(this, "Inventory owner verified", Toast.LENGTH_SHORT).show();
    }

    public void discardButtonOnClick(View view) {
        databaseReference.child("inventory-owner")
                .child(owner.getPhoneNumber())
                .removeValue();
        Toast.makeText(this, "Inventory owner discarded", Toast.LENGTH_SHORT).show();
    }

    public void logoutButtonOnClick(View view) {
        new SessionDetails(this).getEditor().clear();
        new SessionDetails(this).getEditor().apply();
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}

package com.example.fascinationsbusiness;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fascinationsbusiness.core.InventoryOwner;
import com.example.fascinationsbusiness.core.Owner;
import com.example.fascinationsbusiness.core.VendorOwner;
import com.example.fascinationsbusiness.serialize.MyGson;
import com.example.fascinationsbusiness.util.SessionDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
    Owner owner;
    String selectedOwner;

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

        databaseReference.child(selectedOwner).child(phoneNumber)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override public void onDataChange(
                                    @NonNull DataSnapshot dataSnapshot) {
                                Gson gson = MyGson.getGson();
                                Owner owner;
                                if (selectedOwner.equalsIgnoreCase("inventory-owner")) {
                                    owner = gson.fromJson(gson
                                                    .toJson(dataSnapshot
                                                            .getValue()),
                                            InventoryOwner.class);
                                } else {
                                    owner = gson.fromJson(gson
                                                    .toJson(dataSnapshot
                                                            .getValue()),
                                            VendorOwner.class);
                                }
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
        if (selectedOwner.equalsIgnoreCase("inventory-owner")) {
            InventoryOwner inventoryOwner = (InventoryOwner) owner;
            inventoryOwner.setVerified("true");
            databaseReference.child(selectedOwner)
                    .child(owner.getPhoneNumber())
                    .setValue(inventoryOwner);
            Toast.makeText(this, "Inventory owner verified", Toast.LENGTH_SHORT).show();
        } else {
            VendorOwner vendorOwner = (VendorOwner) owner;
            vendorOwner.setVerified("true");
            databaseReference.child(selectedOwner)
                    .child(owner.getPhoneNumber())
                    .setValue(vendorOwner);
            Toast.makeText(this, "Vendor owner verified", Toast.LENGTH_SHORT).show();
        }
    }

    public void discardButtonOnClick(View view) {
        databaseReference.child(selectedOwner)
                .child(owner.getPhoneNumber())
                .removeValue();
        Toast.makeText(this, "Owner discarded", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AdminActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    public void logoutButtonOnClick(View view) {
        SessionDetails sessionDetails = new SessionDetails(this);
        sessionDetails.getEditor().remove("phone");
        sessionDetails.getEditor().remove("password");
        sessionDetails.getEditor().apply();
        new SessionDetails(this).getEditor().apply();
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        if (checked) {
            if (view.getId() == R.id.radio_food_vendor_admin) {
                selectedOwner = "vendor-owner";
            } else if (view.getId() == R.id.radio_inventory_admin) {
                selectedOwner = "inventory-owner";
            }
        }
    }
}

package com.example.fascinationsbusiness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fascinationsbusiness.core.InventoryOwner;
import com.example.fascinationsbusiness.core.Owner;
import com.example.fascinationsbusiness.core.VendorOwner;
import com.example.fascinationsbusiness.security.SecurePassword;
import com.example.fascinationsbusiness.serialize.MyGson;
import com.example.fascinationsbusiness.service.NotificationService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class LoginActivity
        extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    EditText phoneText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;
    String selectedOwner;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneText = findViewById(R.id.input_phone);
        passwordText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.link_signup);
        progressBar = findViewById(R.id.progressbar);
        sharedPreferences = getSharedPreferences("Session",
                MODE_PRIVATE);
        editor = sharedPreferences.edit();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (!sharedPreferences.getString("phone", "#").equals("#") && !sharedPreferences.getString(
                "password", "#").equals("#") && !sharedPreferences.getString("selectedOwner", "#")
                .equals(
                        "#")) {
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);
            Toast.makeText(LoginActivity.this, "Logging In", Toast.LENGTH_SHORT).show();
            phoneText.setText(sharedPreferences.getString("phone", "#"));
            passwordText.setText(sharedPreferences.getString(
                    "password", "#"));
            selectedOwner = sharedPreferences.getString("selectedOwner", "#");
            if (selectedOwner.equals("vendor-owner")) {
                Intent intent = new Intent(
                        LoginActivity.this,
                        VendorOwnerHomePageActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            } else if (selectedOwner.equals("inventory-owner")) {
                Intent intent = new Intent(
                        LoginActivity.this,
                        InventoryOwnerHomePageActivity.class);
                intent.putExtra("phone", sharedPreferences.getString("phone", "#"));
                LoginActivity.this.startActivity(intent);
                finish();
            } else if (selectedOwner.equalsIgnoreCase("Admin")) {
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
                finish();
            }

        }

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        if (checked) {
            if (view.getId() == R.id.radio_admin) {
                selectedOwner = "Admin";
            } else if (view.getId() == R.id.radio_food_vendor) {
                selectedOwner = "vendor-owner";
            } else {
                selectedOwner = "inventory-owner";
            }
        }
    }

    public void loginButtonOnClick(View view) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if ((networkInfo != null) && (networkInfo.isConnected())) {
            final String phonenumber = phoneText.getText().toString();
            final String password = passwordText.getText().toString();
            phoneText.setEnabled(false);
            passwordText.setEnabled(false);
            loginButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            signupLink.setVisibility(View.GONE);
            if (phonenumber.equals("Admin") && password
                    .equals("Admin") && selectedOwner.equals("Admin")) {
                editor.putString("phone",
                        "Admin");
                editor.putString("password", "Admin");
                editor.apply();
                Intent intent = new Intent(
                        LoginActivity.this,
                        AdminActivity.class);
                LoginActivity.this
                        .startActivity(intent);
                finish();
                return;
            }
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(selectedOwner)
                    .child(phonenumber)
                    .addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(
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
                                    assert owner != null;
                                    String hash1 = owner.getPassword();
                                    String hash2 =
                                            SecurePassword.getHashedPassword(
                                                    passwordText.getText().toString(), phonenumber);
                                    if (hash1.equals(hash2)) {
                                        Log.i("login", "Successful login.");
                                        editor.putString("phone",
                                                phonenumber);
                                        editor.putString("password", owner.getPassword());
                                        editor.putString("selectedOwner", selectedOwner);
                                        editor.apply();
                                        startService(new Intent(LoginActivity.this,
                                                NotificationService.class));
                                        Log.i("login", "Ho gya " + "be");
                                        if (selectedOwner.equals(
                                                "inventory-owner")) {
                                            Intent intent = new Intent(
                                                    LoginActivity.this,
                                                    InventoryOwnerHomePageActivity.class);
                                            intent.putExtra("phone", phonenumber);
                                            LoginActivity.this
                                                    .startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(
                                                    LoginActivity.this,
                                                    VendorOwnerHomePageActivity.class);
                                            LoginActivity.this
                                                    .startActivity(intent);
                                            finish();
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(
                                        @NonNull DatabaseError databaseError) {

                                }
                            });
        } else {
            Toast.makeText(LoginActivity.this, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void signUpOnClick(View view) {
        Intent signUpIntent = new Intent(LoginActivity.this,
                ChooseBusinessActivity.class);
        startActivity(signUpIntent);
    }
}

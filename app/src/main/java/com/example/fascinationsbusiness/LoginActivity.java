package com.example.fascinationsbusiness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneText = findViewById(R.id.input_phone);
        passwordText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.link_signup);

        sharedPreferences = getSharedPreferences("Session",
                MODE_PRIVATE);
        editor = sharedPreferences.edit();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        if (checked) {
            if (view.getId() == R.id.radio_admin) {
                selectedOwner = "Admin";
            } else if (view.getId() == R.id.radio_food_vendor) {
                selectedOwner = "food-vendor-owner";
            } else {
                selectedOwner = "inventory-owner";
            }
        }
    }

    public void loginButtonOnClick(View view) {
        final String phonenumber = phoneText.getText().toString();
        final String password = passwordText.getText().toString();
        if (phonenumber.equals("Admin") && password
                .equals("Admin") && selectedOwner.equals("Admin")) {
            Intent intent = new Intent(
                    LoginActivity.this,
                    AdminActivity.class);
            LoginActivity.this
                    .startActivity(intent);
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
                                String pass = dataSnapshot
                                        .getValue(String.class);
                                if (passwordText.getText().toString()
                                        .equals(pass)) {
                                    Log.i("login", "Successful login.");
                                    editor.putString("phone",
                                            phonenumber);
                                    editor.putString("password", pass);
                                    editor.apply();
                                    Log.i("login", "Ho gya " + "be");
                                    if (selectedOwner.equals(
                                            "inventory-owner")) {
                                        Intent intent = new Intent(
                                                LoginActivity.this,
                                                ChooseBusinessActivity.class);
                                        LoginActivity.this
                                                .startActivity(intent);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError databaseError) {

                            }
                        });
    }

    public void signUpOnClick(View view) {
        Intent signUpIntent = new Intent(LoginActivity.this,
                ChooseBusinessActivity.class);
        startActivity(signUpIntent);
    }
}

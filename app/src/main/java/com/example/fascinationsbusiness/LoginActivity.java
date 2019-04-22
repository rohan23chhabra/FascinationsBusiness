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

    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.link_signup);

        sharedPreferences = getSharedPreferences("Session",
                MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences
                .edit();

        databaseReference = FirebaseDatabase.getInstance()
                .getReference();


        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String username = emailText.getText().toString()
                        .substring(0, emailText.getText().toString()
                                .indexOf('@'));
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("users")
                        .child(username)
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
                                            editor.putString("email", username);
                                            editor.putString("password", pass);
                                            editor.apply();
                                            Log.i("login", "Ho gya " + "be");
                                            Intent intent = new Intent(
                                                    LoginActivity.this,
                                                    ChooseBusinessActivity.class);
                                            LoginActivity.this
                                                    .startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(
                                            @NonNull DatabaseError databaseError) {

                                    }
                                });
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this,
                        PhoneAuthActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
}

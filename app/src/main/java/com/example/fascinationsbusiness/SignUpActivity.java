package com.example.fascinationsbusiness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity
        extends AppCompatActivity {

    EditText emailText;
    EditText passwordText;
    Button signUpButton;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        signUpButton = findViewById(R.id.btn_login);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("Session", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = emailText.getText().toString();
                username = username.substring(0, username.indexOf('@'));
                databaseReference.child("users").child(username)
                        .setValue(passwordText.getText().toString());
                Log.i("signup", "Success.");
                editor.putString("email", username);
                editor.putString("password", passwordText.getText().toString());
                editor.apply();
                /*Intent intent = new Intent(SignUpActivity.this,
                        MainActivity.class);
                SignUpActivity.this.startActivity(intent);*/

            }
        });
    }
}

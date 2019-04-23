package com.example.fascinationsbusiness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fascinationsbusiness.db.DBReference;
import com.google.firebase.database.DatabaseReference;

import java.sql.Time;
import java.util.StringTokenizer;

public class SetOpeningTime extends AppCompatActivity {

    EditText openingTime;
    String phoneNumber;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_opening_time);

        openingTime = findViewById(R.id.input_opening_time);
        Bundle bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("phonenumber");
    }

    public void onClickOpeningTime(View view) {

        String openingTime = this.openingTime.getText().toString();

        databaseReference = DBReference.getDatabaseReference();
        databaseReference.child("inventory-owner").child(phoneNumber).child("openingTime")
                .setValue(openingTime);
        Toast.makeText(SetOpeningTime.this, "Opening Time Updated", Toast.LENGTH_SHORT).show();
    }
}

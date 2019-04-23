package com.example.fascinationsbusiness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fascinationsbusiness.db.DBReference;
import com.google.firebase.database.DatabaseReference;

import java.sql.Time;
import java.util.StringTokenizer;

public class SetClosingTime extends AppCompatActivity {

    EditText closingTime;
    String phoneNumber;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_closing_time);

        closingTime = findViewById(R.id.input_closing_time);
        Bundle bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("phonenumber");
    }

    public void onClickClosingTime(View view) {

        String closingTime = this.closingTime.getText().toString();
        databaseReference = DBReference.getDatabaseReference();
        databaseReference.child("inventory-owner").child(phoneNumber).child("closingTime")
                .setValue(closingTime);
        Toast.makeText(SetClosingTime.this, "Closing Time Updated", Toast.LENGTH_SHORT).show();
    }
}

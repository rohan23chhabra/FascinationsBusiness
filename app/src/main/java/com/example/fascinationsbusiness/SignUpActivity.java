package com.example.fascinationsbusiness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.fascinationsbusiness.core.InventoryOwner;
import com.example.fascinationsbusiness.core.Owner;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;

public class SignUpActivity
        extends AppCompatActivity {

    EditText emailText;
    EditText nameText;
    EditText capacityText;
    EditText accountNumberText;
    EditText panText;
    EditText ifscText;
    EditText passwordText;
    Button signUpButton;
    Button setLocationButton;
    Button uploadPhotoButton;
    ImageView imgview;
    String bitmap;
    Owner owner;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        nameText = findViewById(R.id.input_owner_name);
        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        capacityText = findViewById(R.id.input_capacity);
        accountNumberText = findViewById(R.id.input_account_number);
        panText = findViewById(R.id.input_pan);
        ifscText = findViewById(R.id.input_ifsc);
        setLocationButton = findViewById(R.id.set_final_location);
        uploadPhotoButton = findViewById(R.id.btn_upload_photo);
        signUpButton = findViewById(R.id.btn_next);
        imgview = findViewById(R.id.targetimage);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                phoneNumber = null;
            } else {
                phoneNumber = extras.getString("phone");
            }
        } else {
            phoneNumber = (String) savedInstanceState.getSerializable("phone");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("Session", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        String email = emailText.getText().toString();
        String name = nameText.getText().toString();
        String password = passwordText.getText().toString();
        int capacity = Integer
                .parseInt(capacityText.getText().toString());
        String pan = panText.getText().toString();
        String ifsc = ifscText.getText().toString();
        String accountNumber = accountNumberText.getText().toString();

        owner = new InventoryOwner(name, email,
                password, ifsc, phoneNumber,
                pan, null,
                accountNumber, null,
                null, null,
                false, capacity);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("inventory-owner")
                        .child(owner.getPhoneNumber())
                        .setValue(owner);
                Log.i("signup", "Success.");
                editor.putString("email", owner.getPhoneNumber());
                editor.putString("password", owner.getPassword());
                editor.apply();
                Intent intent = new Intent(SignUpActivity.this,
                        ChooseBusinessActivity.class);
                SignUpActivity.this.startActivity(intent);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();

            try {
                bitmap =
                        BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(targetUri));
                owner.setImageURL(bitmap);
                imgview.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setLocationOfInventory(View view) {
        Intent intent = new Intent(SignUpActivity.this,
                SetLocationOfInventory.class);
        startActivity(intent);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("inventory-owner", owner);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        owner = (Owner) savedInstanceState.getSerializable("inventory-owner");
    }
}

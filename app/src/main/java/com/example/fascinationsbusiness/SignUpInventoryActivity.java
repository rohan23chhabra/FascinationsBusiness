package com.example.fascinationsbusiness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fascinationsbusiness.core.InventoryOwner;
import com.example.fascinationsbusiness.security.SecurePassword;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SignUpInventoryActivity
        extends AppCompatActivity {

    EditText emailText;
    EditText nameText;
    EditText addressText;
    EditText capacityText;
    EditText upiIdText;
    EditText passwordText;
    Button signUpButton;
    Button setLocationButton;
    Button uploadPhotoButton;
    ImageView imgview;
    Bitmap bitmap;
    ProgressBar progressBar;
    private InventoryOwner owner;
    private LatLng markedLocation;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST = 71;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        nameText = findViewById(R.id.input_owner_name);
        emailText = findViewById(R.id.input_phone);
        passwordText = findViewById(R.id.input_password);
        capacityText = findViewById(R.id.input_capacity);
        upiIdText = findViewById(R.id.input_upi_id);
        addressText = findViewById(R.id.input_address);
        setLocationButton = findViewById(R.id.set_final_location);
        uploadPhotoButton = findViewById(R.id.btn_upload_photo);
        signUpButton = findViewById(R.id.btn_next);
        progressBar = findViewById(R.id.progressbar);
        imgview = findViewById(R.id.targetimage);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                phoneNumber = null;
            } else {
                phoneNumber = extras.getString("phone");
                fillEditTextsOnReturn(extras);
            }
        } else {
            phoneNumber = (String) savedInstanceState.getSerializable("phone");
        }

        //phoneNumber = "8601444918";

        databaseReference = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("Session", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                emailText.setEnabled(false);
                nameText.setEnabled(false);
                addressText.setEnabled(false);
                capacityText.setEnabled(false);
                upiIdText.setEnabled(false);
                passwordText.setEnabled(false);
                // setLocationButton.setVisibility(View.GONE);
                // uploadPhotoButton.setVisibility(View.GONE);
                signUpButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                uploadImage();
                final StorageReference ref = storageReference
                        .child("images/" + owner.getPhoneNumber());
                UploadTask uploadTask = ref.putFile(filePath);
                Task<Uri> urlTask =
                        uploadTask.addOnProgressListener(
                                new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override public void onProgress(
                                            UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot
                                                .getBytesTransferred()) / taskSnapshot
                                                .getTotalByteCount();
                                        progressBar.setProgress((int) progress);
                                        Log.i("progress101", "lol");
                                    }
                                })
                                .continueWithTask(
                                        new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                            @Override
                                            public Task<Uri> then(
                                                    @NonNull Task<UploadTask.TaskSnapshot> task)
                                                    throws Exception {
                                                if (!task.isSuccessful()) {
                                                    throw task.getException();
                                                }

                                                // Continue with the task to get the download URL
                                                return ref.getDownloadUrl();
                                            }
                                        }).addOnCompleteListener(
                                new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(
                                            @NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Log.i("khatam", "mc");
                                            Uri downloadUri = task.getResult();
                                            owner.setImageURL(
                                                    downloadUri.toString());
                                            databaseReference
                                                    .child("inventory-owner")
                                                    .child(owner
                                                            .getPhoneNumber())
                                                    .setValue(owner);
                                            Log.i("looopp", "paar ho gya loop");
                                            Log.i("signup", "Success.");
                                            //Log.i("imageURL", owner.getImageURL());
                                            editor.putString("phone",
                                                    owner.getPhoneNumber());
                                            editor.putString("password",
                                                    owner.getPassword());
                                            editor.apply();
                                            Intent intent = new Intent(
                                                    SignUpInventoryActivity.this,
                                                    InventoryOwnerHomePageActivity.class);
                                            SignUpInventoryActivity.this
                                                    .startActivity(intent);
                                        } else {
                                            // Handle failures
                                            // ...
                                        }
                                    }
                                });


            }
        });

    }

    private void fillEditTextsOnReturn(Bundle extras) {
        owner = (InventoryOwner) extras
                .getSerializable("inventory-owner");
        if (owner == null) {
            return;
        }
        emailText.setText(owner.getEmail());
        nameText.setText(owner.getName());
        passwordText.setText(owner.getPassword());
        capacityText.setText(String.valueOf(owner.getCapacity()));
        upiIdText.setText(owner.getUpiId());
        addressText.setText(owner.getAddress());

        this.markedLocation = extras.getParcelable("inventory-location");
        owner.setLocation(markedLocation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                filePath = data.getData();
                Log.i("filemilgyi", filePath.toString());
                try {
                    bitmap =
                            MediaStore.Images.Media
                                    .getBitmap(this.getContentResolver(),
                                            filePath);
                    imgview.setImageBitmap(bitmap);
                    Log.i("bitmapmilgya", bitmap.toString());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setLocationOfInventory(View view) {
        Intent intent = new Intent(SignUpInventoryActivity.this,
                SetLocationOfInventory.class);
        String email = emailText.getText().toString();
        String name = nameText.getText().toString();
        String password = passwordText.getText().toString();
        int capacity = Integer
                .parseInt(capacityText.getText().toString());
        String upiId = upiIdText.getText().toString();
        String address = addressText.getText().toString();

        String hash = SecurePassword.getHashedPassword(password, phoneNumber);
        owner = new InventoryOwner(name, email, hash, phoneNumber, null,
                upiId, markedLocation, null, null,
                "true", capacity, address, 5);


        String openingTime = "10:0:0";
        String closingTime = "22:0:0";
        owner.setClosingTime(closingTime);
        owner.setOpeningTime(openingTime);
        intent.putExtra("inventory-owner", owner);
        startActivity(intent);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        owner = (InventoryOwner) savedInstanceState
                .getSerializable("inventory-owner");
        emailText.setText(owner.getEmail());
        nameText.setText(owner.getName());
        passwordText.setText(owner.getPassword());
        capacityText.setText(owner.getCapacity());
        upiIdText.setText(owner.getUpiId());
        addressText.setText(owner.getAddress());
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        this.markedLocation = extras.getParcelable("inventory" +
                "-location");
    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (bitmap != null) {
            Log.i("bitmapcheck", "kya hau");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference ref = storageReference
                    .child("images/" + owner.getPhoneNumber());

            UploadTask uploadTask = ref.putBytes(data);
            uploadTask
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.i("yusss", "picture upload.");
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpInventoryActivity.this,
                                    "Failed " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot
                                            .getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                }
                            });
        }
    }
}

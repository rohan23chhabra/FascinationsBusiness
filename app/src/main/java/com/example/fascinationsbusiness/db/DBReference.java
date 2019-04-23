package com.example.fascinationsbusiness.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBReference {
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReference();

    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}

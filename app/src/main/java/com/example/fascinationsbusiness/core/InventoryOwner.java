package com.example.fascinationsbusiness.core;

import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDateTime;

public class InventoryOwner extends Owner {
    private int capacity;

    public InventoryOwner(int capacity) {
        this.capacity = capacity;
    }

    public InventoryOwner() {
    }

    public InventoryOwner(String name, String email, String password,
                          String ifscCode, String phoneNumber,
                          String panNumber,
                          String imageURL, String accountNumber,
                          LatLng location,
                          LocalDateTime openingTime, LocalDateTime closingTime,
                          boolean isOpen,
                          int capacity) {
        super(name, email, password, ifscCode, phoneNumber,
                panNumber,
                imageURL, accountNumber, location, openingTime, closingTime,
                isOpen);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

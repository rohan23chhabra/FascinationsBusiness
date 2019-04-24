package com.example.fascinationsbusiness.concurrent;

import com.example.fascinationsbusiness.db.DB;

import java.util.TimerTask;

public class DeleteInventoryRequestTask extends TimerTask {
    String userPhoneNumber;
    String ownerPhoneNumber;
    int addCapacity;
    int prevCapacity;

    public DeleteInventoryRequestTask(String userPhoneNumber, int addCapacity,
                                      String ownerPhoneNumber, int prevCapacity) {
        this.userPhoneNumber = userPhoneNumber;
        this.addCapacity = addCapacity;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.prevCapacity = prevCapacity;
    }

    @Override public void run() {
        DB.getDatabaseReference().child("pending-inventory-requests").child(userPhoneNumber)
                .removeValue();

        DB.getDatabaseReference().child("inventory-owner").child(ownerPhoneNumber).child(
                "capacity").setValue(addCapacity + prevCapacity);
    }
}

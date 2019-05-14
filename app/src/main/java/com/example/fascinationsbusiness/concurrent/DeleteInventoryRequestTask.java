package com.example.fascinationsbusiness.concurrent;

import com.example.fascinationsbusiness.db.DB;

import java.util.TimerTask;

public class DeleteInventoryRequestTask extends TimerTask {
    private String userPhoneNumber;
    private String ownerPhoneNumber;
    private int addCapacity;
    private int prevCapacity;
    private int requestId;

    public DeleteInventoryRequestTask(String userPhoneNumber, int addCapacity,
                                      String ownerPhoneNumber, int prevCapacity, int requestId) {
        this.userPhoneNumber = userPhoneNumber;
        this.addCapacity = addCapacity;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.prevCapacity = prevCapacity;
        this.requestId = requestId;
    }

    @Override public void run() {
        DB.getDatabaseReference().child("pending-inventory-requests").child(
                String.valueOf(requestId))
                .removeValue();

        DB.getDatabaseReference().child("inventory-owner").child(ownerPhoneNumber).child(
                "capacity").setValue(addCapacity + prevCapacity);
    }
}

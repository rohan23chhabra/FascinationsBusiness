package com.example.fascinationsbusiness.core;

public class InventoryRequest {
    String userPhoneNumber;
    String ownerPhoneNumber;
    int hours;
    int minutes;
    int seconds;

    public InventoryRequest(String userPhoneNumber, String ownerPhoneNumber, int hours, int minutes,
                            int seconds) {
        this.userPhoneNumber = userPhoneNumber;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}

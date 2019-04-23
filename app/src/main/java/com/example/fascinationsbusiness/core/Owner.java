package com.example.fascinationsbusiness.core;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.time.LocalTime;

public class Owner implements Serializable {
    private String name;
    private String email;
    private String password;
    private String ifscCode;
    private String phoneNumber;
    private String panNumber;
    private String imageURL;
    private String accountNumber;
    private LatLng location;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private boolean isOpen;
    private boolean isVerified = false;
    private String address;






    public Owner() {
    }

    public Owner(String name, String email, String password,
                 String ifscCode, String phoneNumber,
                 String panNumber, String imageURL, String accountNumber,
                 LatLng location, LocalTime openingTime, LocalTime closingTime,
                 boolean isOpen,String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.ifscCode = ifscCode;
        this.phoneNumber = phoneNumber;
        this.panNumber = panNumber;
        this.imageURL = imageURL;
        this.accountNumber = accountNumber;
        this.location = location;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.isOpen = isOpen;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

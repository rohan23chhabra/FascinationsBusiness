package com.example.fascinationsbusiness.core;

import com.google.android.gms.maps.model.LatLng;

public class VendorOwner extends Owner {
    private FoodCategory foodCategory;
    private String qrCodeURL;

    public VendorOwner(String name, String email, String password, String ifscCode,
                       String phoneNumber, String panNumber, String imageURL,
                       String accountNumber, LatLng location,
                       String openingTime, String closingTime, String isOpen, String address,
                       FoodCategory foodCategory, String qrCodeURL) {
        super(name, email, password, ifscCode, phoneNumber, panNumber, imageURL, accountNumber,
                location, openingTime, closingTime, isOpen, address);
        this.foodCategory = foodCategory;
        this.qrCodeURL = qrCodeURL;
    }

    public FoodCategory getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(FoodCategory foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getQrCodeURL() {
        return qrCodeURL;
    }

    public void setQrCodeURL(String qrCodeURL) {
        this.qrCodeURL = qrCodeURL;
    }

    @Override public String toString() {
        return "VendorOwner{" +
                "foodCategory=" + foodCategory +
                ", qrCodeURL='" + qrCodeURL + '\'' +
                "} " + super.toString();
    }
}

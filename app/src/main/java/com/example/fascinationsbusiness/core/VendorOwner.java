package com.example.fascinationsbusiness.core;

import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

public class VendorOwner extends Owner {
    private FoodCategory foodCategory;
    private String qrCodeURL;
    private double avgRating;
    private int numberOfRatings;
    private Map<String, Integer> foodMenu;

    public VendorOwner(String name, String email, String password,
                       String ifscCode,
                       String phoneNumber, String panNumber, String imageURL,
                       String accountNumber, LatLng location,
                       String openingTime, String closingTime, String isOpen,
                       String address,
                       FoodCategory foodCategory, String qrCodeURL,
                       double avgRating, int numberOfRatings) {
        super(name, email, password, ifscCode, phoneNumber, panNumber, imageURL,
                accountNumber,
                location, openingTime, closingTime, isOpen, address);
        this.foodCategory = foodCategory;
        this.qrCodeURL = qrCodeURL;
        this.avgRating = avgRating;
        this.numberOfRatings = numberOfRatings;
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

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public Map<String, Integer> getFoodMenu() {
        return foodMenu;
    }

    public void setFoodMenu(Map<String, Integer> foodMenu) {
        this.foodMenu = foodMenu;
    }

    @Override public String toString() {
        return "VendorOwner{" +
                "foodCategory=" + foodCategory +
                ", qrCodeURL='" + qrCodeURL + '\'' +
                "} " + super.toString();
    }
}

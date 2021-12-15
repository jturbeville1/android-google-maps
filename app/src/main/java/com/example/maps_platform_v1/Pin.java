package com.example.maps_platform_v1;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Array;
import java.util.ArrayList;

/**
 * Pin object is capable of storing a variety of information
 * about a location on the map. Pins can be inserted into
 * the database of pins.
 */
public class Pin {
    private int id;
    private String name;
    private String description;
    private LatLng latlng;
    private ArrayList<String> categories = new ArrayList<>();
    private int contributorId;
    private String address;
    private String city;
    private String state_province;
    private String country;
    private ArrayList<Photo> photos;
    private String phoneNumber;
    private String email;
    private double ratingSum = 0;
    private int ratingCount = 0;

    public Pin(){}

    private String[] getLocation(double latitude, double longitude) {
        return new String[]{"fake city", "fake state", "fake country"};
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public void addCategory(String category) {
        this.categories.add(category);
    }

    public Boolean hasCategory(String category) {
        return categories.contains(category);
    }

    public int getContributorId() {
        return contributorId;
    }

    public void setContributorId(int contributorId) { this.contributorId = contributorId; }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState_province() {
        return state_province;
    }

    public void setState_province(String state_province) {
        this.state_province = state_province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getRating() {
        return ratingSum / ratingCount;
    }

    public void addRating(double rating) {
        ratingSum += rating;
        ratingCount++;
    }

    public double getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(double ratingSum) {
        this.ratingSum = ratingSum;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }
}

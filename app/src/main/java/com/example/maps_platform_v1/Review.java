package com.example.maps_platform_v1;

/**
 * Review object stores the IDs of the pin it belongs
 * to and the user that added the review. It also stores
 * the rating and the review text. Reviews can be inserted
 * into the database.
 */
public class Review {
    private int pinId;
    private int contributorId;
    private double rating;
    private String review;

    public Review(int pinId, int contributorId, double rating, String review) {
        this.pinId = pinId;
        this.contributorId = contributorId;
        this.rating = rating;
        this.review = review;
    }

    public Review(){}

    public int getPinId() {
        return pinId;
    }

    public void setPinId(int pinId) {
        this.pinId = pinId;
    }

    public int getContributorId() {
        return contributorId;
    }

    public void setContributorId(int contributorId) {
        this.contributorId = contributorId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

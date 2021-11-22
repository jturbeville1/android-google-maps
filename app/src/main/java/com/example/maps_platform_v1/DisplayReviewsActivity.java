package com.example.maps_platform_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reviews);

        Intent intent = getIntent();
        int pinId = intent.getIntExtra("pinId", -1);

        if(pinId != -1) {
            ReviewDatabaseHandler reviewDatabaseHandler = new ReviewDatabaseHandler(DisplayReviewsActivity.this);
            ArrayList<Review> reviews = reviewDatabaseHandler.getPinReviews(pinId);
            if(!reviews.isEmpty()) {
                String reviewsString = "username\nRating: " + String.valueOf(reviews.get(0).getRating()) +
                        "/5\n" + reviews.get(0).getReview();
                for (int i = 1; i < reviews.size(); i++) {
                    Review curReview = reviews.get(i);
                    reviewsString += "\n\nusername\nRating: " + String.valueOf(curReview.getRating()) +
                            "/5\n" + curReview.getReview();
                }
                TextView reviewsText = (TextView) findViewById(R.id.reviewsText);
                reviewsText.setText(reviewsString);
            }
        }
    }
}
package com.example.maps_platform_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class AddRatingReviewActivity extends AppCompatActivity {
    private EditText ratingEntry;
    private EditText reviewEntry;
    private int pinId;
    private int userId;
    private double rating;
    private String reviewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rating_review);
        Intent intent = getIntent();
        pinId = intent.getIntExtra("pinId", -1);
        userId = intent.getIntExtra("userId", -1);

        if(pinId != -1 && userId != -1) {
            ratingEntry = (EditText) findViewById(R.id.newRatingEntry);
            reviewEntry = (EditText) findViewById(R.id.reviewEntry);
            String ratingString = ratingEntry.getText().toString();
            try {
                rating = Double.parseDouble(ratingString);
                if(pinId != -1 && userId != -1 && rating >= 0 && rating <= 5) {
                    PinDatabaseHandler pinDatabaseHandler = new PinDatabaseHandler(AddRatingReviewActivity.this);
                    Pin pin = pinDatabaseHandler.search(pinId);
                    if(pin != null) {
                        //Add update method to pin database
                    }
                }
            }
        }
    }
}
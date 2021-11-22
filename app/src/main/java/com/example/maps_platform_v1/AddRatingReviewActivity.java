package com.example.maps_platform_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddRatingReviewActivity extends AppCompatActivity {
    private EditText ratingEntry;
    private EditText reviewEntry;
    private Button submitRatingReviewButton;
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

        submitRatingReviewButton = (Button) findViewById(R.id.submitRatingReviewButton);
        submitRatingReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pinId != -1 && userId != -1) {
                    ratingEntry = (EditText) findViewById(R.id.newRatingEntry);
                    reviewEntry = (EditText) findViewById(R.id.newReviewEntry);
                    String ratingString = ratingEntry.getText().toString();
                    reviewText = reviewEntry.getText().toString();
//                    try {
                        rating = Double.parseDouble(ratingString);
                        if(pinId != -1 && userId != -1 && rating >= 0 && rating <= 5) {
                            Log.e("ifSatisfied", "Satisfied");
                            PinDatabaseHandler pinDatabaseHandler = new PinDatabaseHandler(AddRatingReviewActivity.this);
                            Pin pin = pinDatabaseHandler.search(pinId);
                            String idString = String.valueOf(pin.getId());
                            Log.e("StringId", idString);
                            if(pin != null) {
                                Log.e("PinNotNull", "PinNotNull");
//                                pin.setName("Test Update");
                                pin.addRating(rating);
//                                pinDatabaseHandler.update(pin);
                                if(!reviewText.equals("")) {
                                    Review review = new Review(pinId, userId, rating, reviewText);
                                    ReviewDatabaseHandler reviewDatabaseHandler = new ReviewDatabaseHandler(AddRatingReviewActivity.this);
                                    long id = reviewDatabaseHandler.insert(review);
                                    Log.e("ReviewId", String.valueOf(id));
                                }
                            }
                        }
//                    }
//                    catch (Exception ignored) {}
                }
                Intent returnToMap = new Intent(AddRatingReviewActivity.this, MapsActivity.class);
                startActivity(returnToMap);
            }
        });
    }
}
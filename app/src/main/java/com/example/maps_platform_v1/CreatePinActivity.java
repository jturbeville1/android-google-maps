package com.example.maps_platform_v1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

public class CreatePinActivity extends AppCompatActivity {
    private Pin newPin = new Pin();
    private EditText nameEntry;
    private EditText descriptionEntry;
    private CheckBox category0Entry;
    private CheckBox category1Entry;
    private CheckBox category2Entry;
    private CheckBox category3Entry;
    private CheckBox category4Entry;
    private EditText ratingEntry;
    private EditText reviewEntry;
    private Button takePhotosButton;
    private Button chooseFromGalleryButton;
    private Button returnToMapButton;
    private double latitude;
    private double longitude;
    private final ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private final ActivityResultLauncher<Intent> getPhotoTaken = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent resultIntent = result.getData();
                    if(resultIntent != null) {
                        Bundle extras = resultIntent.getExtras();
                        if(extras != null) {
                            Bitmap bitmap = (Bitmap) extras.get("data");
                            bitmaps.add(bitmap);
                        }
                    }
                }
            });
    private final ActivityResultLauncher<Intent> getPhotoFromGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        Intent resultIntent = result.getData();
                        assert resultIntent != null;
                        Bundle extras = resultIntent.getExtras();
                        assert extras != null;
                        Bitmap bitmap = (Bitmap) extras.get("data");
                        bitmaps.add(bitmap);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);
        latitude = getIntent().getDoubleExtra("latitude", -1);
        longitude = getIntent().getDoubleExtra("longitude", -1);

        takePhotosButton = (Button)findViewById(R.id.takePhotosButton);
        takePhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                getPhotoTaken.launch(cameraIntent);
            }
        });

        chooseFromGalleryButton = (Button)findViewById(R.id.chooseFromGalleryButton);
        chooseFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getPhotoFromGallery.launch(pickPhoto);
            }
        });

        returnToMapButton = (Button)findViewById(R.id.returnToMapButton);
        returnToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameEntry = (EditText)findViewById(R.id.nameEntry);
                newPin.setName(nameEntry.getText().toString());

                newPin.setLatlng(new LatLng(latitude, longitude));
                descriptionEntry = (EditText)findViewById((R.id.descriptionEntry));
                newPin.setDescription(descriptionEntry.getText().toString());

                ArrayList<String> categories = new ArrayList<>();
                category0Entry = (CheckBox)findViewById(R.id.category0Entry);
                if(category0Entry.isChecked()) {
                    categories.add(category0Entry.getText().toString());
                }
                category1Entry = (CheckBox)findViewById(R.id.category1Entry);
                if(category1Entry.isChecked()) {
                    categories.add(category1Entry.getText().toString());
                }
                category2Entry = (CheckBox)findViewById(R.id.category2Entry);
                if(category2Entry.isChecked()) {
                    categories.add(category2Entry.getText().toString());
                }
                category3Entry = (CheckBox)findViewById(R.id.category3Entry);
                if(category3Entry.isChecked()) {
                    categories.add(category3Entry.getText().toString());
                }
                category4Entry = (CheckBox)findViewById(R.id.category4Entry);
                if(category4Entry.isChecked()) {
                    categories.add(category4Entry.getText().toString());
                }
                newPin.setCategories(categories);

                ratingEntry = (EditText)findViewById(R.id.ratingEntry);
                double rating = Double.parseDouble(ratingEntry.getText().toString());
                newPin.addRating(rating);

                PinDatabaseHandler pinDatabaseHandler = new PinDatabaseHandler(CreatePinActivity.this);
                long id = pinDatabaseHandler.insert(newPin);
                if(id != -1) {
                    reviewEntry = (EditText) findViewById(R.id.reviewEntry);
                    String reviewText = reviewEntry.getText().toString();
                    if(!reviewText.equals("")) {
                        ReviewDatabaseHandler reviewDatabaseHandler = new ReviewDatabaseHandler(CreatePinActivity.this);
                        Review review = new Review((int) id, -1, rating, reviewText);
                        reviewDatabaseHandler.insert(review);
                    }
                    if(!bitmaps.isEmpty()) {
                        for(Bitmap bitmap : bitmaps) {
                            PhotoDatabaseHandler photoDatabaseHandler = new PhotoDatabaseHandler(CreatePinActivity.this);
                            Photo photo = new Photo((int) id, -1, bitmap);
                            photoDatabaseHandler.insert(photo);
                        }
                    }
                }
                Intent returnIdPosition = new Intent(CreatePinActivity.this, MapsActivity.class);
                returnIdPosition.putExtra("id", (int) id);
                returnIdPosition.putExtra("latitude", latitude);
                returnIdPosition.putExtra("longitude", longitude);
                startActivity(returnIdPosition);
            }
        });
    }
}
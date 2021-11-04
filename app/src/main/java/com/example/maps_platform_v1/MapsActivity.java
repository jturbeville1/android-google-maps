package com.example.maps_platform_v1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ScrollView pinInfoScrollview;
    private TextView pinTitle;
    private TextView pinRating;
    private TextView pinLocation;
    private TextView pinCategories;
    private TextView pinDescription;
    private TextView pinRating2;
    private TextView pinReview1;
    private TextView pinReview2;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private Button dropPinButton;
    private Button filterButton;
    private LinearLayout filterLayout;
    private ArrayList<Pin> pins;
    private ArrayList<MarkerOptions> markers;
    private static final int REQUEST_CODE = 0;
//    private ActivityResultLauncher<Intent> getPinIdPosition = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if(result.getResultCode() == RESULT_OK) {
//                        Intent idPositionIntent = result.getData();
//                        int id = idPositionIntent.getIntExtra("id", -1);
//                        double latitude = idPositionIntent.getDoubleExtra("latitude", -1);
//                        double longitude = idPositionIntent.getDoubleExtra("longitude", -1);
//                        MarkerOptions markerOptions = new MarkerOptions()
//                                .position(new LatLng(latitude, longitude)).title(String.valueOf(id));
//                        markers.add(markerOptions);
//                        mMap.addMarker(markerOptions);
//                    }
//                }
//            });;
    private LatLng newPinLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Hide pin info until a pin is clicked
        pinInfoScrollview = (ScrollView)findViewById(R.id.pinInfoScrollview);
        pinInfoScrollview.setVisibility(View.GONE);
        filterLayout = (LinearLayout)findViewById(R.id.filterLayout);
        filterLayout.setVisibility(View.GONE);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        mMap = googleMap;

        //Create a pin for demonstration
        Pin connCollege = new Pin();
        connCollege.setId(0);
        LatLng loc = new LatLng(41.379044103233, -72.097063846886);
        connCollege.setLatlng(loc);
        connCollege.addRating(5);
        connCollege.setName("Connecticut College");
        connCollege.setCity("New London");
        connCollege.setState_province("CT");
        connCollege.setCountry("USA");
        connCollege.addCategory("College/University");
//        connCollege.setDescription("A small liberal arts college that belongs to the NESCAC. " +
//                "Approximately 2,000 undergraduates attend the school as full-time students. Most popular " +
//                "majors include Economics, Psychology, and Computer Science.");
//        Review r1 = new Review("testUser", 4, "I always loved Connecticut College! " +
//                "The community was great and so were my professors. Also, the campus is fantastic.");
//        Review r2 = new Review("testUser", 4.5, "I attended Conn many years " +
//                "ago. The buildings have changed but the values that the school and students embody " +
//                "will never change. Great place to get a degree!");
//        connCollege.addReview(r1);
//        connCollege.addReview(r2);
        PinDatabaseHandler pinDatabaseHandler = new PinDatabaseHandler(MapsActivity.this);
//        pinDatabaseHandler.insert(connCollege);
        pins = pinDatabaseHandler.getPins();
        markers = new ArrayList<>();

        //Add a marker for each location. Set marker title equal to the pin's ID
        for(Pin pin : pins) {
            MarkerOptions marker = new MarkerOptions().position(pin.getLatlng()).title(String.valueOf(pin.getId()));
            mMap.addMarker(marker);
            markers.add(marker);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13.5f));

        filterButton = (Button)findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterLayout.setVisibility(View.VISIBLE);

                Button submitFilterButton = (Button)findViewById(R.id.submitFilterButton);
                submitFilterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> filterCategories = new ArrayList<>();
                        CheckBox category0Filter = (CheckBox)findViewById(R.id.category0Filter);
                        if(category0Filter.isChecked()) {
                            filterCategories.add(category0Filter.getText().toString());
                        }
                        CheckBox category1Filter = (CheckBox)findViewById(R.id.category1Filter);
                        if(category1Filter.isChecked()) {
                            filterCategories.add(category1Filter.getText().toString());
                        }
                        CheckBox category2Filter = (CheckBox)findViewById(R.id.category2Filter);
                        if(category2Filter.isChecked()) {
                            filterCategories.add(category2Filter.getText().toString());
                        }
                        CheckBox category3Filter = (CheckBox)findViewById(R.id.category3Filter);
                        if(category3Filter.isChecked()) {
                            filterCategories.add(category3Filter.getText().toString());
                        }
                        CheckBox category4Filter = (CheckBox)findViewById(R.id.category4Filter);
                        if(category4Filter.isChecked()) {
                            filterCategories.add(category4Filter.getText().toString());
                        }

                        for(MarkerOptions marker : markers) {
                            String IdString = marker.getTitle();
                            boolean hasCategory = false;
                            for(Pin pin : pins) {
                                if(String.valueOf(pin.getId()).equals(IdString)) {
                                    for(String category : filterCategories) {
                                        if(pin.hasCategory(category)) {
                                            hasCategory = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if(hasCategory && !marker.isVisible()) {
                                marker.visible(true);
                            }
                            else if(!hasCategory && marker.isVisible()) {
                                marker.visible(false);
                            }
                        }
                        filterLayout.setVisibility(View.GONE);
                    }
                });
            }
        });

        dropPinButton = (Button)findViewById(R.id.dropPinButton);
        dropPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        newPinLatLng = latLng;
                        Log.i("onClick", "Om click method activated.");
                        Intent createPinIntent = new Intent(MapsActivity.this, CreatePinActivity.class);
                        createPinIntent.putExtra("latitude", latLng.latitude);
                        createPinIntent.putExtra("longitude", latLng.longitude);
                        startActivity(createPinIntent);
                    }
                });
            }
        });

        //Wait for a marker to be clicked
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Pin curPin = new Pin();
                for(Pin pin : pins) {
                    if(String.valueOf(pin.getId()).equals(marker.getTitle())) {
                        curPin = pin;
                        break;
                    }
                }

                if(curPin.getName() == null) {
                    return false;
                }
                else {
                    pinTitle = (TextView) findViewById(R.id.pinTitle);
                    pinTitle.setText(curPin.getName());

                    pinRating = (TextView) findViewById(R.id.pinRating);
                    String ratingString = "";
                    if(curPin.getRatingCount() != 0) {
                        Double rating = curPin.getRating();
                        ratingString = "Rating = " + String.valueOf(rating) + "/5";
                    }
                    else {
                        ratingString = "No ratings";
                    }
                    pinRating.setText(ratingString);

                    pinLocation = (TextView) findViewById(R.id.pinLocation);
                    String location = curPin.getCity() + ", " + curPin.getState_province() +
                            ", " + curPin.getCountry();
                    pinLocation.setText(location);

                    pinCategories = (TextView) findViewById(R.id.pinCategories);
                    ArrayList<String> categories = curPin.getCategories();
                    StringBuilder categoriesString = new StringBuilder(categories.get(0));
                    for (int i = 1; i < categories.size(); i++) {
                        categoriesString.append(", ").append(categories.get(i));
                    }
                    pinCategories.setText(categoriesString.toString());

                    pinDescription = (TextView)findViewById(R.id.pinDescription);
                    pinDescription.setText(curPin.getDescription());

                    pinRating2 = (TextView)findViewById(R.id.pinRating2);
                    pinRating2.setText(ratingString);

                    pinReview1 = (TextView)findViewById(R.id.pinReview1);
                    pinReview2 = (TextView)findViewById(R.id.pinReview2);

                    ReviewDatabaseHandler reviewDatabaseHandler = new ReviewDatabaseHandler(MapsActivity.this);
                    ArrayList<Review> reviews = reviewDatabaseHandler.getPinReviews(curPin.getId());

                    if(!reviews.isEmpty()) {
                        pinReview1.setText('"' + reviews.get(0).getReview() + '"');
                        if(reviews.size() > 1) {
                            pinReview2.setText('"' + reviews.get(1).getReview() + "'");
                        }
                        else {
                            pinReview2.setText("");
                        }
                    }
                    else {
                        pinReview1.setText("No reviews");
                        pinReview2.setText("");
                    }
                    PhotoDatabaseHandler photoDatabaseHandler = new PhotoDatabaseHandler(MapsActivity.this);
                    ArrayList<Photo> photos = photoDatabaseHandler.getPinPhotos(curPin.getId());
                    int i = 0;
                    imageView1 = (ImageView) findViewById(R.id.pinImage1);
                    imageView2 = (ImageView) findViewById(R.id.pinImage2);
                    imageView3 = (ImageView) findViewById(R.id.pinImage3);
                    if(photos.size() >= 1) {
                        imageView1.setImageBitmap(photos.get(0).getPhoto());
                        if (photos.size() >= 2) {
                            imageView2.setImageBitmap(photos.get(1).getPhoto());
                            if(photos.size() >= 3) {
                                imageView3.setImageBitmap(photos.get(2).getPhoto());
                            }
                            else {
                                imageView3.setVisibility(View.GONE);
                            }
                        }
                        else {
                            imageView2.setVisibility(View.GONE);
                            imageView3.setVisibility(View.GONE);
                        }
                    }
                    else {
                        imageView1.setVisibility(View.GONE);
                        imageView2.setVisibility(View.GONE);
                        imageView3.setVisibility(View.GONE);
                    }
                }

                dropPinButton.setVisibility(View.GONE);
                pinInfoScrollview.setVisibility(View.VISIBLE);

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        pinInfoScrollview.setVisibility(View.GONE);
                        dropPinButton.setVisibility(View.VISIBLE);
                    }
                });
                return true;
            }
        });
    }
}
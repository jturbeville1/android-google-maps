package com.example.maps_platform_v1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

    private GoogleMap mMap;  //Google Map interface
    private ScrollView pinInfoScrollview;  //Scrollview that appears when pin is clicked
    private TextView pinTitle;  //title displayed in pinInfoScrollView
    private TextView pinRating;  //rating displayed in pinInfoScrollView
    private TextView pinLocation;  //city, state, country displayed in pinInfoScrollView
    private TextView pinCategories;  //categories displayed in pinInfoScrollView
    private TextView pinDescription;  //description displayed in pinInfoScrollView
    private TextView pinRating2;  //rating displayed in the reviews section of pinInfoScrollView
    private TextView pinReview1;  //first review displayed in pinInfoScrollView
    private TextView pinReview2;  //second review displayed in pinInfoScrollView
    private ImageView imageView1;  //first image displayed in pinInfoScrollView
    private ImageView imageView2;  //second image displayed in pinInfoScrollView
    private ImageView imageView3;  //third image displayed in pinInfoScrollView
    private Button dropPinButton;  //button that triggers dropPin function
    private Button filterButton;  //button that triggers filter function
    private Button searchButton;  //button that triggers search function
    private Button addRatingReviewButton;
    private EditText searchEditText;  //search bar at the top of map
    private LinearLayout filterLayout;  //appears when filter button is clicked
    private ArrayList<Pin> pins;  //list of pins from the database
    private ArrayList<Marker> markers;  //list of markers on the map corresponding to pins
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //triggered when maps activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        UserDatabaseHandler userDatabaseHandler = new UserDatabaseHandler(MapsActivity.this);
        user = userDatabaseHandler.getUser(username, password);

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

        LatLng loc = new LatLng(41.379044103233, -72.097063846886);
//        Create a pin for demonstration
//        Pin connCollege = new Pin();
//        connCollege.setId(0);
//        connCollege.setLatlng(loc);
//        connCollege.addRating(5);
//        connCollege.setName("Connecticut College");
//        connCollege.setCity("New London");
//        connCollege.setState_province("CT");
//        connCollege.setCountry("USA");
//        connCollege.addCategory("College/University");
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
//        pinDatabaseHandler.insert(connCollege);

        //retrieve pins from database
        PinDatabaseHandler pinDatabaseHandler = new PinDatabaseHandler(MapsActivity.this);
        pins = pinDatabaseHandler.getPins();
        markers = new ArrayList<>();

        /*
         * Add a marker for each location.
         * Set marker title equal to the pin's ID.
         * Store markers to ArrayList.
         */
        for(Pin pin : pins) {
            MarkerOptions markerOptions = new MarkerOptions().position(pin.getLatlng()).title(String.valueOf(pin.getId()));
            Marker marker = mMap.addMarker(markerOptions);
            markers.add(marker);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13.5f));

        //filter button brings filterLayout to front of view
        filterButton = (Button)findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterLayout.setVisibility(View.VISIBLE);

                /*
                 * Create ArrayList to store filter categories.
                 * Loop through category checkboxes
                 * Add category to ArrayList if box is checked
                 */
                ArrayList<String> filterCategories = new ArrayList<>();
                Button submitFilterButton = (Button)findViewById(R.id.submitFilterButton);
                submitFilterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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

                        for(Marker marker : markers) {
                            marker.setVisible(true);
                        }

                        /*
                         * Create ArrayList of pins to filter out
                         * Loop through pins
                         * add pin to ArrayList if does not contain a category
                         */
                        if(!filterCategories.isEmpty()) {
                            ArrayList<Integer> removePins = new ArrayList<>();
                            for (Pin pin : pins) {
                                boolean hasCategory = false;
                                for (String category : filterCategories) {
                                    if (pin.hasCategory(category)) {
                                        hasCategory = true;
                                    }
                                }
                                if (!hasCategory) {
                                    removePins.add(pin.getId());
                                }
                            }

                            //remove the markers corresponding to the pins
                            for (int id : removePins) {
                                for (Marker marker : markers) {
                                    if (marker.getTitle().equals(String.valueOf(id))) {
                                        marker.setVisible(false);
                                    }
                                }
                            }
                        }
                        filterLayout.setVisibility(View.GONE);
                    }
                });
            }
        });

        /*
         * When clicked...
         * Waits for user to click map
         * Calls activity that creates a pin
         */
        dropPinButton = (Button)findViewById(R.id.dropPinButton);
        dropPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        Log.i("onClick", "Om click method activated.");
                        Intent createPinIntent = new Intent(MapsActivity.this, CreatePinActivity.class);
                        createPinIntent.putExtra("latitude", latLng.latitude);
                        createPinIntent.putExtra("longitude", latLng.longitude);
                        startActivity(createPinIntent);
                    }
                });
            }
        });

        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        /*
         * When clicked...
         * Takes text from search bar.
         * Finds pin with matching name if exists
         * Else finds pin names that contain search text
         */
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = searchEditText.getText().toString();
                for(Marker marker : markers) {
                    marker.setVisible(false);
                }
                ArrayList<Pin> specificPin = new ArrayList<>();
                ArrayList<Pin> similarPins = new ArrayList<>();
                for(Pin pin : pins) {
                    if(pin.getName().toLowerCase().equals(searchText.toLowerCase())) {
                        specificPin.add(pin);
                    }
                    else if(pin.getName().toLowerCase().contains(searchText.toLowerCase())) {
                        similarPins.add(pin);
                    }
                }
                if(!specificPin.isEmpty()) {
                    displayPins(specificPin);
                }
                else if(!similarPins.isEmpty()) {
                    displayPins(similarPins);
                }
            }
        });

        //Displays pin info when corresponding marker is clicked
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
                        ratingString = "0 ratings";
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

                addRatingReviewButton = (Button) findViewById(R.id.addRatingReviewButton);
                Pin finalCurPin = curPin;
                addRatingReviewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MapsActivity.this, AddRatingReviewActivity.class);
                        intent.putExtra("pinId", finalCurPin.getId());
//                        int userId = -1;
//                        if(user != null) {
                            intent.putExtra("userId", 0);
//                        }
//                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                });
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

    public void displayPins(ArrayList<Pin> displayPins) {
        for(Pin pin : displayPins) {
            for(Marker marker : markers) {
                Log.e("CurrentId", String.valueOf(pin.getId()));
                Log.e("CurrentId", marker.getTitle());
                if(marker.getTitle().equals(String.valueOf(pin.getId()))) {
                    marker.setVisible(true);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
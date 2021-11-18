package com.example.maps_platform_v1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PinDatabaseHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "PinsTable";
    private static final String PINS_TABLE = "PINS_TABLE";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String CATEGORIES = "categories";
    private static final String CONTRIBUTOR_ID = "contributorId";
    private static final String ADDRESS = "address";
    private static final String CITY = "city";
    private static final String STATE_PROVINCE = "state_province";
    private static final String COUNTRY = "country";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String EMAIL = "email";
    private static final String RATING_SUM = "ratingSum";
    private static final String RATING_COUNT = "ratingCount";

    public PinDatabaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableString = "CREATE TABLE " + PINS_TABLE + " (" + ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME + " TEXT, " +
                DESCRIPTION + " TEXT, " + LATITUDE + " TEXT, " + LONGITUDE +
                " TEXT, " + CATEGORIES + " TEXT, " + CONTRIBUTOR_ID +
                " INTEGER, " + ADDRESS + " TEXT, " + CITY + " TEXT, " +
                STATE_PROVINCE + " TEXT, " + COUNTRY + " TEXT, " +
                PHONE_NUMBER + " TEXT, " + EMAIL + " TEXT, " + RATING_SUM +
                " TEXT, " + RATING_COUNT + " TEXT)";
        sqLiteDatabase.execSQL(tableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP TABLE IF EXISTS " + PINS_TABLE);
        onCreate(db);
    }

    public long insert(Pin pin) {
        try(SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();


            contentValues.put(NAME, pin.getName());


            contentValues.put(DESCRIPTION, pin.getDescription());


            LatLng latLng = pin.getLatlng();
            contentValues.put(LATITUDE, String.valueOf(latLng.latitude));
            contentValues.put(LONGITUDE, String.valueOf(latLng.longitude));

            ArrayList<String> categories = pin.getCategories();
            StringBuilder categoriesString = new StringBuilder(categories.get(0));
            for (int i = 1; i < categories.size(); i++) {
                categoriesString.append(",").append(categories.get(i));
            }
            contentValues.put(CATEGORIES, categoriesString.toString());


            contentValues.put(CONTRIBUTOR_ID, pin.getContributorId());


//            contentValues.put(ADDRESS, pin.getAddress());

            contentValues.put(CITY, pin.getCity());
            contentValues.put(STATE_PROVINCE, pin.getState_province());

            contentValues.put(COUNTRY, pin.getCountry());
//            contentValues.put(PHONE_NUMBER, "null");
//            contentValues.put(EMAIL, "null");
            contentValues.put(RATING_SUM, String.valueOf(pin.getRatingSum()));
            contentValues.put(RATING_COUNT, String.valueOf(pin.getRatingCount()));
            return db.insert(PINS_TABLE, null, contentValues);
        }
    }

    public Pin search(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(PINS_TABLE, new String[]{ID, NAME, DESCRIPTION, LATITUDE, LONGITUDE,
                        CATEGORIES, CONTRIBUTOR_ID, ADDRESS, CITY, STATE_PROVINCE, COUNTRY,
                        PHONE_NUMBER, EMAIL, RATING_SUM, RATING_COUNT}, ID + "= ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Pin pin = new Pin();

        if (cursor == null) {
            return null;
        }
        else {
            cursor .moveToNext();
            pin.setId(cursor.getInt(0));
            if(cursor.getString(1) != null) {
                pin.setName(cursor.getString(1));
            }
            if(cursor.getString(2) != null) {
                pin.setDescription(cursor.getString(2));
            }
            LatLng latLng = new LatLng(Double.parseDouble(cursor.getString(3)),
                    Double.parseDouble(cursor.getString(4)));
            pin.setLatlng(latLng);
            if(cursor.getString(5) != null) {
                String[] categories = cursor.getString(5).split(",");
                for (String category : categories) {
                    pin.addCategory(category);
                }
            }
            if(cursor.getInt(6) > -1) {
                pin.setContributorId(cursor.getInt(6));
            }
            if(cursor.getString(7) != null) {
                pin.setAddress(cursor.getString(7));
            }
            if(cursor.getString(8) != null) {
                pin.setCity(cursor.getString(8));
            }
            if(cursor.getString(9) != null) {
                pin.setState_province(cursor.getString(9));
            }
            if(cursor.getString(10) != null) {
                pin.setCountry(cursor.getString(10));
            }
            if(cursor.getString(11) != null) {
                pin.setPhoneNumber(cursor.getString(11));
            }
            if(cursor.getString(12) != null) {
                pin.setEmail(cursor.getString(12));
            }
            if(cursor.getString(13) != null) {
                pin.setRatingSum(Integer.parseInt(cursor.getString(10)));
            }
            if(cursor.getString(14) != null) {
                pin.setRatingCount(Integer.parseInt(cursor.getString(14)));
            }

            return pin;
        }
    }

    public ArrayList<Pin> getPins() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + ID + ", " + NAME + ", " + DESCRIPTION +
                ", " + LATITUDE + ", " + LONGITUDE + ", " + CATEGORIES + ", " +
                CONTRIBUTOR_ID + ", " + ADDRESS + ", " + CITY + ", " +
                STATE_PROVINCE + ", " + COUNTRY + " , " + PHONE_NUMBER + ", " +
                EMAIL + ", " + RATING_SUM + ", " + RATING_COUNT+ " FROM "+ PINS_TABLE; //ADDED id
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<Pin> pins = new ArrayList<>();
        while(cursor.moveToNext()) {
            Pin pin = new Pin();

            pin.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            pin.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            pin.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));

            double latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(LATITUDE)));
            double longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
            LatLng latLng = new LatLng(latitude, longitude);
            pin.setLatlng(latLng);

            String categoriesString = cursor.getString(cursor.getColumnIndex(CATEGORIES));
            String[] categories = categoriesString.split(",");
            for(String category : categories) {
                pin.addCategory(category);
            }

            pin.setContributorId(cursor.getInt(cursor.getColumnIndex(CONTRIBUTOR_ID)));
//            pin.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
            pin.setCity(cursor.getString(cursor.getColumnIndex(CITY)));
            pin.setState_province(cursor.getString(cursor.getColumnIndex(STATE_PROVINCE)));
            pin.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
//            pin.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
//            pin.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
            pin.setRatingSum(Double.parseDouble(cursor.getString(cursor.getColumnIndex(RATING_SUM))));
            pin.setRatingCount(Double.parseDouble(cursor.getString(cursor.getColumnIndex(RATING_COUNT))));
            pins.add(pin);
        }
        cursor.close();
        return pins;
    }
}

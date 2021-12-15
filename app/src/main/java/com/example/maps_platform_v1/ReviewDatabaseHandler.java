package com.example.maps_platform_v1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ReviewDatabaseHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "ReviewsTable";
    private static final String REVIEWS_TABLE = "REVIEWS_TABLE";
    private static final String ID = "id";
    private static final String PIN_ID = "pinId";
    private static final String CONTRIBUTOR_ID = "contributorId";
    private static final String RATING = "rating";
    private static final String REVIEW = "review";

    /**
     * This class is used to insert and retrieve reviews
     * from the database.
     */
    public ReviewDatabaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableString = "CREATE TABLE " + REVIEWS_TABLE + " (" + ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + PIN_ID +
                " INTEGER, " + CONTRIBUTOR_ID + " INTEGER, " + RATING +
                " REAL, " + REVIEW + " TEXT)";
        sqLiteDatabase.execSQL(tableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + REVIEWS_TABLE);
        onCreate(db);
    }

    public long insert(Review review) {
        try(SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PIN_ID, review.getPinId());
            contentValues.put(CONTRIBUTOR_ID, review.getContributorId());
            contentValues.put(RATING, review.getRating());
            contentValues.put(REVIEW, review.getReview());

            return db.insert(REVIEWS_TABLE, null, contentValues);
        }
    }

    public ArrayList<Review> getPinReviews(int pinId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + PIN_ID + ", " + CONTRIBUTOR_ID + ", " +
                RATING + ", " + REVIEW + " FROM " + REVIEWS_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Review> reviews = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(PIN_ID));
            if(id == pinId) {
                Review review = new Review(id,
                        cursor.getInt(cursor.getColumnIndex(CONTRIBUTOR_ID)),
                        cursor.getDouble(cursor.getColumnIndex(RATING)),
                        cursor.getString(cursor.getColumnIndex(REVIEW)));
                reviews.add(review);
            }
        }
        return reviews;
    }
}
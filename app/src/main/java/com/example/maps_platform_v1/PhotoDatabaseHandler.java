package com.example.maps_platform_v1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PhotoDatabaseHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "PhotosTable";
    private static final String PHOTOS_TABLE = "PHOTOS_TABLE";
    private static final String ID = "id";
    private static final String PIN_ID = "pinId";
    private static final String CONTRIBUTOR_ID = "contributorId";
    private static final String PHOTO = "bitmap";

    public PhotoDatabaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableString = "CREATE TABLE " + PHOTOS_TABLE + " (" + ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + PIN_ID +
                " INTEGER, " + CONTRIBUTOR_ID + " INTEGER, " + PHOTO +
                " BLOB)";
        sqLiteDatabase.execSQL(tableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + PHOTOS_TABLE);
        onCreate(db);
    }

    public long insert(Photo photo) {
        try(SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PIN_ID, photo.getPinId());
            contentValues.put(CONTRIBUTOR_ID, photo.getContributorId());

            Bitmap bitmap = photo.getPhoto();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byte[] byteArray = stream.toByteArray();
            contentValues.put(PHOTO, byteArray);

            return db.insert(PHOTOS_TABLE, null, contentValues);
        }
    }

    public ArrayList<Photo> getPinPhotos(int pinId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + PIN_ID + ", " + CONTRIBUTOR_ID + ", " +
                PHOTO + " FROM " + PHOTOS_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Photo> photos = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(PIN_ID));
            if(id == pinId) {
                int contributorId = cursor.getInt(cursor.getColumnIndex(CONTRIBUTOR_ID));
                byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(PHOTO));
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                Photo photo = new Photo(id, contributorId, bitmap);
                photos.add(photo);
            }
        }
        return photos;
    }
}
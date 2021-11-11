package com.example.maps_platform_v1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UserDatabaseHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "UsersTable";
    private static final String USERS_TABLE = "USERS_TABLE";
    private static final String ID = "id";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String ADDRESS = "address";
    private static final String CITY = "city";
    private static final String STATE_PROVINCE = "state_province";
    private static final String COUNTRY = "country";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String EMAIL = "email";
    private static final String STATUS = "status";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public UserDatabaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableString = "CREATE TABLE " + USERS_TABLE + " (" + ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + FIRST_NAME + " TEXT, " +
                LAST_NAME + " TEXT, " + ADDRESS + " TEXT, " + CITY +
                " TEXT, " + STATE_PROVINCE + " TEXT, " + COUNTRY + " TEXT, " +
                PHONE_NUMBER + " TEXT, " + EMAIL + " TEXT, " + STATUS + " TEXT, " +
                USERNAME + " TEXT, " + PASSWORD + " TEXT)";
        sqLiteDatabase.execSQL(tableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        onCreate(db);
    }

    public long insert(User user) {
        try(SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FIRST_NAME, user.getFirstName());
            contentValues.put(LAST_NAME, user.getLastName());
            contentValues.put(CITY, user.getCity());
            contentValues.put(STATE_PROVINCE, user.getState_province());
            contentValues.put(COUNTRY, user.getCountry());
            contentValues.put(PHONE_NUMBER, user.getPhoneNumber());
            contentValues.put(EMAIL, user.getEmail());
            contentValues.put(USERNAME, user.getUsername());
            contentValues.put(PASSWORD, user.getPassword());
            return db.insert(USERS_TABLE, null, contentValues);
        }
    }

    public User getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + ID + ", " + FIRST_NAME + ", " + LAST_NAME +
                ", " + ADDRESS + ", " + CITY + ", " + STATE_PROVINCE + ", " +
                COUNTRY + ", " + PHONE_NUMBER + ", " + EMAIL + ", " + STATUS +
                ", " + USERNAME + ", " + PASSWORD + " FROM "+ USERS_TABLE;
        Cursor cursor = db.rawQuery(query,null);

        while(cursor.moveToNext()) {
            String curUsername = cursor.getString(cursor.getColumnIndex(USERNAME));
            String curPassword = cursor.getString(cursor.getColumnIndex(PASSWORD));
            Log.e("CurColumnIndex", String.valueOf(cursor.getColumnIndex(STATUS)));
            if(curUsername.equals(username) && curPassword.equals(password)) {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                user.setUsername(curUsername);
                user.setPassword(curPassword);
                user.setFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
                user.setLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
                user.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
                user.setCity(cursor.getString(cursor.getColumnIndex(CITY)));
                user.setState_province(cursor.getString(cursor.getColumnIndex(STATE_PROVINCE)));
                user.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
                user.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                cursor.close();
                return user;
            }
        }
        cursor.close();
        return null;
    }

    public ArrayList<String> getUsernames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + USERNAME + " FROM "+ USERS_TABLE;
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<String> usernames = new ArrayList<>();
        while(cursor.moveToNext()) {
            usernames.add(cursor.getString(cursor.getColumnIndex(USERNAME)));
        }

        return usernames;
    }

}
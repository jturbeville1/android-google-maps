package com.example.maps_platform_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstNameEntry;
    private EditText lastNameEntry;
    private EditText usernameEntry;
    private EditText passwordEntry;
    private EditText emailEntry;
    private TextView usernameExistsText;
    private TextView passwordRequiredText;
    private Button createAccountButton;

    /**
     * This activity takes the information the user inputs
     * and creates an account in the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firstNameEntry = (EditText) findViewById(R.id.firstNameEntry);
        lastNameEntry = (EditText) findViewById(R.id.lastNameEntry);
        usernameEntry = (EditText) findViewById(R.id.newRatingEntry);
        passwordEntry = (EditText) findViewById(R.id.passwordEntry);
        emailEntry = (EditText) findViewById(R.id.emailEntry);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        usernameExistsText = (TextView) findViewById(R.id.usernameExistsText);
        passwordRequiredText = (TextView) findViewById(R.id.passwordRequiredText);
        usernameExistsText.setVisibility(View.GONE);
        passwordRequiredText.setVisibility(View.GONE);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEntry.getText().toString().toLowerCase();
                String password = passwordEntry.getText().toString();
                boolean emptyUsername = username.equals("");
                boolean emptyPassword = password.equals("");
                if(emptyUsername) {
                    usernameExistsText.setText("Please enter a username.");
                    usernameExistsText.setVisibility(View.VISIBLE);
                }
                if(emptyPassword) {
                    passwordRequiredText.setVisibility(View.VISIBLE);
                }
                if(!emptyUsername && !emptyPassword) {
                    UserDatabaseHandler userDatabaseHandler = new UserDatabaseHandler(CreateAccountActivity.this);
                    ArrayList<String> usernames = userDatabaseHandler.getUsernames();
                    boolean exists = false;
                    for (String u : usernames) {
                        if (u.equals(username)) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        usernameExistsText.setText("Username already exists.");
                        usernameExistsText.setVisibility(View.VISIBLE);
                    } else {
                        String firstName = firstNameEntry.getText().toString();
                        String lastName = lastNameEntry.getText().toString();
                        String email = emailEntry.getText().toString();

                        User newUser = new User(firstName, lastName, username, password);
                        newUser.setEmail(email);
                        userDatabaseHandler.insert(newUser);

                        Intent intent = new Intent(CreateAccountActivity.this, MapsActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
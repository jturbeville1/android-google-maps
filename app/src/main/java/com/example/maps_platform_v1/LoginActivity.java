package com.example.maps_platform_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEntry;
    private EditText passwordEntry;
    private Button signInButton;
    private TextView createNewAccountLink;
    private TextView invalidLoginMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserDatabaseHandler userDatabaseHandler = new UserDatabaseHandler(LoginActivity.this);
//        User myUser = new User("Justin", "Turbeville", "jturbeville", "Mapsmaggie1$");
//        myUser.setEmail("jturbevil@conncoll.edu");
//        userDatabaseHandler.insert(myUser);

        usernameEntry = (EditText) findViewById(R.id.newRatingEntry);
        passwordEntry = (EditText) findViewById(R.id.passwordEntry);
        signInButton = (Button) findViewById(R.id.submitRatingReviewButton);
        createNewAccountLink = (TextView) findViewById(R.id.createNewAccountLink);
        invalidLoginMessage = (TextView) findViewById(R.id.invalidMessageText);
        invalidLoginMessage.setVisibility(View.GONE);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validLogin = false;
                while(!validLogin) {
                    String username = usernameEntry.getText().toString().toLowerCase();
                    String password = passwordEntry.getText().toString();
                    User user = userDatabaseHandler.getUser(username, password);
                    if (user == null) {
                        invalidLoginMessage.setVisibility(View.VISIBLE);
                    } else {
                        validLogin = true;
                        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }
                }
            }
        });

        createNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
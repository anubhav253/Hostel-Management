package com.example.devil.hostelmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.devil.hostelmanagement.utils.PrefManager;

public class LoginActivity extends AppCompatActivity {

    private PrefManager prefManager;
    private static String log = "LogInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);
        Log.i("ON create", log);
        if (!prefManager.isLoggedIn()) {
            launchHomeScreen();
            finish();
        }
        setContentView(R.layout.activity_login);
    }

    private void launchHomeScreen() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public void SignIn(View view){
        //Read value from username and password
        //Make a rest api call
        //if rest api call is same then go to homeActivity othwise return to same page.
        //Also Set the logged in value in shared prefernce
        prefManager.setLoggedIn(true);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

}

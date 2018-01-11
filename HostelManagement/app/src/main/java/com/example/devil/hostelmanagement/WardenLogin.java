package com.example.devil.hostelmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WardenLogin extends AppCompatActivity {

    Button warden_sign_in_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warden_login);

        warden_sign_in_button = (Button) findViewById(R.id.warden_sign_in_button);

        warden_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WardenLogin.this,WardenScreen.class);
                startActivity(i);
            }
        });
    }

    public void WardeSignIn(View view) {
    }
}

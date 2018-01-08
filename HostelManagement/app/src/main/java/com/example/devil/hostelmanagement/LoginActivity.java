package com.example.devil.hostelmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devil.hostelmanagement.Remote.ApiUtils;
import com.example.devil.hostelmanagement.Remote.RetrofitClient;
import com.example.devil.hostelmanagement.Remote.UserService;
import com.example.devil.hostelmanagement.constants.ProjectConstants;
import com.example.devil.hostelmanagement.model.ResObj;
import com.example.devil.hostelmanagement.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText login_email;
    EditText login_password;
    Button sign_in_button;
    //UserService userService;

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
        TextView register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        login_email = (EditText) findViewById(R.id.login_email);
        login_password= (EditText) findViewById(R.id.login_password);
        sign_in_button = (Button) findViewById(R.id.sign_in_button);
        //userService = ApiUtils.getUserService();

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = login_email.getText().toString();
                String U_password = login_password.getText().toString();

                if(validateLogin(Email,U_password)){
                    //login
                    doLogin(Email,U_password);

                }
            }
        });
    }

    private boolean validateLogin(String Email, String U_password){
        if(Email == null || Email.trim().length() == 0){
            Toast.makeText(this, "Email address is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(U_password == null || U_password.trim().length() == 0){
            Toast.makeText(this, "Password is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

    private void doLogin(final String Email, final String U_password){
        String BASE_URL = ProjectConstants.BASE_URL;
        UserService userService = RetrofitClient.getClient(BASE_URL).create(UserService.class);
        JSONObject body = new JSONObject(); //yaha value baad mai daal denge abhi body empty jaega
        try {
            body.put("Email", Email);
            body.put("U_password", U_password);
        } catch (JSONException e){
            Log.i("JSON Object", "Something went wrong while adding json");
        }
        Call<JSONObject> call= userService.login(body);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if(response.code() == 200) {
                    Log.i("Flask Response", "" + response.code() + response.message());
                    JSONObject resObj = response.body();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("Email", "ada");
                    startActivity(i);
                }
                else if(response.code() == 400){
                        Toast.makeText(LoginActivity.this, "The user name or password is incorrect", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.i("Flask Response", "" + response.code() + response.message());
                    Toast.makeText(LoginActivity.this,"Error, Try again!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.i("Flask Response", "" + t.getMessage());
                Log.i("Flask Resposne err", t.getStackTrace().toString());
                Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

}

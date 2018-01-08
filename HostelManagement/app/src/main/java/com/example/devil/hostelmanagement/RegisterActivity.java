package com.example.devil.hostelmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.devil.hostelmanagement.Remote.RetrofitClient;
import com.example.devil.hostelmanagement.Remote.UserService;
import com.example.devil.hostelmanagement.constants.ProjectConstants;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_name;
    private EditText register_email;
    private EditText register_password;
    private EditText register_phone_number;
    private Button register;

    private String name;
    private String email;
    private String password;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_name = (EditText) findViewById(R.id.register_name);
        register_email= (EditText) findViewById(R.id.register_email);
        register_password = (EditText) findViewById(R.id.login_password);
        register_phone_number = (EditText) findViewById(R.id.register_phone_number);

        register = (Button)findViewById(R.id.register_form);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister();
            }
        });
    }

    private void doRegister(){
        name = register_name.getText().toString();
        email = register_email.getText().toString();
        password = register_password.getText().toString();
        phone = register_phone_number.getText().toString();

        if (name == "" ||  email == "" || password == "" || phone == ""){
            Toast.makeText(this, "Please provide all the details",Toast.LENGTH_SHORT).show();
        }


        String BASE_URL = ProjectConstants.BASE_URL;
        UserService userService = RetrofitClient.getClient(BASE_URL).create(UserService.class);
        JSONObject body = new JSONObject(); //yaha value baad mai daal denge abhi body empty jaega
        try {
            body.put("Email", email);
            body.put("password", password);
            body.put("fullName",name);
            body.put("mobile",phone);
        } catch (JSONException e){
            Log.i("JSON Object", "Something went wrong while adding json");
        }
        Call<JSONObject> call= userService.register(body);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if(response.code() == 200) {
                    Log.i("Flask Response", "" + response.code() + response.message());
                    JSONObject resObj = response.body();
                    Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(i);
                }
                else if(response.code() == 400){
                    Toast.makeText(RegisterActivity.this, "Invalid details or duplicate user.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.i("Flask Response", "" + response.code() + response.message());
                    Toast.makeText(RegisterActivity.this,"Error, Try again!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.i("Flask Response", "" + t.getMessage());
                Log.i("Flask Resposne err", t.getStackTrace().toString());
                Toast.makeText(RegisterActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

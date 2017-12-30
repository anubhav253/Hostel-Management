package com.example.devil.hostelmanagement.Remote;

/**
 * Created by DEVIL on 12/26/2017.
 */

public class ApiUtils {

    public static  final String BASE_URL = "http://172.0.0.1:8080/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
}

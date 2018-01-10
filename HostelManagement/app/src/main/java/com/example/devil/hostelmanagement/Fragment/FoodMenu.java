package com.example.devil.hostelmanagement.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devil.hostelmanagement.LoginActivity;
import com.example.devil.hostelmanagement.MainActivity;
import com.example.devil.hostelmanagement.R;
import com.example.devil.hostelmanagement.Remote.RetrofitClient;
import com.example.devil.hostelmanagement.Remote.UserService;
import com.example.devil.hostelmanagement.constants.ProjectConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoodMenu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoodMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodMenu extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g.

    private TextView food_date;
    private TextView food_breakfast;
    private TextView food_lunch;
    private TextView food_dinner;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FoodMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodMenu newInstance(String param1, String param2) {
        FoodMenu fragment = new FoodMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFoodData();
    }

    private  void getFoodData(){
        String BASE_URL = ProjectConstants.BASE_URL;
        UserService userService = RetrofitClient.getClient(BASE_URL).create(UserService.class);
        Call<JSONObject> call= userService.fooddata("1");
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if(response.code() == 200) {
                    Log.i("Flask Response", "" + response.code() + response.message());
                    JSONObject resObj = response.body();

                    //
                    food_date.setText("2018-01-10");
                    food_breakfast.setText("POHA");
                    food_lunch.setText("Kadi , Chane");
                    food_dinner.setText("Aaloo gobhi, Chana dal");

                    Log.i("Flask Response",resObj.toString());
//                    JSONArray breafast = resObj[0];
                }
                else if(response.code() == 400){
                    Toast.makeText(getActivity(), "The user name or password is incorrect", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.i("Flask Response", "" + response.code() + response.message());
                    Toast.makeText(getActivity(),"Error, Try again!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.i("Flask Response", "" + t.getMessage());
                Log.i("Flask Resposne err", t.getStackTrace().toString());
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_food_menu, container, false);

        food_date = (TextView)view.findViewById(R.id.food_date);
        food_breakfast = (TextView)view.findViewById(R.id.food_breakfast);
        food_lunch = (TextView)view.findViewById(R.id.food_lunch);
        food_dinner = (TextView)view.findViewById(R.id.food_dinner);

        getFoodData();
        return inflater.inflate(R.layout.fragment_food_menu, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onClick(View view) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

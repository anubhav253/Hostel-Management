package com.example.devil.hostelmanagement.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devil.hostelmanagement.LoginActivity;
import com.example.devil.hostelmanagement.MainActivity;
import com.example.devil.hostelmanagement.R;
import com.example.devil.hostelmanagement.Remote.RetrofitClient;
import com.example.devil.hostelmanagement.Remote.UserService;
import com.example.devil.hostelmanagement.constants.ProjectConstants;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ComplaintMenu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ComplaintMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComplaintMenu extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    private EditText complaint_name;
    private EditText complaint_room;
    private Spinner complaint_menu_list;
    private Spinner complaint_type_list;
    private EditText complaint_complain;
    private Button complaint_button;

    private String FullName;
    private String RoomNumber;
    private String complaint_menu;
    private String complaint_type;
    private String complain;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ComplaintMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComplaintMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static ComplaintMenu newInstance(String param1, String param2) {
        ComplaintMenu fragment = new ComplaintMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void getComplaint(View view) {
        FullName = complaint_name.getText().toString();
        RoomNumber = complaint_room.getText().toString();
        complain = complaint_complain.getText().toString();

        String BASE_URL = ProjectConstants.BASE_URL;
        UserService userService = RetrofitClient.getClient(BASE_URL).create(UserService.class);
        JSONObject body = new JSONObject();
        try {
            body.put("FullName", FullName);
            body.put("RoomNumber", RoomNumber);
            body.put("complain",complain);
            body.put("complaint_type", "food");
        } catch (JSONException e){
            Log.i("JSON Object", "Something went wrong while adding json");
        }
        Call<JSONObject> call= userService.complaint(body);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if(response.code() == 200) {
                    Log.i("Flask Response", "" + response.code() + response.message());
                    JSONObject resObj = response.body();
                    Intent i=new Intent(getActivity(),MainActivity.class);
                    startActivity(i);
                }
                else if(response.code() == 400){
                    Toast.makeText(getActivity(), "Invalid details or duplicate user.", Toast.LENGTH_SHORT).show();
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
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_complaint_menu, container, false);
        // Inflate the layout for this fragment
//        getComplaint(view);
        setTextViews(view);
        return view;
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

    public void setTextViews(View view) {
        complaint_name = (EditText)view.findViewById(R.id.complaint_name);
        complaint_room = (EditText)view.findViewById(R.id.complaint_room);
        complaint_complain = (EditText)view.findViewById(R.id.complaint_complain);

        complaint_button = (Button)view.findViewById(R.id.complaint_button);
        complaint_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getComplaint(view);
            }
        });
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

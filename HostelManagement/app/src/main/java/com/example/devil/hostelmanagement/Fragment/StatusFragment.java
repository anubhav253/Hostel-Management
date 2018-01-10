package com.example.devil.hostelmanagement.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devil.hostelmanagement.R;

public class StatusFragment extends BaseFragment {
    private TextView status;
    private void setTextViews(View view){
        status = (TextView)view.findViewById(R.id.status);

    }
}

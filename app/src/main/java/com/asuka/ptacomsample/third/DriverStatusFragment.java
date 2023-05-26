package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

public class DriverStatusFragment extends Fragment {
    private RadioGroup radioGroupDriverStatus, radioGroupCodriverStatus;
    private Integer driverStatus, codriverStatus;
    private String cmd;
    private static final String TAG = "DriverStatusFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_status, container, false);

        radioGroupDriverStatus = view.findViewById(R.id.DRBtnGroup);
        radioGroupCodriverStatus = view.findViewById(R.id.CRBtnGroup);

        radioGroupDriverStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                driverStatus = radioGroupDriverStatus.indexOfChild(view.findViewById(i));
                Log.d(TAG, "onCheckedChanged: driverStatus: " + driverStatus);

                cmd = "$LCD+DRIVERSTATUS=0," + driverStatus;
            }
        });

        radioGroupCodriverStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                codriverStatus = radioGroupCodriverStatus.indexOfChild(view.findViewById(i));
                Log.d(TAG, "onCheckedChanged: codriverStatus: " + codriverStatus);

                cmd = "$LCD+DRIVERSTATUS=1," + codriverStatus;
            }
        });



        return view;
    }



    public String getCmd() {
        return cmd;
    }
}

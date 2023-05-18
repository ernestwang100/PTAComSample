package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

public class DriverStatusFragment extends Fragment {
    private Spinner driverSpn, codriverSpn;
    private Integer driverStatus, codriverStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_status, container, false);

        driverSpn = view.findViewById(R.id.driverSpinner);
        codriverSpn = view.findViewById(R.id.codriverSpinner);

        driverSpn.setOnItemSelectedListener(driverSpnListener);
        codriverSpn.setOnItemSelectedListener(codriverSpnListener);

        return view;
    }

    private AdapterView.OnItemSelectedListener driverSpnListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            driverStatus = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener codriverSpnListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            codriverStatus = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}

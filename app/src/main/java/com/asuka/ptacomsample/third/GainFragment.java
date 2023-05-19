package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

public class GainFragment extends Fragment {
    private NumberPicker numberPicker;
    private String cmd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gain, container, false);

        numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(1000);
        numberPicker.setMinValue(1);
        int hour = numberPicker.getValue();



        return view;
    }
//    TODO: add this to the main activity
    @Override
    public void onResume() {
        super.onResume();

    }

    public String getCmd() {
        cmd = "$LCD+GAIN=" + numberPicker.getValue();
        return cmd;
    }
}

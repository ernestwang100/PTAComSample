package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

public class PrintFragment extends Fragment {
    private RadioGroup radioGroupPrint;
    private String cmd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_print, container, false);

        radioGroupPrint = view.findViewById(R.id.radioGroupPrint);

        switch (radioGroupPrint.getCheckedRadioButtonId()){
            case R.id.print1RB:
                cmd = "$LCD+PRINT=1";
                break;
            case R.id.print2RB:
                cmd = "$LCD+PRINT=2";
                break;
            case R.id.print3RB:
                cmd = "$LCD+PRINT=3";
                break;
            case R.id.print4RB:
                cmd = "$LCD+PRINT=4";
                break;
        }
        return view;
    }

    public String getCmd() {
        return cmd;
    }
}

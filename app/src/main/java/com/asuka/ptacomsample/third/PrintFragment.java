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
        radioGroupPrint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton print1RB = view.findViewById(R.id.print1RB);
                RadioButton print2RB = view.findViewById(R.id.print2RB);
                RadioButton print3RB = view.findViewById(R.id.print3RB);
                RadioButton print4RB = view.findViewById(R.id.print4RB);

                if (print1RB.isChecked()){
                    cmd = "$LCD+PRINT=1";
                } else if (print2RB.isChecked()){
                    cmd = "$LCD+PRINT=2";
                } else if (print3RB.isChecked()){
                    cmd = "$LCD+PRINT=3";
                } else if (print4RB.isChecked()){
                    cmd = "$LCD+PRINT=4";
                }
            }
        });

//        switch (radioGroupPrint.getCheckedRadioButtonId()){
//            case R.id.print1RB:
//                cmd = "$LCD+PRINT=1";
//                break;
//            case R.id.print2RB:
//                cmd = "$LCD+PRINT=2";
//                break;
//            case R.id.print3RB:
//                cmd = "$LCD+PRINT=3";
//                break;
//            case R.id.print4RB:
//                cmd = "$LCD+PRINT=4";
//                break;
//        }
        return view;
    }

    public String getCmd() {
        return cmd;
    }
}

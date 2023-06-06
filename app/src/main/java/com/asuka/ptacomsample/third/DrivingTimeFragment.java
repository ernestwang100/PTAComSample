package com.asuka.ptacomsample.third;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

import java.util.Calendar;
import java.util.Locale;

public class DrivingTimeFragment extends Fragment {
    private TextView drivertimeTV1, drivertimeTV2, drivertimeTV3, codrivertimeTV1, codrivertimeTV2, codrivertimeTV3;
    private String cmd;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driving_time, container, false);

        drivertimeTV1 = view.findViewById(R.id.drivertimeTV1);
        drivertimeTV2 = view.findViewById(R.id.drivertimeTV2);
        drivertimeTV3 = view.findViewById(R.id.drivertimeTV3);
        codrivertimeTV1 = view.findViewById(R.id.codrivertimeTV1);
        codrivertimeTV2 = view.findViewById(R.id.codrivertimeTV2);
        codrivertimeTV3 = view.findViewById(R.id.codrivertimeTV3);





        return view;
    }





    public String getCmd() {

        return cmd;
    }
}

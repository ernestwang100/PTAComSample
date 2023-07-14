package com.asuka.ptacomsample.third;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.ButtonFreezeListener;
import com.asuka.ptacomsample.main.RecvThread;

import java.util.Calendar;
import java.util.Locale;

public class DrivingTimeFragment extends Fragment {
    private TextView drivertimeTV1, drivertimeTV2, drivertimeTV3, codrivertimeTV1, codrivertimeTV2, codrivertimeTV3;
    private String cmd;
    private static final String TAG = "DrivingTimeFragment";


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

    public void updateValues(String[] temp) {
        if (temp != null && temp.length > 11) {
            drivertimeTV1.setText(temp[0] + ":" + temp[1]);
            drivertimeTV2.setText(temp[2] + ":" + temp[3]);
            drivertimeTV3.setText(temp[4] + ":" + temp[5]);
            codrivertimeTV1.setText(temp[6] + ":" + temp[7]);
            codrivertimeTV2.setText(temp[8] + ":" + temp[9]);
            codrivertimeTV3.setText(temp[10] + ":" + temp[11]);
        }
    }
}

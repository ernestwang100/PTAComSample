package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.RecvThread;

public class GainFragment extends Fragment {
    private NumberPicker numberPicker, numberPicker2;
    private String cmd, cmdStart, cmdEnd, temp[];
    private int gainType;
    private static final String TAG = "GainFragment";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gain, container, false);
        cmdStart = "$LCD+GAIN=";
        cmdEnd = "\r\n";

        numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(1000);
        numberPicker.setMinValue(0);
        int hour = numberPicker.getValue();

        numberPicker2 = view.findViewById(R.id.numberPicker2);
        numberPicker2.setMaxValue(1000);
        numberPicker2.setMinValue(0);

        return view;
    }

    public String getCmd() {
//        cmd = cmdStart + numberPicker.getValue();
        cmd = cmdStart + numberPicker.getValue() + "," + numberPicker2.getValue() + cmdEnd;
        return cmd;
    }

    public void updateValues(String[] temp) {
        if(temp.length > 0 && temp[0] != null && !temp[0].isEmpty() && TextUtils.isDigitsOnly(temp[0])) {
            numberPicker.setValue(Integer.parseInt(temp[0]));
            numberPicker2.setValue(Integer.parseInt(temp[1]));
        }
    }
}

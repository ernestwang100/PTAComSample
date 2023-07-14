package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.RecvThread;

public class PrintFragment extends Fragment {
    private RadioGroup radioGroupPrint;
    private Integer printDataID;
    private String cmd, cmdStart, temp[];
    public static final String TAG = "PrintFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_print, container, false);


        radioGroupPrint = view.findViewById(R.id.radioGroupPrint);
        radioGroupPrint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                printDataID = radioGroupPrint.indexOfChild(view.findViewById(i));
                Log.d(TAG, "onCheckedChanged: printDataID: " + printDataID);
            }
        });

        cmdStart = "$LCD+PRINT=";
        return view;
    }

    public String getCmd() {
        cmd = cmdStart + printDataID;
        return cmd;
    }

    public void updateValues(String[] temp) {
//        default set to check the first radio button
        radioGroupPrint.check(R.id.print1RB);
    }

}

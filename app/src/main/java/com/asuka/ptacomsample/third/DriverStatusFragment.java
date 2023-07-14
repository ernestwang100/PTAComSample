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
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.RecvThread;

public class DriverStatusFragment extends Fragment {
    private RadioGroup radioGroupDriverStatus, radioGroupCodriverStatus;
    private Integer driverStatus, codriverStatus;

    private String cmd, temp[], cmdStart;
    public static final int DRIVER_STATUS = 0;
    public static final int CODRIVER_STATUS = 1;
    private boolean isDefaultsSet = false;
    private static final String TAG = "DriverStatusFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_status, container, false);

        cmdStart = "$LCD+DRIVER STATUS=";

        radioGroupDriverStatus = view.findViewById(R.id.DRBtnGroup);
        radioGroupCodriverStatus = view.findViewById(R.id.CRBtnGroup);

        radioGroupDriverStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                driverStatus = radioGroupDriverStatus.indexOfChild(view.findViewById(i));
                Log.d(TAG, "onCheckedChanged: driverStatus: " + driverStatus);
            }
        });

        radioGroupCodriverStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                codriverStatus = radioGroupCodriverStatus.indexOfChild(view.findViewById(i));
                Log.d(TAG, "onCheckedChanged: codriverStatus: " + codriverStatus);
            }
        });

        return view;
    }

    private void setRadioButtons(String driverStatus, String codriverStatus) {
        if (driverStatus.equals("0")) {
            radioGroupDriverStatus.check(R.id.DRBtn0);
        } else if (driverStatus.equals("1")) {
            radioGroupDriverStatus.check(R.id.DRBtn1);
        } else if (driverStatus.equals("2")) {
            radioGroupDriverStatus.check(R.id.DRBtn2);
        } else if (driverStatus.equals("3")) {
            radioGroupDriverStatus.check(R.id.DRBtn3);
        }

        if (codriverStatus.equals("0")) {
            radioGroupCodriverStatus.check(R.id.CRBtn0);
        } else if (codriverStatus.equals("1")) {
            radioGroupCodriverStatus.check(R.id.CRBtn1);
        } else if (codriverStatus.equals("2")) {
            radioGroupCodriverStatus.check(R.id.CRBtn2);
        } else if (codriverStatus.equals("3")) {
            radioGroupCodriverStatus.check(R.id.CRBtn3);
        }
    }

    public String getCmd() {
        if (driverStatus != null && codriverStatus != null) {
            cmd = cmdStart + driverStatus + "," + codriverStatus;
        }
        return cmd;
    }

    public void updateValues(String[] temp) {
        this.temp = temp;
        setRadioButtons(temp[DRIVER_STATUS], temp[CODRIVER_STATUS]);
    }
}

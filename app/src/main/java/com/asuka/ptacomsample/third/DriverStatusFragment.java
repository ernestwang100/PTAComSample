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
import com.asuka.ptacomsample.main.DataUpdateListener;
import com.asuka.ptacomsample.main.MainActivity;
import com.asuka.ptacomsample.main.MainFragmentTV2;
import com.asuka.ptacomsample.main.RecvThread;

public class DriverStatusFragment extends Fragment implements DataUpdateListener {
    private RadioGroup radioGroupDriverStatus, radioGroupCodriverStatus;
    private Integer driverStatus, codriverStatus;
    private byte[] writeData;
    private String cmd, temp[], cmdStart;
    MainActivity mainActivity = MainActivity.getInstance();
    private RecvThread mRecvThread = mainActivity.getRecvThread();
    public static final int DRIVER_STATUS = 0;
    public static final int CODRIVER_STATUS = 1;
    private boolean isDefaultsSet;
    private static final String TAG = "DriverStatusFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_status, container, false);
        registerListener();
        isDefaultsSet = false;
        cmdStart = "$LCD+DRIVER STATUS=";

//        handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message msg) {
//                Log.d(TAG, "handleMessage: " + msg.obj.toString());
//                temp = msg.obj.toString().split(",");
//                for (int i = 0; i < temp.length; i++) {
//                    temp[i] = temp[i].trim();
//                }
//
//                if (!isDefaultsSet && temp != null && temp.length > Math.max(DRIVER_STATUS, CODRIVER_STATUS) &&TextUtils.isDigitsOnly(temp[DRIVER_STATUS]) && TextUtils.isDigitsOnly(temp[CODRIVER_STATUS])) {
//
//                    Log.d(TAG, "onCreateView: temp: " + temp[DRIVER_STATUS] + ", " + temp[CODRIVER_STATUS]);
//
//                    setRadioButtons(temp[DRIVER_STATUS], temp[CODRIVER_STATUS]);
//                    isDefaultsSet = true;
//                }
//
//
//            }
//        };

        writeData = (cmdStart + "?").getBytes();
        Log.d(TAG, "onViewCreated: mainActivity = " + mainActivity);
        Log.d(TAG, "onViewCreated:  mRecvThread = " + mRecvThread);
        Log.d(TAG, "onCreateView: writeData = " + new String(writeData));
        mRecvThread.setWriteData(writeData);
//        mRecvThread = new RecvThread(handler, mPort, writeData);
//        mRecvThread.start();

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

    @Override
    public void onPause() {
        super.onPause();
//        mRecvThread.interrupt();
        unregisterListener();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        registerListener();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    public String getCmd() {
        if (driverStatus != null && codriverStatus != null) {
            cmd = cmdStart + driverStatus + "," + codriverStatus;
        }
        return cmd;
    }

    @Override
    public void onDataUpdated(String newData) {
        // Handle the updated data
        Log.d(TAG, "Received updated data: " + newData);
        temp = newData.split(",");
        for (int i = 0; i < temp.length; i++) {
            temp[i] = temp[i].trim();
        }

        if (!isDefaultsSet && temp != null && temp.length > Math.max(DRIVER_STATUS, CODRIVER_STATUS) && TextUtils.isDigitsOnly(temp[DRIVER_STATUS]) && TextUtils.isDigitsOnly(temp[CODRIVER_STATUS])) {

            Log.d(TAG, "onCreateView: temp: " + temp[DRIVER_STATUS] + ", " + temp[CODRIVER_STATUS]);

            setRadioButtons(temp[DRIVER_STATUS], temp[CODRIVER_STATUS]);
            isDefaultsSet = true;
        }

    }

    // Register the listener in the appropriate place, e.g., in onCreate() or onResume()
    public void registerListener() {
        MainActivity.registerDataUpdateListener(this);
    }

    // Unregister the listener in the appropriate place, e.g., in onDestroy() or onPause()
    public void unregisterListener() {
        MainActivity.unregisterDataUpdateListener(this);
    }
}

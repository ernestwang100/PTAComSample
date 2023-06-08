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
    private byte[] writeData;
    private ComPort mPort;
    private String cmd, temp[], cmdStart;
    private RecvThread mRecvThread;
    private Handler handler;
    public static final int DRIVER_STATUS = 1;
    public static final int CODRIVER_STATUS = 3;
    private boolean isDefaultsSet = false;
    private static final String TAG = "DriverStatusFragment";

    public DriverStatusFragment() {
        super();
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_status, container, false);

        cmdStart = "$LCD+DRIVERSTATUS=";
        writeData = "$LCD+PAGE=1".getBytes();
        mPort.write(writeData, writeData.length);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "handleMessage: " + msg.obj.toString());
                temp = msg.obj.toString().split(",");
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = temp[i].trim();
                }

                if (!isDefaultsSet && temp != null && temp.length > 3 &&TextUtils.isDigitsOnly(temp[DRIVER_STATUS]) && TextUtils.isDigitsOnly(temp[CODRIVER_STATUS])) {

                    Log.d(TAG, "onCreateView: temp: " + temp[DRIVER_STATUS] + ", " + temp[CODRIVER_STATUS]);

                    setRadioButtons(temp[DRIVER_STATUS], temp[CODRIVER_STATUS]);
                    isDefaultsSet = true;
                }


            }
        };
        mRecvThread = new RecvThread(handler, mPort, writeData, 1);
        mRecvThread.start();

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
    public void onDestroyView() {
        super.onDestroyView();
        mRecvThread.interrupt();
    }

    public String getCmd() {
        if (driverStatus != null && codriverStatus != null) {
            cmd = cmdStart + driverStatus + "," + codriverStatus;
        }
        return cmd;
    }
}

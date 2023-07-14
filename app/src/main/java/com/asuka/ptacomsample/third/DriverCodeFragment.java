package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.RecvThread;

public class DriverCodeFragment extends Fragment {
    private EditText driverCodeEdt, codriverCodeEdt;
    private ComPort mPort;
    private String cmd, temp[], cmdStart;

    public static final int DRIVER_STATUS = 0;
    public static final int CODRIVER_STATUS = 1;
    private boolean isDefaultsSet = false;

    private static final String TAG = "DriverCodeFragment";

    public DriverCodeFragment() {
        super();
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_code, container, false);
        if (temp != null && temp.length > 1) {
            driverCodeEdt.setText(temp[DRIVER_STATUS]);
            codriverCodeEdt.setText(temp[CODRIVER_STATUS]);
        }


        cmdStart = "$LCD+DRIVER IN=";


        driverCodeEdt = view.findViewById(R.id.driverCodeEdt);
        codriverCodeEdt = view.findViewById(R.id.codriverCodeEdt);

        return view;
    }


    public String getCmd() {
        cmd = cmdStart + driverCodeEdt.getText().toString() + "," + codriverCodeEdt.getText().toString();
        return cmd;

    }

    public void updateValues(String[] temp) {
        this.temp = temp;
        if (temp != null && temp.length > 1) {
            driverCodeEdt.setText(temp[DRIVER_STATUS]);
            codriverCodeEdt.setText(temp[CODRIVER_STATUS]);
        }
    }
}

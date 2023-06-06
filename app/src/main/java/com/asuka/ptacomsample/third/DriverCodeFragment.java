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
    private byte[] writeData;
    private ComPort mPort;
    private String cmd, temp[];
    private RecvThread mRecvThread;
    private Handler handler;
    public static final int DRIVER_STATUS = 0;
    public static final int CODRIVER_STATUS = 2;
    private static final String TAG = "DriverCodeFragment";

    public DriverCodeFragment() {
        super();
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_code, container, false);

        writeData = "$LCD+PAGE=1".getBytes();
        mPort.write(writeData, writeData.length);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message msg) {
                temp = msg.obj.toString().replace("#", "").split(",");
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = temp[i].trim();
                }

                if (temp != null && temp.length > 1) {
                    driverCodeEdt.setText(temp[DRIVER_STATUS]);
                    codriverCodeEdt.setText(temp[CODRIVER_STATUS]);
                }
            }
        };

        mRecvThread = new RecvThread(handler, mPort, writeData, 1);
        mRecvThread.start();




        driverCodeEdt = view.findViewById(R.id.driverCodeEdt);
        codriverCodeEdt = view.findViewById(R.id.codriverCodeEdt);

        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
        mRecvThread.interrupt();
    }

    public String getCmd() {
        String cmd = "$LCD+DRIVER IN=0," + driverCodeEdt.getText().toString() + "\n" +
                "$LCD+DRIVER IN=1," + codriverCodeEdt.getText().toString();
        return cmd;

    }
}

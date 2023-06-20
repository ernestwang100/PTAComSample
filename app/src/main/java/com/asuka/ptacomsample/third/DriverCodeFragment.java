package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.DataUpdateListener;
import com.asuka.ptacomsample.main.MainActivity;
import com.asuka.ptacomsample.main.RecvThread;

public class DriverCodeFragment extends Fragment implements DataUpdateListener {
    private EditText driverCodeEdt, codriverCodeEdt;
    private byte[] writeData;
    private ComPort mPort;
    private String cmd, temp[], cmdStart;
    MainActivity mainActivity = MainActivity.getInstance();
    private RecvThread mRecvThread = mainActivity.getRecvThread();
    private Handler handler;
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

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message msg) {
                temp = msg.obj.toString().replace("#", "").split(",");
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = temp[i].trim();
                }

                if (!isDefaultsSet && temp != null && temp.length > 1) {
                    driverCodeEdt.setText(temp[DRIVER_STATUS]);
                    codriverCodeEdt.setText(temp[CODRIVER_STATUS]);
                    isDefaultsSet = true;
                }
            }
        };

        cmdStart = "$LCD+DRIVER IN=";
        writeData = (cmdStart+"?").getBytes();
        Log.d(TAG, "onViewCreated: mainActivity = " + mainActivity);
        Log.d(TAG, "onViewCreated:  mRecvThread = " + mRecvThread);
        Log.d(TAG, "onCreateView: writeData = " + new String(writeData));
        mRecvThread.setWriteData(writeData);
//        mRecvThread = new RecvThread(handler, mPort, writeData);
//        mRecvThread.start();

        driverCodeEdt = view.findViewById(R.id.driverCodeEdt);
        codriverCodeEdt = view.findViewById(R.id.codriverCodeEdt);

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
//        mRecvThread.interrupt();
        unregisterListener();
        Log.d(TAG, "onPause: ");
    }


    public String getCmd() {
        cmd = cmdStart + driverCodeEdt.getText().toString() + "," + codriverCodeEdt.getText().toString();
        return cmd;
    }

    public void onDataUpdated(String newData) {
        // Handle the updated data
        Log.d(TAG, "Received updated data: " + newData);
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

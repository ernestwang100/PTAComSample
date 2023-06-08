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
import com.asuka.ptacomsample.main.RecvThread;

import java.util.Calendar;
import java.util.Locale;

public class DrivingTimeFragment extends Fragment {
    private TextView drivertimeTV1, drivertimeTV2, drivertimeTV3, codrivertimeTV1, codrivertimeTV2, codrivertimeTV3;
    private String cmd, temp[];
    private byte[] writeData;
    private ComPort mPort;
    private RecvThread mRecvThread;
    private Handler handler;
    private boolean isDefaultsSet = false;
    private static final String TAG = "DrivingTimeFragment";


    public DrivingTimeFragment() {
        super();
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driving_time, container, false);

        writeData = "$LCD+DRIVER TIME=?".getBytes();
        mPort.write(writeData, writeData.length);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message msg) {
                Log.d(TAG, "handleMessage: " + msg.obj.toString());
                temp = msg.obj.toString().split(",");
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = temp[i].trim();
                }

                if ( !isDefaultsSet && temp != null && temp.length > 1) {
                    drivertimeTV1.setText(temp[0]);
                    drivertimeTV2.setText(temp[1]);
                    isDefaultsSet = true;
                }
            }
        };

        mRecvThread = new RecvThread(handler, mPort, writeData, 1);
        mRecvThread.start();

        drivertimeTV1 = view.findViewById(R.id.drivertimeTV1);
        drivertimeTV2 = view.findViewById(R.id.drivertimeTV2);
        drivertimeTV3 = view.findViewById(R.id.drivertimeTV3);
        codrivertimeTV1 = view.findViewById(R.id.codrivertimeTV1);
        codrivertimeTV2 = view.findViewById(R.id.codrivertimeTV2);
        codrivertimeTV3 = view.findViewById(R.id.codrivertimeTV3);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecvThread.interrupt();
        mPort.close();
    }


    public String getCmd() {

        return cmd;
    }
}

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
    private NumberPicker numberPicker;
    private String cmd, cmdStart, temp[];
    private int gainType;
    private byte[] writeData;
    private ComPort mPort;
    private RecvThread mRecvThread;
    private Handler handler;
    private static final String TAG = "GainFragment";

    public GainFragment(int gainType) {
        this.gainType = gainType;

        if (gainType == 0) {
            cmdStart = "$LCD+SPEED GAIN=";
        } else {
            cmdStart = "$LCD+RPM DIV=";
        }
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gain, container, false);

        numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(1000);
        numberPicker.setMinValue(1);
        int hour = numberPicker.getValue();

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "handleMessage: " + msg.obj.toString());
                temp = msg.obj.toString().split(",");
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = temp[i].trim();
                    Log.d(TAG, "handleMessage: temp[" + i + "]: " + temp[i]);
                }
                if(temp.length > 0 && temp[0] != null && !temp[0].isEmpty() && TextUtils.isDigitsOnly(temp[0])) {
                    numberPicker.setValue(Integer.parseInt(temp[0]));
                }

            }
        };
        writeData = (cmdStart+"?").getBytes();
        mRecvThread = new RecvThread(handler, mPort, writeData, getContext());
        mRecvThread.start();


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecvThread.interrupt();
    }

    public String getCmd() {
        cmd = cmdStart + numberPicker.getValue();
        return cmd;
    }
}

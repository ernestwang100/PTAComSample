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
    private byte[] writeData;
    private ComPort mPort;
    private RecvThread mRecvThread;
    private Handler handler;
    public static final String TAG = "PrintFragment";

    public PrintFragment() {
        super();
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }

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


        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "handleMessage: " + msg.obj.toString());
                temp = msg.obj.toString().split(",");
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = temp[i].trim();
                }

                switch (temp[0]) {
                    case "0":
                        Log.d(TAG, "handleMessage: processing complete");
                        break;
                    case "1":
                        Log.d(TAG, "handleMessage: processing");
                        break;
                    case "2":
                        Log.d(TAG, "handleMessage: processing error");
                        break;
                }
            }
        };

        cmdStart = "$LCD+PRINT=";
        cmd = cmdStart + printDataID;
        writeData = cmd.getBytes();
        mRecvThread = new RecvThread(handler, mPort, writeData, getContext());
        mRecvThread.start();

        return view;
    }

    public String getCmd() {
        cmd = cmdStart + printDataID;
        return cmd;
    }
    @Override
    public void onPause() {
        super.onPause();
        mRecvThread.interrupt();
    }

}

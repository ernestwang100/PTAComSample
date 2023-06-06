package com.asuka.ptacomsample.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;

public class MainFragmentTV1 extends Fragment {
    private TextView tv1;
    private Handler handler;
    private ComPort mPort;
    RecvThread mRecvThread;
    static final String TAG = "MainFragmentTV1";

    public MainFragmentTV1() {
        super();
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_tv1, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv1 = (TextView) view.findViewById(R.id.mainTV_1);
        byte[] writeData = "$LCD+PAGE=0".getBytes();
        mPort.write(writeData, writeData.length);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                tv1.setText(msg.obj.toString());
                Log.d(TAG, "handleMessage: " + msg.obj.toString());
            }
        };
        mRecvThread = new RecvThread(handler, mPort,writeData);;
        mRecvThread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        tv1.setText("資料讀取中...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Received = run TV1 onPause()");
        Log.d(TAG, "Received = current thread: " + Thread.currentThread().toString());
        mRecvThread.interrupt();
        Log.d(TAG, "Received = current thread: " + Thread.currentThread());
        Log.d(TAG, "Received = thread 數量: " + Thread.getAllStackTraces().size());
    }
}

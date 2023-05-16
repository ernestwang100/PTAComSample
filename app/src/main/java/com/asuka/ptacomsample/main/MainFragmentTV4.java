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

import java.util.concurrent.ExecutorService;

public class MainFragmentTV4 extends Fragment {
    private TextView tv4;
    private Handler handler;
    private String messageText = "";
    private ComPort mPort;
    private RecvThread mRecvThread;
    private static final String TAG = "MainFragmentTV4";
    private ExecutorService executorService;

    public MainFragmentTV4(ExecutorService executorService) {
        super();
        this.executorService = executorService;
        this.mPort = new ComPort();
        this.mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_tv4, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv4 = (TextView) view.findViewById(R.id.mainTV_4);
        byte[] writeData = "$LCD+PAGE=3".getBytes();
        mPort.write(writeData, writeData.length);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                tv4.setText(msg.obj.toString());
            }
        };
        mRecvThread = new RecvThread(handler, mPort);
        mRecvThread.start();
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "Received = run TV4 onPause()");
        Log.d(TAG, "Received = current thread: " + Thread.currentThread().toString());
        mRecvThread.interrupt();
        Log.d(TAG, "Received = current thread: " + Thread.currentThread());
        Log.d(TAG, "Received = thread 數量: " + Thread.getAllStackTraces().size());
    }


}

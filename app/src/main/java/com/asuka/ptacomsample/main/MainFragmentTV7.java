package com.asuka.ptacomsample.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;

public class MainFragmentTV7 extends Fragment {
    private TextView tv7;
    private Handler handler;
    private String messageText = "";
    private ComPort mPort;
    private RecvThread mRecvThread;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_tv7, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv7 = (TextView) view.findViewById(R.id.mainTV_7);
//        byte[] writeData = "$LCD+PAGE=6".getBytes();
//        mPort.write(writeData, writeData.length);
//        handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message msg) {
//                tv7.setText(msg.obj.toString());
//            }
//        };
//        mRecvThread = new RecvThread(handler, mPort);
//        mRecvThread.start();
    }
}

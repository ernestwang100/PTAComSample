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

import java.lang.ref.WeakReference;

public class MainFragmentTV1 extends Fragment implements Handler.Callback {
    private TextView tv1;
    private WeakReference<Handler> handlerRef;
    private ComPort mPort;
    private RecvThread mRecvThread;
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
        tv1 = view.findViewById(R.id.mainTV_1);
        byte[] writeData = "$LCD+PAGE=0".getBytes();
        handlerRef = new WeakReference<>(new Handler(Looper.getMainLooper(), this));
        mRecvThread = new RecvThread(handlerRef.get(), mPort, writeData);
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
        if (mRecvThread != null) {
            mRecvThread.interrupt();
            mRecvThread = null;
        }
        if (handlerRef != null) {
            handlerRef.clear();
            handlerRef = null;
        }
        mPort.close();
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (tv1 != null) {
            tv1.setText(msg.obj.toString());
            Log.d(TAG, "handleMessage: " + msg.obj.toString());
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handlerRef != null) {
            handlerRef.clear();
            handlerRef = null;
        }
        if (mRecvThread != null) {
            mRecvThread.interrupt();
            mRecvThread = null;
        }

        mPort.close();

    }
}

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

public class MainFragmentTV1 extends Fragment implements DataUpdateListener {
    private TextView tv1;
    MainActivity mainActivity = MainActivity.getInstance();
    private RecvThread mRecvThread = mainActivity.getRecvThread();
    private byte[] writeData;
    static final String TAG = "MainFragmentTV1";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_tv1, container, false);
        writeData = "$LCD+PAGE=0".getBytes();

        return view;
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: mainActivity = " + mainActivity);
        Log.d(TAG, "onViewCreated:  mRecvThread = " + mRecvThread);
        Log.d(TAG, "onViewCreated: writeData = " + new String(writeData));
        mRecvThread.setWriteData(writeData);
        tv1 = view.findViewById(R.id.mainTV_1);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerListener();
        tv1.setText("資料讀取中...");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterListener();
//        mRecvThread.interrupt();
    }


    @Override
    public void onDataUpdated(String newData) {
        Log.d(TAG, "Received updated data: " + newData);
        tv1.setText(newData);
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

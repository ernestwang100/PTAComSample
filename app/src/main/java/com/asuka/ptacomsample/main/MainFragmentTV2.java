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

public class MainFragmentTV2 extends Fragment {
    private TextView tv2;
    private Handler handler;
    private String messageText = "";
    private ExecutorService executorService;
    ComPort mPort;
    RecvThread mRecvThread;
    static final String LOG_TAG = "ComSampleApp";

    public MainFragmentTV2(ExecutorService executorService) {
        super();
        this.executorService = executorService;
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_tv2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv2 = (TextView) view.findViewById(R.id.mainTV_2);
        byte[] writeData = "$LCD+PAGE=1".getBytes();
        mPort.write(writeData, writeData.length);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                tv2.setText(msg.obj.toString());
            }
        };
        mRecvThread = new RecvThread();
        mRecvThread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        tv2.setText("資料讀取中...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Received = run TV2 onPause()");
        Log.d(LOG_TAG, "Received = current thread: " + Thread.currentThread().toString());
        mRecvThread.interrupt();
        Log.d(LOG_TAG, "Received = current thread: " + Thread.currentThread());
        Log.d(LOG_TAG, "Received = thread 數量: " + Thread.getAllStackTraces().size());
    }

    class RecvThread extends Thread {
        @Override
        public void run() {
            byte[] readBuf = new byte[200];
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Log.i(LOG_TAG, "Exception: " + e.toString() + " occurs");
                    if (e instanceof InterruptedException)
                        break;
                }

                int count = mPort.read(readBuf, readBuf.length);
                if (count > 0) {
                    String received = "";
                    for (int i = 0; i < count; i++) {
                        received += String.format("%c", readBuf[i]);
                    }
                    Log.d(LOG_TAG, "Received = " + received);
                    if (received.contains("$VDR+SHOW DATA=1")) {
                        String[] temp = received.split(",");
                        messageText = "主駕駛\n" + temp[1];
                        switch (temp[2].trim()) {
                            case "0":
                                messageText += " 車停\n";
                                break;
                            case "1":
                                messageText += " 行駛\n";
                                break;
                            case "2":
                                messageText += " 待班\n";
                                break;
                            case "3":
                                messageText += " 休息\n";
                                break;
                        }
                        messageText += "共同駕駛\n" + temp[3];
                        switch (temp[4].trim()) {
                            case "0#":
                                messageText += " 車停\n";
                                break;
                            case "1#":
                                messageText += " 行駛\n";
                                break;
                            case "2#":
                                messageText += " 待班\n";
                                break;
                            case "3#":
                                messageText += " 休息\n";
                                break;
                        }
                        Message msg2 = Message.obtain();
                        msg2.obj = messageText;
                        //msg2.obj = received;
                        handler.sendMessage(msg2);
                    }
                }
            }
            Log.d(LOG_TAG, "Received = RecvThread ended~~~");
        }
    }
}

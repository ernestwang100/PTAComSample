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

import java.awt.font.TextAttribute;
import java.util.concurrent.ExecutorService;

public class MainFragmentTV1 extends Fragment {
    private TextView tv1;
    private Handler handler;
    private String messageText = "";
    ComPort mPort;
    RecvThread mRecvThread;
    static final String LOG_TAG = "ComSampleApp";

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
            }
        };
        mRecvThread = new RecvThread(handler, mPort,writeData);;
        mRecvThread.start();
/*
        executorService.execute(() -> {
            byte[] readBuf = new byte[200];
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Log.i(LOG_TAG, "Exception: " + e.toString() + " occurs");
                }

                int count = mPort.read(readBuf, readBuf.length);
                if (count > 0) {
                    String received = "";
                    for (int i = 0; i < count; i++) {
                        received += String.format("%c", readBuf[i]);
                    }

                    Log.d(LOG_TAG, "Received = " + received);
                    if (received.contains("$VDR+SHOW DATA=0")) {
                        String[] temp = received.split(",");
                        messageText = temp[1] + "\n" + temp[2] + "\n" +
                                "速度 " + temp[3] + " km/h\n" +
                                "駕駛 " + temp[4];
                        switch (temp[5].trim()) {
                            case "0#":
                                messageText += " 車停";
                                break;
                            case "1#":
                                messageText += " 行駛";
                                break;
                            case "2#":
                                messageText += " 待班";
                                break;
                            case "3#":
                                messageText += " 休息";
                                break;
                        }
                    Message msg1 = Message.obtain();
                    msg1.obj = messageText;
                    msg1.obj = received;
                    handler.sendMessage(msg1);
                    }
                }
            }
        });
 */
    }

    @Override
    public void onResume() {
        super.onResume();
        tv1.setText("資料讀取中...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Received = run TV1 onPause()");
        Log.d(LOG_TAG, "Received = current thread: " + Thread.currentThread().toString());
        mRecvThread.interrupt();
        Log.d(LOG_TAG, "Received = current thread: " + Thread.currentThread());
        Log.d(LOG_TAG, "Received = thread 數量: " + Thread.getAllStackTraces().size());
    }

    /*
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
//                        TODO: 這裡有問題，如果收到的資料不是完整的一筆，會有問題
                        received += String.format("%c", readBuf[i]);
                    }

                    Log.d(LOG_TAG, "Received = " + received);
                    if (received.contains("$VDR+SHOW DATA=0")) {
                        String[] temp = received.split(",");
                        messageText = temp[1] + "\n" + temp[2] + "\n" +
                                "速度 " + temp[3] + " km/h\n" +
                                "駕駛 " + temp[4];
                        switch (temp[5].trim()) {
                            case "0#":
                                messageText += " 車停";
                                break;
                            case "1#":
                                messageText += " 行駛";
                                break;
                            case "2#":
                                messageText += " 待班";
                                break;
                            case "3#":
                                messageText += " 休息";
                                break;
                        }
                        Message msg1 = Message.obtain();
                        msg1.obj = messageText;
//                        msg1.obj = received;
                        handler.sendMessage(msg1);
                    }
                }
            }
            Log.d(LOG_TAG, "Received = RecvThread ended~~~");
        }
    }
     */

}

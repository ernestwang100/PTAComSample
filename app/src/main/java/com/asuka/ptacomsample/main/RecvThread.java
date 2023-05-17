package com.asuka.ptacomsample.main;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.asuka.comm.ComPort;

public class RecvThread extends Thread {
    private String messageText = "NULL";
    private int round = 0;
    private boolean isRunning = true;
    private byte[] readBuf, writeData;
    private ComPort mPort;
    private Handler handler;
    private static final String TAG = "RecvThread";

    public RecvThread(Handler handler, ComPort mPort, byte[] writeData) {
        this.mPort = mPort;
        this.handler = handler;
        this.writeData = writeData;



    }


    public void run() {

        Message msg = Message.obtain();
        msg.obj = "資料讀取中...";
        //msg.obj = received;
        handler.sendMessage(msg);

        readBuf = new byte[200];
        while (!Thread.currentThread().isInterrupted()) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                Log.i(TAG, "Exception: " + e.toString() + " occurs");
                if (e instanceof InterruptedException)
                    break;
            }

            int count = readBuf == null ? 0 : mPort.read(readBuf, readBuf.length);
            if (count > 0) {
                String received = "";
                for (int i = 0; i < count; i++) {
                    received += String.format("%c", readBuf[i]);
                }
                Log.i(TAG, "received: " + received);

                String[] temp = received.split(",");
                Log.i(TAG, "temp[0]: " + temp[0]);

                if(new String(writeData).split("=")[1].equals(temp[0].split("=")[1].trim())) {
                    switch (temp[0]) {
                        case "$VDR+SHOW DATA=0":

                        case "$VDR+SHOW DATA=1":
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
                            break;

                        case "$VDR+SHOW DATA=2":
                            messageText = "主駕駛\n" + temp[1] + "\n";
                            messageText += "共同駕駛\n" + temp[2];
                            break;


                        case "$VDR+SHOW DATA=3":
                            messageText = "BUS-ID: " + temp[1] + "\n";
                            messageText += "HW-ID: " + temp[2] + "\n";
                            messageText += "FW-ID: " + temp[3] + "\n";
                            messageText += "日期: " + temp[4] + "\n";
                            break;

                        case "$VDR+SHOW DATA=4":
                            messageText = "GPS狀態: " + temp[1] + "\n";
                            messageText += "衛星數: " + temp[2] + "\n";
                            messageText += "日期: " + temp[3] + "\n";
                            messageText += "時間: " + temp[4] + "\n";
                            break;
                        case "$VDR+SHOW DATA=5":
                        case "$VDR+SHOW DATA=6":
                        case "$VDR+SHOW DATA=7":


                    }

//                Log.d(TAG, "messageText: " + messageText);
                    msg = Message.obtain();
                    msg.obj = messageText;
                    //msg.obj = received;
                    handler.sendMessage(msg);
                }


                }
            }
        Log.i(TAG, "Received = RecvThread ended~~~");
        }
    }




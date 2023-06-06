package com.asuka.ptacomsample.main;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.asuka.comm.ComPort;

public class RecvThread extends Thread {
    private String messageText = "NULL", tempMsg = "NULL";
    private byte[] readBuf, writeData;
    private ComPort mPort;
    private Handler handler;
    private String[] temp;
    private int recvType;
    private static final String TAG = "RecvThread";


    public RecvThread(Handler handler, ComPort mPort, byte[] writeData) {
        this.mPort = mPort;
        this.handler = handler;
        this.writeData = writeData;
    }

    public RecvThread(Handler handler, ComPort mPort, byte[] writeData, int recvType) {
        this.mPort = mPort;
        this.handler = handler;
        this.writeData = writeData;
        this.recvType = recvType;
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

            mPort.write(writeData, writeData.length);

//            int count = readBuf == null ? 0 : mPort.read(readBuf, readBuf.length);
            int count = mPort.read(readBuf, readBuf.length);
            if (count > 0) {
//                Log.d(TAG, "run: readbuf: " + new String(readBuf));
                String received = "";
                for (int i = 0; i < count; i++) {
                    received += String.format("%c", readBuf[i]);
                }
                Log.i(TAG, "received: " + received);

                temp = received.split(",");
                Log.i(TAG, "temp[0]: " + temp[0]);

                // if received data is the same as the data sent
                if (temp[0].contains("$VDR+SHOW DATA") && temp.length > 2 && new String(writeData).split("=")[1].equals(temp[0].split("=")[1].trim())) {
                    switch (temp[0]) {
                        case "$VDR+SHOW DATA=0":
                            if (temp.length == 6) {
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
                            } else {
                                messageText = "資料讀取中...";
                            }
                            break;

                        case "$VDR+SHOW DATA=1":
                            tempMsg = temp[1] + ',' + temp[2] + ',' + temp[3] + ',' + temp[4];
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
                            tempMsg = temp[1] + ',' + temp[2];
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
                            tempMsg = temp[3] + ',' + temp[4];
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

                    if (recvType == 1) {
                        msg.obj = tempMsg;
                    }
                    handler.sendMessage(msg);
                }


            }
        }
        Log.i(TAG, "Received = RecvThread ended~~~");
    }

    public String[] getTemp() {
        return temp;
    }
}




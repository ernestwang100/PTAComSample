package com.asuka.ptacomsample.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;

public class RecvThread extends Thread {
    private String messageText = "NULL";
    private byte[] readBuf, writeData;
    private ComPort mPort;
    private Handler handler;
    private String temp[], warningMsg;
    private boolean isRunning = true;
    private Context context;
    private Message msg;
    private static final String TAG = "RecvThread";

    public RecvThread(Handler handler, ComPort mPort, byte[] writeData, Context context) {
        this.mPort = mPort;
        this.handler = handler;
        this.writeData = writeData;
        this.context = context;

    }


    public void run() {

//        msg = Message.obtain();
//        msg.obj = "資料讀取中...";
//        //msg.obj = received;
//        handler.sendMessage(msg);

        readBuf = new byte[200];
        Log.d(TAG, "run: writeData: " + new String(writeData));
        mPort.write(writeData, writeData.length);


        while (!Thread.currentThread().isInterrupted() && isRunning) {
            try {
                sleep(30);
            } catch (InterruptedException e) {
                Log.i(TAG, "Exception: " + e.toString() + " occurs");
//                try {
//                    writeData = "$LCD+PAGE=99".getBytes();
//                    mPort.write(writeData, writeData.length);
//                    Log.d(TAG, "run: writeData: " + new String(writeData));
//                    sleep(5000);
//                } catch (Exception ex) {
//                    Log.i(TAG, "Exception: " + ex.toString() + " occurs");
//                }

                break;

            }

            int count = mPort.read(readBuf, readBuf.length);
//            Log.d(TAG, "run: count: " + count);
            if (count > 0) {
//                Log.d(TAG, "run: readbuf: " + new String(readBuf));
                String received = "";
                for (int i = 0; i < count; i++) {
                    try {
                        received += String.format("%c", readBuf[i]);
                    } catch (Exception e) {
                        Log.i(TAG, "Exception: " + e.toString() + " occurs");
                        if (e instanceof InterruptedException) break;
                    }
                }
                Log.i(TAG, "received: " + received);

//                if (!received.contains("$VDR+SHOW DATA")) {
//                    isRunning = false;
//                }

//                default messageText
                if(received.contains("=")){
                    messageText = received.replace("#", "").split("=")[1].trim();
                    temp = received.replace("#", ",").replace("=", ",").split(",");
                }




//                Log.d(TAG, "run: !temp[0].contains(\"$\"): " + !temp[0].contains("$"));
                Log.d(TAG, "run: writeData: " + new String(writeData));
                // if received data is the same as the data sent
                if (!temp[0].startsWith("$")) {
                    messageText = "資料讀取中...";
                    Log.d(TAG, "run: start without $");
                } else if (temp[0].contains("$VDR+WARNING")) {
                    switch (temp[1].trim()) {
                        case "0":
                            warningMsg = "Alert 0";
                            messageText = "Alert 0";
                            break;
                        case "1":
                            warningMsg = "Alert 1";
                            messageText = "Alert 1";
                            break;
                        case "2":
                            warningMsg = "Alert 2";
                            messageText = "Alert 2";
                            break;
                    }

                    openWarningDialog(messageText);
                } else if (temp[0].contains("$VDR+SHOW DATA")) {
//                    Log.d(TAG, "run: temp[1]=" + temp[1]);

                    boolean istheSame = new String(writeData).split("=")[1].equals(temp[1].trim());
                    if (istheSame) {
                        Log.d(TAG, "run: enable button");
                        switch (temp[1]) {
                            case "0":
//                            Log.d(TAG, "run: temp.length: " + temp.length);
                                if (temp.length == 8) {
                                messageText = temp[2] + "\n" + temp[3] + "\n" + "速度 " + temp[4] + " km/h\n" + "駕駛 " + temp[5];
                                switch (temp[6].trim()) {
                                    case "0":
                                        messageText += " 車停";
                                        break;
                                    case "1":
                                        messageText += " 行駛";
                                        break;
                                    case "2":
                                        messageText += " 待班";
                                        break;
                                    case "3":
                                        messageText += " 休息";
                                        break;
                                }
                                } else {
                                    messageText = "資料讀取中...";
                                }
                                break;

                            case "1":
                                messageText = "主駕駛\n" + temp[2];
                                switch (temp[3].trim()) {
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
                                messageText += "共同駕駛\n" + temp[4];
                                switch (temp[5].trim()) {
                                    case "0":
                                        messageText += " 車停";
                                        break;
                                    case "1":
                                        messageText += " 行駛";
                                        break;
                                    case "2":
                                        messageText += " 待班";
                                        break;
                                    case "3":
                                        messageText += " 休息";
                                        break;
                                }
                                break;

                            case "2":
                                messageText = "";
                                Log.d(TAG, "run: temp[2], temp[3], temp[4]: " + temp[2] + ", " + temp[3] + ", " + temp[4]);

                                switch (temp[2].trim()) {
                                    case "0":
                                        messageText += "駕駛正常\n";
                                        break;
                                    case "1":
                                        messageText += "即將超時駕駛\n";
                                        break;
                                    case "2":
                                        messageText += "駕駛超時開車\n";
                                        break;
                                }

                                switch (temp[3].trim()) {
                                    case "0":
                                        messageText += "印表機正常\n";
                                        break;
                                    case "1":
                                        messageText += "印表機缺紙\n";
                                        break;
                                }

                                switch (temp[4].trim()) {
                                    case "0":
                                        messageText += "供電正常";
                                        break;
                                    case "1":
                                        messageText += temp[5] + " 斷電發生";
                                        break;
                                }


                                break;


                            case "3":
                                messageText = "BUS-ID: " + temp[2] + "\n";
                                messageText += "HW-ID: " + temp[3] + "\n";
                                messageText += "FW-ID: " + temp[4] + "\n";
                                messageText += "日期: " + temp[5];
                                break;

                            case "4":
                                messageText = "GPS狀態: " + temp[2] + "\n";
                                messageText += "衛星數: " + temp[3] + "\n";
                                messageText += "日期: " + temp[4] + "\n";
                                messageText += "時間: " + temp[5];
                                break;

                            case "5":
                                messageText = "里程" + temp[2] + " km\n";
                                messageText += "VIN" + temp[3];
                                break;

                        }
                    } else {
                        Log.d(TAG, "run: disable button");
                        messageText = "資料讀取中...";
                    }

                }

                Log.d(TAG, "messageText: " + messageText);

                msg = Message.obtain();
                msg.obj = messageText;
                //msg.obj = received;

                handler.sendMessage(msg);
            }
        }
        Log.i(TAG, "Received = RecvThread ended~~~");


    }


    private void openWarningDialog(final String warningMsg) {
        Log.d(TAG, "openWarningDialog: ");
//        TODO:
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("警告");
                    builder.setMessage(warningMsg);
                    builder.setCancelable(false);
                    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle button click if needed
//                         terminate the runnable on runOnUiThread
                            dialog.dismiss();

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    Toast.makeText(context, "openWarningDialog", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void setWriteData(byte[] writeData) {
        this.writeData = writeData;
        mPort.write(writeData, writeData.length);
    }

}





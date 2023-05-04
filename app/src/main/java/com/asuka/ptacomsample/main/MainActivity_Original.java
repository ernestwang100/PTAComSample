package com.asuka.ptacomsample.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;

public class MainActivity_Original extends AppCompatActivity implements View.OnClickListener {

    ComPort mPort;

    Button mWriteBtn;
    EditText mInputText;

    RecvThread mRecvThread;

    static final String LOG_TAG = "ComSampleApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_original);

        mWriteBtn = findViewById(R.id.button_write);
        mInputText = findViewById(R.id.editText_input);

        mPort = new ComPort();
        mPort.open(3, ComPort.BAUD_115200, 8, 'N', 1);

        mRecvThread = new RecvThread();
        mRecvThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPort.close();
    }

    @Override
    public void onClick(View view) {
        byte[] writeData = mInputText.getText().toString().getBytes();
        mPort.write(writeData, writeData.length);
    }

    class RecvThread extends Thread {

        @Override
        public void run() {
            byte[] readBuf = new byte[200];
            while (true) {
                try {
                    sleep(100);
                } catch (Exception e) {
                    Log.i(LOG_TAG, "Exception: " + e.toString() + " occurs");
                }

                int count = mPort.read(readBuf, readBuf.length);
                if (count > 0) {
                    int i;
                    String received = "";
                    for (i = 0; i < count; i++) {
                        received += String.format("%02X", readBuf[i]);
                    }
                    Log.d(LOG_TAG, "Received = " + received);
                }
            }
        }
    }
}
package com.asuka.ptacomsample.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.second.SettingListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LiveActivity extends AppCompatActivity implements ButtonFreezeListener {
    private static final String TAG = "LiveActivity";
    private Switch themeSW;
    private FloatingActionButton listFAB;
    private TextView tv;
    private Handler handler;
    private ComPort mPort;
    private RecvThread mRecvThread;
    private byte[] writeData;
    private String cmdStart, cmdEnd;

    public LiveActivity(){
        super();
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        tv = findViewById(R.id.tv);
//        themeSW = findViewById(R.id.themeSwitchButton);
        listFAB = findViewById(R.id.listFAB);

        cmdStart = "$LCD+PAGE=0";
        cmdEnd = "\r\n";
        writeData = (cmdStart + cmdEnd).getBytes();

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                tv.setText(msg.obj.toString());
            }
        };
        mRecvThread = new RecvThread(handler, mPort, writeData, this,this);
        mRecvThread.start();

        listFAB.setOnClickListener(v -> {
            writeData = ("$LCD+PAGE=98" +cmdEnd).getBytes();
            mPort.write(writeData, writeData.length);
            Log.d(TAG, "onCreate: + writeData: " + writeData);
            mRecvThread.interrupt();
            Intent intent = new Intent(this, SettingListActivity.class);
            startActivity(intent);
            finish();
        });

//        themeSW.setOnClickListener(v -> {
//            runOnUiThread(() -> {
//                if (!themeSW.isChecked()) {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                }
//                recreate();
//            });
//        });
    }


    @Override
    public void freezeButtons() {

    }

    @Override
    public void unfreezeButtons() {

    }

    @Override
    public void onPause() {
        super.onPause();
        mRecvThread.interrupt();
        mPort.close();
    }
}

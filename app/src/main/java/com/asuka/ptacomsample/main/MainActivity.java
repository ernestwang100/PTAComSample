package com.asuka.ptacomsample.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.second.SettingListActivity;

public class MainActivity extends AppCompatActivity implements ButtonFreezeListener{

    private Button mainMenuBtn, upBtn, downBtn;
    private Switch themeSW;
    private TextView tv;
    private int round = 0;

    private static final String TAG = "MainActivity";
    public static final int FRAGMENT_NUM = 5;
    Fragment selectedFragment = null;
    private Handler handler;
    private ComPort mPort;
    private RecvThread mRecvThread;
    private byte[] writeData;
    private String[] temp;
    private boolean isDarkTheme;

    public MainActivity(){
        super();
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        switchTheme();
//        loadTheme();
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        themeSW = findViewById(R.id.themeSwitchButton);
        mainMenuBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);


        writeData = "$LCD+PAGE=0".getBytes();
        mPort.write(writeData, writeData.length);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                tv.setText(msg.obj.toString());
            }
        };

        mRecvThread = new RecvThread(handler, mPort, writeData, this,this);
        mRecvThread.start();
//        themeSW.setOnClickListener(v -> {
//            runOnUiThread(() -> {
//                if (themeSW.isChecked()) {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                }
//                recreate();
//            });
//        });

        Switch themeSwitchButton = findViewById(R.id.themeSwitchButton);

        themeSwitchButton.setOnClickListener(v -> {
            toggleTheme();
            recreate();
        });
//
//        themeSwitchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (!isChecked) {
//                setTheme(R.style.Theme_PTAComSample_Light);
//            } else {
//                setTheme(R.style.Theme_PTAComSample_Dark);
//            }
//            recreate();
//        });


        mainMenuBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingListActivity.class);
            startActivity(intent);
            writeData = "$LCD+PAGE=98".getBytes();
            mPort.write(writeData, writeData.length);
        });

        upBtn.setOnClickListener(v -> {
            freezeButtons();
            round--;
            round = getValidRoundIndex(round);
            writeData = ("$LCD+PAGE=" + round).getBytes();
            mRecvThread.setWriteData(writeData);
//            mPort.write(writeData, writeData.length);

            Log.d(TAG, "onCreate upBtn: round = " + round);
//            v.setEnabled(false); // Disable the button to prevent multiple clicks
//            new Handler().postDelayed(() -> {
//                v.setEnabled(true); // Enable the button after the delay
//            }, 1000); // 1000 milliseconds = 1 second delay
        });

        downBtn.setOnClickListener(v -> {
            freezeButtons();
            round++;
            round = getValidRoundIndex(round);
            writeData = ("$LCD+PAGE=" + round).getBytes();
            mRecvThread.setWriteData(writeData);
//            mPort.write(writeData, writeData.length);


            Log.d(TAG, "onCreate downBtn: round = " + round);
//            v.setEnabled(false); // Disable the button to prevent multiple clicks
//            new Handler().postDelayed(() -> {
//                v.setEnabled(true); // Enable the button after the delay
//            }, 1000); // 1000 milliseconds = 1 second delay
        });
    }
    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        saveTheme();
    }

    private void loadTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        isDarkTheme = sharedPreferences.getBoolean("dark_theme", false);
        if (isDarkTheme) {
            setTheme(R.style.Theme_PTAComSample_Light);
        } else {
            setTheme(R.style.Theme_PTAComSample_Dark);
        }
    }

    private void saveTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("dark_theme", isDarkTheme);
        editor.apply();
    }


    private void switchTheme() {
        // Retrieve the current theme
        int currentTheme = getCurrentTheme();

        // Determine the new theme based on the current theme
        int newTheme = currentTheme == R.style.Theme_PTAComSample_Light
                ? R.style.Theme_PTAComSample_Dark
                : R.style.Theme_PTAComSample_Light;

        // Set the new theme for the activity
        setTheme(newTheme);

        // Recreate the activity to apply the new theme
        recreate();
    }

    private int getCurrentTheme() {
        // Retrieve the current theme resource ID of the activity
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(androidx.appcompat.R.attr.theme, typedValue, true);
        return typedValue.resourceId;
    }


    @Override
    protected void onPause() {
        super.onPause();
        mRecvThread.interrupt();
        mPort.close();
    }

    private int getValidRoundIndex(int round) {
        return (round % FRAGMENT_NUM + FRAGMENT_NUM) % FRAGMENT_NUM;
    }

    @Override
    public void freezeButtons() {
        runOnUiThread(() -> {
            upBtn.setEnabled(false);
            downBtn.setEnabled(false);
        });
    }

    @Override
    public void unfreezeButtons() {
        runOnUiThread(() -> {
            upBtn.setEnabled(true);
            downBtn.setEnabled(true);
        });
    }
}

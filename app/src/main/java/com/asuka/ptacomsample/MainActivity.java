package com.asuka.ptacomsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private Button mainMenuBtn;
    private Button upBtn;
    private Button downBtn;
    private int round = 0;
    private MainFragmentWelcomeTV mainFragmentWelcomeTV;
    private MainFragmentTV1 mainFragmentTV1;
    private MainFragmentTV2 mainFragmentTV2;
    private MainFragmentTV3 mainFragmentTV3;
    private MainFragmentTV4 mainFragmentTV4;
    private MainFragmentTV5 mainFragmentTV5;
    private MainFragmentTV6 mainFragmentTV6;
    private MainFragmentTV7 mainFragmentTV7;
    private MainFragmentTV8 mainFragmentTV8;
    private ExecutorService executorService = Executors.newCachedThreadPool();
//    ComPort mPort;
//
//    Button mWriteBtn;
//    EditText mInputText;
//    EditText mInputText2;
//
//    RecvThread mRecvThread;
//    Handler handler;
//    String printMessage;
//
//    static final String LOG_TAG = "ComSampleApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainMenuBtn = findViewById(R.id.mainMenuBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);


        mainMenuBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        mainFragmentWelcomeTV = new MainFragmentWelcomeTV();
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrameLayout, mainFragmentWelcomeTV, "WelcomeTV").commit();

        upBtn.setOnClickListener(v -> {
            round--;
            fragmentSwitcher();
        });
        downBtn.setOnClickListener(v -> {
            round++;
            fragmentSwitcher();
        });

    }

    private void fragmentSwitcher() {
        if (round > 8)
            round = 0;
        if (round < 0)
            round = 8;
        switch (round) {
            case 0:
                if (mainFragmentWelcomeTV == null)
                    mainFragmentWelcomeTV = new MainFragmentWelcomeTV();
                if (mainFragmentTV8 != null)
                    mainFragmentTV8 = null;
                if (mainFragmentTV1 != null)
                    mainFragmentTV1 = null;
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, mainFragmentWelcomeTV, "WelcomeTV").commit();
                break;
            case 1:
                if (mainFragmentTV1 == null)
                    mainFragmentTV1 = new MainFragmentTV1(executorService);
                if (mainFragmentTV2 != null)
                    mainFragmentTV2 = null;
                if (mainFragmentWelcomeTV != null)
                    mainFragmentWelcomeTV = null;
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, mainFragmentTV1, "TV1").commit();
                break;
            case 2:
                if (mainFragmentTV2 == null)
                    mainFragmentTV2 = new MainFragmentTV2(executorService);
                if (mainFragmentTV3 != null)
                    mainFragmentTV3 = null;
                if (mainFragmentTV1 != null)
                    mainFragmentTV1 = null;
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, mainFragmentTV2, "TV2").commit();
                break;
            case 3:
                if (mainFragmentTV3 == null)
                    mainFragmentTV3 = new MainFragmentTV3();
                if (mainFragmentTV4 != null)
                    mainFragmentTV4 = null;
                if (mainFragmentTV2 != null)
                    mainFragmentTV2 = null;
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, mainFragmentTV3, "TV3").commit();
                break;
            case 4:
                if (mainFragmentTV4 == null)
                    mainFragmentTV4 = new MainFragmentTV4();
                if (mainFragmentTV5 != null)
                    mainFragmentTV5 = null;
                if (mainFragmentTV3 != null)
                    mainFragmentTV3 = null;
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, mainFragmentTV4, "TV4").commit();
                break;
            case 5:
                if (mainFragmentTV5 == null)
                    mainFragmentTV5 = new MainFragmentTV5();
                if (mainFragmentTV6 != null)
                    mainFragmentTV6 = null;
                if (mainFragmentTV4 != null)
                    mainFragmentTV4 = null;
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, mainFragmentTV5, "TV5").commit();
                break;
            case 6:
                if (mainFragmentTV6 == null)
                    mainFragmentTV6 = new MainFragmentTV6();
                if (mainFragmentTV7 != null)
                    mainFragmentTV7 = null;
                if (mainFragmentTV5 != null)
                    mainFragmentTV5 = null;
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, mainFragmentTV6, "TV6").commit();
                break;
            case 7:
                if (mainFragmentTV7 == null)
                    mainFragmentTV7 = new MainFragmentTV7();
                if (mainFragmentTV8 != null)
                    mainFragmentTV8 = null;
                if (mainFragmentTV6 != null)
                    mainFragmentTV6 = null;
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, mainFragmentTV7, "TV7").commit();
                break;
            case 8:
                if (mainFragmentTV8 == null)
                    mainFragmentTV8 = new MainFragmentTV8(executorService);
                if (mainFragmentWelcomeTV != null)
                    mainFragmentWelcomeTV = null;
                if (mainFragmentTV7 != null)
                    mainFragmentTV7 = null;
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, mainFragmentTV8, "TV8").commit();
                break;
        }
    }
}
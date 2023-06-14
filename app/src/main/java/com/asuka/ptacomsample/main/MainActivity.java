package com.asuka.ptacomsample.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.second.SettingListActivity;

public class MainActivity extends AppCompatActivity {
    private Button mainMenuBtn, upBtn, downBtn;
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
    private static final String TAG = "MainActivity";
    public static final int FRAGMENT_NUM = 8;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().hasExtra("FragmentIndex")) {
            round = getIntent().getIntExtra("FragmentIndex", 0);
            Log.d(TAG, "onCreate: round = " + round);
        } else {
            round = 0;
        }

        initializeFragments();
        fragmentSwitcher(round);

        mainMenuBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);

        mainMenuBtn.setOnClickListener(v -> {
            if (selectedFragment != null) {
                selectedFragment.onPause();
            }
            Intent intent = new Intent(MainActivity.this, SettingListActivity.class);
            startActivity(intent);
        });

        upBtn.setOnClickListener(v -> {
            round--;
            fragmentSwitcher(round);
            Log.d(TAG, "onCreate upBtn: round = " + round);
        });

        downBtn.setOnClickListener(v -> {
            round++;
            fragmentSwitcher(round);
            Log.d(TAG, "onCreate downBtn: round = " + round);
        });
    }

    private void initializeFragments() {
        mainFragmentWelcomeTV = new MainFragmentWelcomeTV();
        mainFragmentTV1 = new MainFragmentTV1();
        mainFragmentTV2 = new MainFragmentTV2();
        mainFragmentTV3 = new MainFragmentTV3();
        mainFragmentTV4 = new MainFragmentTV4();
        mainFragmentTV5 = new MainFragmentTV5();
        mainFragmentTV6 = new MainFragmentTV6();
        mainFragmentTV7 = new MainFragmentTV7();
        mainFragmentTV8 = new MainFragmentTV8();
    }

    private void fragmentSwitcher(int round) {
        Log.d(TAG, "fragmentSwitcher: round = " + round);

        round = getValidRoundIndex(round);
        Log.d(TAG, "fragmentSwitcher: round%FRAGMENT_NUM = " + round);
        switch (round) {
            case 0:
                selectedFragment = mainFragmentWelcomeTV;
                break;
            case 1:
                selectedFragment = mainFragmentTV1;
                break;
            case 2:
                selectedFragment = mainFragmentTV2;
                break;
            case 3:
                selectedFragment = mainFragmentTV3;
                break;
            case 4:
                selectedFragment = mainFragmentTV4;
                break;
            case 5:
                selectedFragment = mainFragmentTV5;
                break;
            case 6:
                selectedFragment = mainFragmentTV6;
                break;
            case 7:
                selectedFragment = mainFragmentTV7;
                break;
            case 8:
                selectedFragment = mainFragmentTV8;
                break;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, selectedFragment).commit();
        }
    }

    private int getValidRoundIndex(int round) {
        return (round % FRAGMENT_NUM + FRAGMENT_NUM) % FRAGMENT_NUM;
    }
}

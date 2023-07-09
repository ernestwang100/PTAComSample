package com.asuka.ptacomsample.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.Switch;

import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.second.SettingListActivity;

public class MainActivity extends AppCompatActivity {
    private Button mainMenuBtn, upBtn, downBtn;
    private Switch themeSW;
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
    public static final int FRAGMENT_NUM = 5;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().hasExtra("FragmentIndex")) {
            round = getIntent().getIntExtra("FragmentIndex", 0);
            Log.d(TAG, "onCreate: round = " + round);
        }

        initializeFragments();
        selectedFragment = mainFragmentWelcomeTV;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, selectedFragment).commit();

        themeSW = findViewById(R.id.themeSwitchButton);
        mainMenuBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);

        themeSW.setOnClickListener(v -> {
            runOnUiThread(() -> {
                if (themeSW.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                recreate();
            });
        });

//        Switch themeSwitchButton = findViewById(R.id.themeSwitchButton);
//
//        themeSwitchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (!isChecked) {
//                setTheme(R.style.Theme_PTAComSample_Light);
//            } else {
//                setTheme(R.style.Theme_PTAComSample_Dark);
//            }
//            recreate();
//        });

//        switchTheme();

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
            v.setEnabled(false); // Disable the button to prevent multiple clicks
            new Handler().postDelayed(() -> {
                v.setEnabled(true); // Enable the button after the delay
            }, 500); // 1000 milliseconds = 1 second delay
        });

        downBtn.setOnClickListener(v -> {
            round++;
            fragmentSwitcher(round);
            Log.d(TAG, "onCreate downBtn: round = " + round);
            v.setEnabled(false); // Disable the button to prevent multiple clicks
            new Handler().postDelayed(() -> {
                v.setEnabled(true); // Enable the button after the delay
            }, 500); // 1000 milliseconds = 1 second delay
        });
    }

//    private void switchTheme() {
//        // Retrieve the current theme
//        int currentTheme = getCurrentTheme();
//
//        // Determine the new theme based on the current theme
//        int newTheme = currentTheme == R.style.Theme_PTAComSample_Light
//                ? R.style.Theme_PTAComSample_Dark
//                : R.style.Theme_PTAComSample_Light;
//
//        // Set the new theme for the activity
//        setTheme(newTheme);
//
//        // Recreate the activity to apply the new theme
//        recreate();
//    }
//
//    private int getCurrentTheme() {
//        // Retrieve the current theme resource ID of the activity
//        TypedValue typedValue = new TypedValue();
//        getTheme().resolveAttribute(androidx.appcompat.R.attr.theme, typedValue, true);
//        return typedValue.resourceId;
//    }


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

package com.asuka.ptacomsample.third;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.asuka.ptacomsample.R;

public class SettingDetailsActivity extends AppCompatActivity {
    private Button homeBtn, upBtn, downBtn, confirmBtn;
    private TextView titleTV;
    private int round = 0;
    private DriverStatusFragment driverStatusFragment;
    private DriverCodeFragment driverCodeFragment;
    private DrivingTimeFragment drivingTimeFragment;
    private Data24hFragment data24hFragment;
    private GainFragment gainFragment;
    private ManufacturerFragment manufacturerFragment;


    private static final String TAG = "SettingDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_details);

        if(getIntent().hasExtra("INDEX")){
            round = getIntent().getIntExtra("INDEX", 0);
            Log.d(TAG, "onCreate: round = " + round);
        }else {
            round = 0;
        }
        titleTV = findViewById(R.id.titleTV);
        homeBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);
        confirmBtn = findViewById(R.id.confirmBtn);

        fragmentSwitcher(round);

        homeBtn.setOnClickListener(v -> {
            finish();
        });

        upBtn.setOnClickListener(v -> {
            fragmentSwitcher(--round);
        });

        downBtn.setOnClickListener(v -> {
            fragmentSwitcher(++round);
        });
    }

    private void fragmentSwitcher(int round){
        Log.d(TAG, "fragmentSwitcher: round = " + round);
        String[] title = getResources().getStringArray(R.array.setting_full_txt);
        round = round % title.length < 0 ? round % title.length + title.length : round % title.length;

        titleTV.setText(title[round]);

        Fragment selectedFragment = null;

        switch (round){
            case 0:
                selectedFragment = new DriverStatusFragment();
                break;
            case 1:
                selectedFragment = new DriverCodeFragment();
                break;
            case 2:
            case 3:
            case 4:
                selectedFragment = new DrivingTimeFragment();
                break;
            case 9:
                selectedFragment = new Data24hFragment();
                break;
            case 15:
            case 16:
                selectedFragment = new GainFragment();
                break;
            case 18:
                selectedFragment = new ManufacturerFragment();
                break;

            default:
                break;
        }
        if(selectedFragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.settingDetailsFragmentContainer, selectedFragment).commit();
        }
    }
}



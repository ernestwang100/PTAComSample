package com.asuka.ptacomsample.third;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;

public class SettingDetailsActivity extends AppCompatActivity {
    private Button homeBtn, upBtn, downBtn, confirmBtn;
    private TextView titleTV;
    private int round = 0;
    private String cmd;
    private ComPort mPort;
    private byte[] writeData;
    private DriverStatusFragment driverStatusFragment;
    private DriverCodeFragment driverCodeFragment;
    private DrivingTimeFragment drivingTimeFragment;
    private Data24hFragment data24hFragment;
    private GainFragment gainFragment;
    private ManufacturerFragment manufacturerFragment;
    private PrintFragment printFragment;


    private static final String TAG = "SettingDetailsActivity";

    public SettingDetailsActivity() {
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }

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

        confirmBtn.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: cmd = " + cmd);
            if (driverStatusFragment != null) {
                cmd = driverStatusFragment.getCmd();
                Log.d(TAG, "onCreate: cmd = " + cmd);
            }

            if (cmd != null) {
                writeData = cmd.getBytes();
                Log.d(TAG, "onCreate: writeData = " + writeData);
                mPort.write(writeData, writeData.length);
            }
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
                driverStatusFragment = new DriverStatusFragment();
                selectedFragment = driverStatusFragment;
                break;
            case 1:
                driverCodeFragment = new DriverCodeFragment();
                selectedFragment = driverCodeFragment;
                break;
            case 2:
            case 3:
            case 4:
                drivingTimeFragment = new DrivingTimeFragment();
                selectedFragment = drivingTimeFragment;
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                printFragment = new PrintFragment();
                selectedFragment = printFragment;
                break;
            case 9:
                data24hFragment = new Data24hFragment();
                selectedFragment = data24hFragment;
                break;
            case 15:
            case 16:
                gainFragment = new GainFragment();
                selectedFragment = gainFragment;
                break;
            case 18:
                manufacturerFragment = new ManufacturerFragment();
                selectedFragment = manufacturerFragment;
            default:
                break;
        }
        if(selectedFragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.settingDetailsFragmentContainer, selectedFragment).commit();
        }
    }
}



package com.asuka.ptacomsample.third;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.DataUpdateListener;
import com.asuka.ptacomsample.main.MainActivity;
import com.asuka.ptacomsample.second.SettingListActivity;

public class SettingDetailsActivity extends AppCompatActivity implements LoginDialogListener {
    private Button homeBtn, upBtn, downBtn, confirmBtn;
    private FrameLayout settingDetailsFragmentContainer;
    private TextView titleTV;
    private int round = 0;
    private String cmd;
    private String[] title;
    private ComPort mPort;
    private byte[] writeData;
    private DriverStatusFragment driverStatusFragment;
    private DriverCodeFragment driverCodeFragment;
    private DrivingTimeFragment drivingTimeFragment;
    private GainFragment speedGainFragment, rpmGainFragment;
    private ManufacturerFragment manufacturerFragment;
    private PrintFragment printFragment;
    private DownloadFragment downloadFragment;
    private ThresholdTimeFragment drivingThresholdTimeFragment, restingThresholdTimeFragment;
    private SystemTimeFragment systemTimeFragment;
    private BrightnessFragment brightnessFragment;
    private Fragment selectedFragment = null;

    private static final String TAG = "SettingDetailsActivity";

    private DataUpdateListener dataUpdateListener = new DataUpdateListener() {
        @Override
        public void onDataUpdated(String newData) {
            // Handle the updated data here
            Log.d(TAG, "Received updated data: " + newData);
            // Perform any necessary operations based on the updated data
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_details);

        if (getIntent().hasExtra("INDEX")) {
            round = getIntent().getIntExtra("INDEX", 0);
            Log.d(TAG, "onCreate: round = " + round);
        } else {
            round = 0;
        }
        titleTV = findViewById(R.id.titleTV);
        homeBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);
        confirmBtn = findViewById(R.id.confirmBtn);
        settingDetailsFragmentContainer = findViewById(R.id.settingDetailsFragmentContainer);

        title = getResources().getStringArray(R.array.setting_full_txt);
        fragmentSwitcher(round);

        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(SettingDetailsActivity.this, SettingListActivity.class);
            startActivity(intent);
//            finish();
        });

        upBtn.setOnClickListener(v -> {
            fragmentSwitcher(--round);
            round = getValidRoundIndex(round, title.length);
        });

        downBtn.setOnClickListener(v -> {
            fragmentSwitcher(++round);
            round = getValidRoundIndex(round, title.length);
        });

        confirmBtn.setOnClickListener(v -> {
            cmd = getCmdFromSelectedFragment();
            if (needLoginCredentials()) {
                showLoginFragment();
            } else if (needWaiting()) {
                Log.d(TAG, "onCreate: cmd = " + cmd);
                writeDataToPort(cmd);
                showWaitingFragment();
            } else {
                Log.d(TAG, "onCreate: cmd = " + cmd);
                writeDataToPort(cmd);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.registerDataUpdateListener(dataUpdateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.unregisterDataUpdateListener(dataUpdateListener);
    }

    private void fragmentSwitcher(int round) {
        Log.d(TAG, "fragmentSwitcher: round = " + round);

        round = getValidRoundIndex(round, title.length);
        titleTV.setText(title[round]);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        switch (round) {
            case 0:
                if (driverStatusFragment == null)
                    driverStatusFragment = new DriverStatusFragment();
                selectedFragment = driverStatusFragment;

                layoutParams.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 3, getResources().getDisplayMetrics()), 0, 0);
                settingDetailsFragmentContainer.setLayoutParams(layoutParams);
                break;

            case 1:
                if (driverCodeFragment == null)
                    driverCodeFragment = new DriverCodeFragment();
                selectedFragment = driverCodeFragment;
                break;
            case 2:
                if (drivingTimeFragment == null)
                    drivingTimeFragment = new DrivingTimeFragment();
                selectedFragment = drivingTimeFragment;

                layoutParams.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 3, getResources().getDisplayMetrics()), 0, 0);
                settingDetailsFragmentContainer.setLayoutParams(layoutParams);
                break;

            case 3:
                if (printFragment == null)
                    printFragment = new PrintFragment();
                selectedFragment = printFragment;

                layoutParams.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()), 0, 0);
                settingDetailsFragmentContainer.setLayoutParams(layoutParams);
                break;

            case 4:
                if (downloadFragment == null)
                    downloadFragment = new DownloadFragment();
                selectedFragment = downloadFragment;
                break;

            case 5:
                if (drivingThresholdTimeFragment == null)
                    drivingThresholdTimeFragment = new ThresholdTimeFragment(0);
                selectedFragment = drivingThresholdTimeFragment;
                break;

            case 6:
                if (restingThresholdTimeFragment == null)
                    restingThresholdTimeFragment = new ThresholdTimeFragment(1);
                selectedFragment = restingThresholdTimeFragment;
                break;

            case 7:
                if (speedGainFragment == null)
                    speedGainFragment = new GainFragment(0);
                selectedFragment = speedGainFragment;
                break;

            case 8:
                if (rpmGainFragment == null)
                    rpmGainFragment = new GainFragment(1);
                selectedFragment = rpmGainFragment;
                break;

            case 9:
                if (systemTimeFragment == null)
                    systemTimeFragment = new SystemTimeFragment();
                selectedFragment = systemTimeFragment;
                break;

            case 10:
                if (brightnessFragment == null)
                    brightnessFragment = new BrightnessFragment();
                selectedFragment = brightnessFragment;
                break;

            case 11:
                if (manufacturerFragment == null)
                    manufacturerFragment = new ManufacturerFragment();
                selectedFragment = manufacturerFragment;
                break;
            default:
                break;
        }
        if (selectedFragment != null) {
            FrameLayout fragmentContainer = findViewById(R.id.settingDetailsFragmentContainer);
            fragmentContainer.removeAllViews();
            getSupportFragmentManager().beginTransaction().replace(R.id.settingDetailsFragmentContainer, selectedFragment).commit();
        }
    }

    private int getValidRoundIndex(int round, int titleLength) {
        return round % titleLength < 0 ? round % titleLength + titleLength : round % titleLength;
    }

    private String getCmdFromSelectedFragment() {
        String cmd = null;

        if (selectedFragment instanceof DriverStatusFragment) {
            cmd = ((DriverStatusFragment) selectedFragment).getCmd();
        } else if (selectedFragment instanceof DriverCodeFragment) {
            cmd = ((DriverCodeFragment) selectedFragment).getCmd();
        } else if (selectedFragment instanceof DrivingTimeFragment) {
            cmd = ((DrivingTimeFragment) selectedFragment).getCmd();
        } else if (selectedFragment instanceof GainFragment) {
            cmd = ((GainFragment) selectedFragment).getCmd();
        } else if (selectedFragment instanceof ManufacturerFragment) {
            cmd = ((ManufacturerFragment) selectedFragment).getCmd();
        } else if (selectedFragment instanceof PrintFragment) {
            cmd = ((PrintFragment) selectedFragment).getCmd();
        } else if (selectedFragment instanceof DownloadFragment) {
            cmd = ((DownloadFragment) selectedFragment).getCmd();
        } else if (selectedFragment instanceof ThresholdTimeFragment) {
            cmd = ((ThresholdTimeFragment) selectedFragment).getCmd();
        } else if (selectedFragment instanceof SystemTimeFragment) {
            cmd = ((SystemTimeFragment) selectedFragment).getCmd();
        }

        return cmd;
    }

    private boolean needWaiting() {
        return selectedFragment instanceof PrintFragment || selectedFragment instanceof DownloadFragment;
    }

    private void showWaitingFragment() {
        WaitingFragment waitingFragment = new WaitingFragment();
        waitingFragment.show(getSupportFragmentManager(), "waiting");
    }

    private boolean needLoginCredentials() {
        return selectedFragment instanceof GainFragment || selectedFragment instanceof ThresholdTimeFragment || selectedFragment instanceof SystemTimeFragment;
    }

    private void showLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        loginFragment.show(getSupportFragmentManager(), "LoginFragment");
    }

    private void writeDataToPort(String cmd) {
        if (cmd != null) {
            writeData = cmd.getBytes();
            Log.d(TAG, "writeDataToPort: writeData = " + writeData);
            mPort.write(writeData, writeData.length);
        }
    }

    @Override
    public void onLoginResult(String username, boolean isLoginSuccessful) {
        if (isLoginSuccessful) {
            Toast.makeText(this, "Login successful for user: " + username, Toast.LENGTH_SHORT).show();
            String cmd = getCmdFromSelectedFragment();
            Log.d(TAG, "onCreate: cmd = " + cmd);
            writeDataToPort(cmd);
        } else {
            Toast.makeText(this, "Login failed for user: " + username, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoginCancelled() {
        Toast.makeText(this, "Login cancelled", Toast.LENGTH_SHORT).show();
    }
}



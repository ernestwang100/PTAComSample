package com.asuka.ptacomsample.third;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.widget.FrameLayout;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
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
    private GainFragment gainFragment;
    private ManufacturerFragment manufacturerFragment;
    private PrintFragment printFragment;
    private DownloadFragment downloadFragment;
    private ThresholdTimeFragment thresholdTimeFragment;
    private SystemTimeFragment systemTimeFragment;
    private BrightnessFragment brightnessFragment;
    private Fragment selectedFragment = null;

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
        settingDetailsFragmentContainer = findViewById(R.id.settingDetailsFragmentContainer);

        title = getResources().getStringArray(R.array.setting_full_txt);
        fragmentSwitcher(round);

        homeBtn.setOnClickListener(v -> {
//            finish();
//            onBackPressed();

            Intent intent = new Intent();
            intent.setClass(SettingDetailsActivity.this, SettingListActivity.class);
            startActivity(intent);
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


    private void fragmentSwitcher(int round){
        Log.d(TAG, "fragmentSwitcher: round = " + round);

        round = getValidRoundIndex(round, title.length);
        titleTV.setText(title[round]);


        switch (round){
            case 0:
                driverStatusFragment = new DriverStatusFragment();
                selectedFragment = driverStatusFragment;

                LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 3, getResources().getDisplayMetrics()), 0, 0);

//               Set the top margin to 30sp
                settingDetailsFragmentContainer.setLayoutParams(layoutParams);

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
                printFragment = new PrintFragment();
                selectedFragment = printFragment;
                break;
            case 6:
                downloadFragment = new DownloadFragment();
                selectedFragment = downloadFragment;
                break;
            case 7:
                thresholdTimeFragment = new ThresholdTimeFragment(0);
                selectedFragment = thresholdTimeFragment;
                break;
            case 8:
                thresholdTimeFragment = new ThresholdTimeFragment(1);
                selectedFragment = thresholdTimeFragment;
                break;
            case 9:
                gainFragment = new GainFragment(0);
                selectedFragment = gainFragment;
                break;
            case 10:
                gainFragment = new GainFragment(1);
                selectedFragment = gainFragment;
                break;
            case 11:
                systemTimeFragment = new SystemTimeFragment();
                selectedFragment = systemTimeFragment;
                break;
            case 12:
                brightnessFragment = new BrightnessFragment();
                selectedFragment = brightnessFragment;
                break;
            case 13:
                manufacturerFragment = new ManufacturerFragment();
                selectedFragment = manufacturerFragment;
                break;
            default:
                break;
        }
        if(selectedFragment != null){
            // Get the reference to the fragment container
            FrameLayout fragmentContainer = findViewById(R.id.settingDetailsFragmentContainer);

            // Clear the fragment container by removing all views
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
        if (selectedFragment instanceof PrintFragment || selectedFragment instanceof DownloadFragment) {
            return true;
        }
        return false;
    }
    private void showWaitingFragment() {
        WaitingFragment waitingFragment = new WaitingFragment();
        waitingFragment.show(getSupportFragmentManager(), "waiting");
    }
    private boolean needLoginCredentials() {

        if (selectedFragment instanceof GainFragment || selectedFragment instanceof ThresholdTimeFragment || selectedFragment instanceof SystemTimeFragment) {
            return true;
        }
        return false;
    }
    private void showLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog); // Set the custom dialog style
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
            // Login successful
            Toast.makeText(this, "Login successful for user: " + username, Toast.LENGTH_SHORT).show();
            String cmd = getCmdFromSelectedFragment();
            Log.d(TAG, "onCreate: cmd = " + cmd);
            writeDataToPort(cmd);
        } else {
            // Login failed
            Toast.makeText(this, "Login failed for user: " + username, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoginCancelled() {
        Toast.makeText(this, "Login cancelled", Toast.LENGTH_SHORT).show();
    }



}



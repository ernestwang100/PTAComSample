package com.asuka.ptacomsample.third;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;

public class SettingDetailsActivity extends AppCompatActivity implements LoginDialogListener {
    private Button homeBtn, upBtn, downBtn, confirmBtn;
    private TextView titleTV;
    private int round = 0;
    private String cmd;
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

            if(selectedFragment instanceof DriverStatusFragment){
                cmd = ((DriverStatusFragment) selectedFragment).getCmd();
            } else if(selectedFragment instanceof DriverCodeFragment){
                cmd = ((DriverCodeFragment) selectedFragment).getCmd();
            } else if(selectedFragment instanceof DrivingTimeFragment) {
                cmd = ((DrivingTimeFragment) selectedFragment).getCmd();
            } else if(selectedFragment instanceof GainFragment) {
                if(showLoginDialog()) {
                    cmd = ((GainFragment) selectedFragment).getCmd();
                }
            } else if(selectedFragment instanceof ManufacturerFragment) {
                cmd = ((ManufacturerFragment) selectedFragment).getCmd();
            } else if(selectedFragment instanceof PrintFragment) {
                cmd = ((PrintFragment) selectedFragment).getCmd();
            } else if(selectedFragment instanceof DownloadFragment) {
                cmd = ((DownloadFragment) selectedFragment).getCmd();
            } else if(selectedFragment instanceof ThresholdTimeFragment) {
                cmd = ((ThresholdTimeFragment) selectedFragment).getCmd();
            }

            Log.d(TAG, "onCreate: cmd = " + cmd);



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
                printFragment = new PrintFragment();
                selectedFragment = printFragment;
                break;
            case 6:
                downloadFragment = new DownloadFragment();
                selectedFragment = downloadFragment;
                break;
            case 7:
            case 8:
                thresholdTimeFragment = new ThresholdTimeFragment();
                selectedFragment = thresholdTimeFragment;
                break;
            case 9:
            case 10:
                gainFragment = new GainFragment();
                selectedFragment = gainFragment;
                break;
            case 13:
                manufacturerFragment = new ManufacturerFragment();
                selectedFragment = manufacturerFragment;
                break;
            default:
                break;
        }
        if(selectedFragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.settingDetailsFragmentContainer, selectedFragment).commit();
        }
    }

    private boolean showLoginDialog() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.show(getSupportFragmentManager(), "login_dialog");
        Log.d(TAG, "showLoginDialog: isLoginSuccessful = " + loginFragment.getIsLoginSuccessful() );
        return loginFragment.getIsLoginSuccessful();
    }

    @Override
    public void onLoginSuccess(String username) {
        Toast.makeText(this, "Login successful for user: " + username, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "Login cancelled", Toast.LENGTH_SHORT).show();
    }
}



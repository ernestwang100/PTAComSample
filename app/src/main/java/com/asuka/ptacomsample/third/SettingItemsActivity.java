package com.asuka.ptacomsample.third;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.ButtonFreezeListener;
import com.asuka.ptacomsample.main.LiveActivity;
import com.asuka.ptacomsample.main.RecvThread;
import com.asuka.ptacomsample.second.SettingListActivity;

public class SettingItemsActivity extends AppCompatActivity implements LoginDialogListener, ButtonFreezeListener {
    private ImageButton homeBtn, upBtn, downBtn, confirmBtn;
    private FrameLayout settingDetailsFragmentContainer;
    private TextView titleTV;
    private int round = 0;
    private String cmd, cmdStart, title[];
    private Handler handler;
    private ComPort mPort;
    private RecvThread mRecvThread;
    private byte[] writeData;
    private String[] temp;
    private DriverStatusFragment driverStatusFragment;
    private DriverCodeFragment driverCodeFragment;
    private DrivingTimeFragment drivingTimeFragment;
    private GainFragment speedGainFragment, rpmGainFragment;
    private ManufacturerFragment manufacturerFragment;
    private PrintFragment printFragment;
    private DownloadFragment downloadFragment;
    private ThresholdTimeFragment drivingThresholdTimeFragment;
    private SystemTimeFragment systemTimeFragment;
    private BrightnessFragment brightnessFragment;
    private Fragment selectedFragment = null;
    private WaitingFragment waitingFragment;
    private GpsFragment gpsFragment;
    private EventFragment eventFragment;

    private static final String TAG = "SettingItemsActivity";


    public SettingItemsActivity() {
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
//        mRecvThread = new RecvThread(handler, mPort, null, this, this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_details);

//        if (getIntent().hasExtra("INDEX")) {
//            round = getIntent().getIntExtra("INDEX", 0);
//            Log.d(TAG, "onCreate: round = " + round);
//        } else {
//            round = 0;
//        }
        titleTV = findViewById(R.id.titleTV);
        homeBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);
        confirmBtn = findViewById(R.id.confirmBtn);
        settingDetailsFragmentContainer = findViewById(R.id.settingDetailsFragmentContainer);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                temp = msg.obj.toString().split(",");
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = temp[i].trim();
                }
                Log.d(TAG, "handleMessage: temp = " + temp[0]);
                updateValuestoFragment(temp);
            }
        };

        mRecvThread = new RecvThread(handler, mPort, this, this);
        mRecvThread.start();

        title = getResources().getStringArray(R.array.setting_items_txt);
        fragmentSwitcher(round);

        homeBtn.setOnClickListener(v -> {
//            finish();
//            onBackPressed();

            Intent intent = new Intent();
            intent.setClass(SettingItemsActivity.this, SettingListActivity.class);
            startActivity(intent);
        });

        upBtn.setOnClickListener(v -> {
            freezeButtons();
            fragmentSwitcher(--round);
            round = getValidRoundIndex(round, title.length);
        });

        downBtn.setOnClickListener(v -> {
            freezeButtons();
            fragmentSwitcher(++round);
            round = getValidRoundIndex(round, title.length);
        });

        confirmBtn.setOnClickListener(v -> {
            freezeButtons();
            cmd = getCmdFromSelectedFragment();
            if (needLoginCredentials()) {
                showLoginFragment();
                unfreezeButtons();
            } else if (needWaiting()) {
                Log.d(TAG, "onCreate: cmd = " + cmd);
                writeDataToPort(cmd);
                showWaitingFragment();
//                TODO: 這邊要改成等待回傳的方式
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: temp[0] = " + temp[0]);
                        if (temp[0].equals("0")) {
                            waitingFragment.setmStr("處理完成");
                            waitingFragment.dismiss();
                        } else {
                            waitingFragment.setmStr("處理中...");
                        }

                    }
                }, 1000);


            } else {
                Log.d(TAG, "onCreate: cmd = " + cmd);
                writeDataToPort(cmd);
            }

//            v.setEnabled(false); // Disable the button to prevent multiple clicks
//            new Handler().postDelayed(() -> {
//                v.setEnabled(true); // Enable the button after the delay
//            }, 2000); // 1000 milliseconds = 1 second delay
        });


    }


    private void fragmentSwitcher(int round) {
        Log.d(TAG, "fragmentSwitcher: round = " + round);

        round = getValidRoundIndex(round, title.length);
        titleTV.setText(title[round]);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        switch (round) {
            case 0:
                if (drivingThresholdTimeFragment == null)
                    drivingThresholdTimeFragment = new ThresholdTimeFragment();
                selectedFragment = drivingThresholdTimeFragment;
                cmdStart = "$LCD+SET THRESHOLD TIME=";
                break;

            case 1:
                if (speedGainFragment == null)
                    speedGainFragment = new GainFragment();
                selectedFragment = speedGainFragment;
                cmdStart = "$LCD+GAIN=";
                break;

            case 2:
                if (gpsFragment == null)
                    gpsFragment = new GpsFragment();
                selectedFragment = gpsFragment;
                cmdStart = "$LCD+PAGE=4";
                break;

            case 3:
                if (systemTimeFragment == null)
                    systemTimeFragment = new SystemTimeFragment();
                selectedFragment = systemTimeFragment;
                cmdStart = "$LCD+TIME ADJ=";
                break;

            case 4:
                if (brightnessFragment == null)
                    brightnessFragment = new BrightnessFragment();
                selectedFragment = brightnessFragment;
                cmdStart = "$LCD+INFO=";
//                this.unfreezeButtons();
                break;

            case 5:
                if (eventFragment == null)
                    eventFragment = new EventFragment();
                selectedFragment = eventFragment;
                cmdStart = "$LCD+PAGE=2";
                break;

            case 6:
                if (manufacturerFragment == null)
                    manufacturerFragment = new ManufacturerFragment();
                selectedFragment = manufacturerFragment;
                cmdStart = "$LCD+INFO=";
//                this.unfreezeButtons();
                break;
            default:
                break;
        }
        if (!cmdStart.contains("PAGE")){
            writeData = (cmdStart + "?").getBytes();
        }else{
            writeData = cmdStart.getBytes();
        }
        mRecvThread.setWriteData(writeData);

        if (selectedFragment != null) {
            // Get the reference to the fragment container
            FrameLayout fragmentContainer = findViewById(R.id.settingDetailsFragmentContainer);

            // Clear the fragment container by removing all views
            fragmentContainer.removeAllViews();

            getSupportFragmentManager().beginTransaction().replace(R.id.settingDetailsFragmentContainer, selectedFragment).commit();
//            selectedFragment.setRecvThread(mRecvThread);
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
        waitingFragment = new WaitingFragment();
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
            Log.d(TAG, "writeDataToPort: writeData = " + new String(writeData));
            mPort.write(writeData, writeData.length);
        }
        this.unfreezeButtons();
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

    public void updateValuestoFragment(String[] temp) {
        Log.d(TAG, "passTemptoFragment: temp[0] = " + temp[0]);
        if (selectedFragment instanceof DriverStatusFragment) {
            ((DriverStatusFragment) selectedFragment).updateValues(temp);
        } else if (selectedFragment instanceof DriverCodeFragment) {
            ((DriverCodeFragment) selectedFragment).updateValues(temp);
        } else if (selectedFragment instanceof DrivingTimeFragment) {
            ((DrivingTimeFragment) selectedFragment).updateValues(temp);
        } else if (selectedFragment instanceof GainFragment) {
            ((GainFragment) selectedFragment).updateValues(temp);
        } else if (selectedFragment instanceof ManufacturerFragment) {
            ((ManufacturerFragment) selectedFragment).updateValues(temp);
        } else if (selectedFragment instanceof ThresholdTimeFragment) {
            ((ThresholdTimeFragment) selectedFragment).updateValues(temp);
        } else if (selectedFragment instanceof SystemTimeFragment) {
            ((SystemTimeFragment) selectedFragment).updateValues(temp);
        } else if (selectedFragment instanceof PrintFragment) {
            ((PrintFragment) selectedFragment).updateValues(temp);
        } else if (selectedFragment instanceof DownloadFragment) {
            ((DownloadFragment) selectedFragment).updateValues(temp);
        } else if (selectedFragment instanceof GpsFragment) {
            ((GpsFragment) selectedFragment).updateValues(temp);
        } else if (selectedFragment instanceof EventFragment) {
            ((EventFragment) selectedFragment).updateValues(temp);
        }
    }

    @Override
    public void freezeButtons() {
        runOnUiThread(() -> {
            upBtn.setEnabled(false);
            downBtn.setEnabled(false);
            confirmBtn.setEnabled(false);
        });
    }

    @Override
    public void unfreezeButtons() {
        runOnUiThread(() -> {
            upBtn.setEnabled(true);
            downBtn.setEnabled(true);
            confirmBtn.setEnabled(true);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecvThread.interrupt();
        mPort.close();
    }
}



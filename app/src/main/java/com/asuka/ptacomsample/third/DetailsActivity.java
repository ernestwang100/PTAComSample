package com.asuka.ptacomsample.third;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.second.SettingListActivity;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;

public class DetailsActivity extends AppCompatActivity {
    private Button homeBtn, upBtn, downBtn, confirmBtn;
    private TextView titleTV, detailsTV;
    private String[] titles;
    private String[] details;
    private int currentIndex;
    private Switch lightSwitch;
    private EditText editText_input;
    private Dialog mDlgPilotCode, mDlgLogin;
    private ExecutorService executorService;
    private byte[] writeData;
    private ComPort mPort;

    private Thread thread;
    private Handler handler;
    private static final String TAG = "DetailsActivity";
    private volatile boolean stopThread = false;

    public DetailsActivity() {
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent it = getIntent();
        titles = it.getStringArrayExtra("TITLES");
        details = it.getStringArrayExtra("DETAILS");
        currentIndex = it.getIntExtra("INDEX", 0);
        Log.d("titles", String.valueOf(titles));
        Log.d("details", String.valueOf(details));
        Log.d("currentIndex", String.valueOf(currentIndex));


        homeBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);
        titleTV = findViewById(R.id.titleTV);
        detailsTV = findViewById(R.id.detailsTV);
        lightSwitch = findViewById(R.id.lightSwitch);
        editText_input = findViewById(R.id.editText_input);
        confirmBtn = findViewById(R.id.confirmBtn);

        updateViews();

//        TODO
        mPort.write(writeData, writeData.length);





        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(DetailsActivity.this, SettingListActivity.class);
            startActivity(intent);
        });

        upBtn.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                updateViews();
            }
        });

        downBtn.setOnClickListener(v -> {
            if (currentIndex < titles.length - 1) {
                currentIndex++;
                updateViews();
            }
        });

        detailsTV.setOnClickListener(v -> {
            if (currentIndex == 1) {
                showPilotCodeDialog();
            } else if (currentIndex == 10) {
                showDatePickerDialog();
            } else if (currentIndex == 13 || currentIndex == 14) {
                showTimePickerDialog();
            } else if (currentIndex == 15 || currentIndex == 16) {
                showNumberPickerDialog();

            }
        });


        confirmBtn.setOnClickListener(v -> {
            if (currentIndex == 13 || currentIndex == 14 || currentIndex == 15 || currentIndex == 16) {
                showLoginDialog();

                updateViews();
            }
        });



    }


    public void startThread() {
        stopThread = false;
        RunnableRead runnable = new RunnableRead();
        thread = new Thread(runnable);
        thread.start();
    }


    public void stopThread() {
        stopThread = true;
    }

//    TODO
    class RunnableRead implements Runnable {
        @Override
        public void run() {
            byte[] readData = new byte[1024];
            if(stopThread)  return;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.i(TAG, "run: "+e.getMessage());
                e.printStackTrace();
            }
            int readLen = mPort.read(readData, readData.length);
            if (readLen > 0) {
                String received = "";
                for (int i = 0; i < readLen; i++) {
                    received += String.format("%02X ", readData[i]);
                }
                Log.d(TAG, String.valueOf(readData));
            }
        }
    }

    private void updateViews() {
//        if (currentIndex == 0 || currentIndex == 1 || currentIndex == 9 || (currentIndex >= 13 && currentIndex <= 16)) {
//            editText_input.setVisibility(View.VISIBLE);
//            detailsTV.setVisibility(View.GONE);
//            lightSwitch.setVisibility(View.GONE);
//        } else {
        editText_input.setVisibility(View.GONE);
        detailsTV.setVisibility(View.VISIBLE);
        if (currentIndex == 17) {
            lightSwitch.setVisibility(View.VISIBLE);
        } else {
            lightSwitch.setVisibility(View.GONE);
        }
//        }


        switch(currentIndex){
            case 0:
                writeData = "$LCD+DRIVER STATUS=ID,STATUS".getBytes();
                break;
            case 1:
                writeData = "$LCD+DRIVER IN=ID,NUMBER".getBytes();
                break;
            case 2:
                writeData = "$LCD+DRIVER TIME=?".getBytes();
                break;
            case 3:
                writeData = "$LCD+STANDBY TIME=?".getBytes();
                break;
            case 4:
                writeData = "$LCD+REST TIME=?".getBytes();
                break;
            case 5:
                writeData = "$LCD+PRINT=1".getBytes();
                break;
            case 6:
                writeData = "$LCD+PRINT=2".getBytes();
                break;
            case 7:
                writeData = "$LCD+PRINT=3".getBytes();
                break;
            case 8:
                writeData = "$LCD+PRINT=4".getBytes();
                break;
            case 9:
                writeData = "$LCD+DOWNLOAD=24".getBytes();
                break;
            case 10:
                writeData = "$LCD+DOWNLOAD=YYYYMMDD".getBytes();
                break;
            case 11:
                writeData = "$LCD+DOWNLOAD=NOW".getBytes();
                break;
            case 12:
                writeData = "$LCD+TIME ADJ=YYYYDDMMhhmmss".getBytes();
                break;
            case 13:
                writeData = "$LCD+SET DRIVE TIME=hh,mm".getBytes();
                break;
            case 14:
                writeData = "$LCD+SET REST TIME=hh,mm".getBytes();
                break;
            case 15:
                writeData = "$LCD+SPEED GAIN=".getBytes();
                break;
            case 16:
                writeData = "$LCD+RPM DIV=".getBytes();
                break;
            case 18:
                writeData = "$LCD+INFO=".getBytes();
                break;
            default:
                break;


        }


        titleTV.setText(titles[currentIndex]);
        detailsTV.setText(details[currentIndex]);


    }

    private View.OnClickListener lDlgBtnOKOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText edtName = (EditText) mDlgLogin.findViewById(R.id.edtName);
            EditText edtPassword = (EditText) mDlgLogin.findViewById(R.id.edtPassword);
//            it.putExtra("DETAILS",)
            updateViews();
            mDlgLogin.dismiss();
        }
    };

    private View.OnClickListener lDlgBtnCancelOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDlgLogin.dismiss();
        }
    };

    private View.OnClickListener pDlgBtnOKOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText edtCodePilot = (EditText) mDlgPilotCode.findViewById(R.id.edtCodePilot);
            EditText edtCodeCopilot = (EditText) mDlgPilotCode.findViewById(R.id.edtCodeCopilot);
            details[currentIndex] = edtCodePilot.getText().toString() + "/" + edtCodeCopilot.getText().toString();
            updateViews();
            mDlgPilotCode.dismiss();
        }
    };

    private View.OnClickListener pDlgBtnCancelOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDlgPilotCode.dismiss();
        }
    };

    private void showPilotCodeDialog() {
        mDlgPilotCode = new Dialog(this);
        mDlgPilotCode.setCancelable(false);
        mDlgPilotCode.setContentView(R.layout.dlg_code_pilot);
        Button btnOK = (Button) mDlgPilotCode.findViewById(R.id.pbtnOK);
        Button btnCancel = (Button) mDlgPilotCode.findViewById(R.id.pbtnCancel);
        btnOK.setOnClickListener(pDlgBtnOKOnClick);
        btnCancel.setOnClickListener(pDlgBtnCancelOnClick);
        mDlgPilotCode.show();

    }

    private void showLoginDialog(){
        mDlgLogin = new Dialog(this);
        mDlgLogin.setCancelable(false);
        mDlgLogin.setContentView(R.layout.dlg_login);
        Button btnOK = (Button) mDlgLogin.findViewById(R.id.lbtnOK);
        Button btnCancel = (Button) mDlgLogin.findViewById(R.id.lbtnCancel);
        btnOK.setOnClickListener(lDlgBtnOKOnClick);
        btnCancel.setOnClickListener(lDlgBtnCancelOnClick);
        mDlgLogin.show();

    }

    private void showNumberPickerDialog() {
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(1);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a number:");
        builder.setView(numberPicker);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                details[currentIndex] = String.valueOf(numberPicker.getValue());
                updateViews();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(DetailsActivity.this, (view, year, month, dayOfMonth) -> {
            String date = year + "/" + (month + 1) + "/" + dayOfMonth;
            details[currentIndex] = date;
            updateViews();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showTimePickerDialog(){
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(DetailsActivity.this, (view, hourOfDay, minute) -> {
            String time = hourOfDay + ":" + minute;
            details[currentIndex] = time;
            updateViews();
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.setMessage("Select Proper Time");
        timePickerDialog.setIcon(R.drawable.baseline_access_time_24);
        timePickerDialog.setCancelable(false);
        timePickerDialog.show();
    }



}
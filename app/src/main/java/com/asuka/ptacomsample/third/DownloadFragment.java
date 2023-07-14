package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.RecvThread;

public class DownloadFragment extends Fragment {
    private RadioGroup radioGroupDownload;
    private String cmd, cmdStart, temp[];
    private DatePickerFragment datePickerFragment;
    private byte[] writeData;
    private ComPort mPort;
    private RecvThread mRecvThread;
    private Handler handler;
    private static final String TAG = "DownloadFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);


        radioGroupDownload = view.findViewById(R.id.radioGroupDownload);
        radioGroupDownload.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.download1RB:
                    showDatePickerDialog();
                    break;
                case R.id.download2RB:
                    cmd = cmdStart + "2,0";
                    break;
                case R.id.download3RB:
                    cmd = cmdStart + "3,24";
                    break;

            }
        });

//        TODO: cmd
        cmdStart = "$LCD+DOWNLOAD=";
//        handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message msg) {
//                Log.d(TAG, "handleMessage: " + msg.obj.toString());
//                temp = msg.obj.toString().split(",");
//                for (int i = 0; i < temp.length; i++) {
//                    temp[i] = temp[i].trim();
//                }
//
//                switch (temp[0]) {
//                    case "0":
//                        Log.d(TAG, "handleMessage: processing complete");
//                        break;
//                    case "1":
//                        Log.d(TAG, "handleMessage: processing");
//                        break;
//                    case "2":
//                        Log.d(TAG, "handleMessage: processing error");
//                        break;
//                }
//            }
//        };

        return view;
    }

    private void showDatePickerDialog() {
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSelectedListener((year, month, day) -> {
            // Handle the selected date
            String selectedDate = String.format("%04d%02d%02d", year, month + 1, day);
            RadioButton download1RB = getView().findViewById(R.id.download1RB);
            download1RB.setText(selectedDate + " 下載");

            cmd = cmdStart + "1," + selectedDate;
        });
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    public String getCmd() {
        return cmd;
    }

    public void updateValues(String[] temp){
//        default set as download1RB
        radioGroupDownload.check(R.id.download1RB);
    }
}


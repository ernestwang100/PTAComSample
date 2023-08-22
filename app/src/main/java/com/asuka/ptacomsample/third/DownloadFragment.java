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

import java.text.SimpleDateFormat;

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
                    cmd = cmdStart + "0";
                    break;
                case R.id.download3RB:
                    showNumberPickerDialog();
                    break;

            }
        });

//        TODO: cmd
        cmdStart = "$LCD+DOWNLOAD=";

        return view;
    }

    private void showDatePickerDialog() {
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSelectedListener((year, month, day) -> {
            // Handle the selected date
            String selectedDate = String.format("%04d%02d%02d", year, month + 1, day);
            RadioButton download1RB = getView().findViewById(R.id.download1RB);
            String formattedDate = String.format("%04d/%02d/%02d", year, month + 1, day);
            download1RB.setText(formattedDate);

            cmd = cmdStart + selectedDate;
        });
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    private void showNumberPickerDialog() {
        NumberPickerFragment numberPickerFragment = new NumberPickerFragment(1, 24, 1, 8);
        numberPickerFragment.setOnNumberSelectedListener(new NumberPickerFragment.OnNumberSelectedListener() {
            @Override
            public void onNumberSelected(int numberPickerId, int selectedValue) {
                RadioButton download3RB = getView().findViewById(R.id.download3RB);
                download3RB.setText("最近" + selectedValue + "小時");

                cmd = cmdStart + selectedValue;
            }
        });
        numberPickerFragment.show(getFragmentManager(), "numberPicker");
    }


    public String getCmd() {
        return cmd;
    }

    public void updateValues(String[] temp) {
//        default set as download1RB
//        radioGroupDownload.check(R.id.download1RB);
    }
}


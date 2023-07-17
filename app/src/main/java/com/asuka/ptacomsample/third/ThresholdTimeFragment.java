package com.asuka.ptacomsample.third;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.RecvThread;

import java.util.Calendar;
import java.util.Locale;

public class ThresholdTimeFragment extends Fragment {
    private TextView drivetimeTV, resttimeTV;
    private String cmd, cmdStart;
    private static final String TAG = "ThresholdTimeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_threshold_time, container, false);

        drivetimeTV = view.findViewById(R.id.drivetimeTV);
        resttimeTV = view.findViewById(R.id.resttimeTV);

        drivetimeTV.setOnClickListener(v -> showTimePickerDialog((TextView) v));
        resttimeTV.setOnClickListener(v -> showTimePickerDialog((TextView) v));


//        cmdStart = "$LCD+SET DRIVE TIME=";
        cmdStart = "$LCD+SET THRESHOLD TIME=";

        return view;
    }

    private void showTimePickerDialog(TextView tv) {
        Calendar now = Calendar.getInstance();
        int hourOfDay = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, selectedHour, selectedMinute) -> onTimeSelected(tv, selectedHour, selectedMinute),
                hourOfDay,
                minute,
                true
        );

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.setMessage("Select Proper Time");
        timePickerDialog.setIcon(R.drawable.baseline_access_time_24);
        timePickerDialog.setCancelable(false);
        timePickerDialog.show();
    }

    private void onTimeSelected(TextView tv, int hourOfDay, int minute) {
        String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        tv.setText(time);
    }

    public String getCmd() {
        cmd = cmdStart + drivetimeTV.getText().toString().replace(':',',') + "," + resttimeTV.getText().toString().replace(':',',');
        return cmd;
    }

    public void updateValues(String[] temp){
        drivetimeTV.setText(addZero(temp[0]) + ":" + addZero(temp[1]));
        resttimeTV.setText(addZero(temp[2]) + ":" + addZero(temp[3]));
    }

    //    add 0 in front of single digit numbers
    public String addZero(String num) {
        if (num.length() == 1) {
            num = "0" + num;
        }
        return num;
    }
}

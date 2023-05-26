package com.asuka.ptacomsample.third;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

import java.util.Calendar;
import java.util.Locale;

public class ThresholdTimeFragment extends Fragment {
    private TextView drivertimeTV, codrivertimeTV;
    private String cmd, cmdStart;
    private int thresholdType;

    public ThresholdTimeFragment(int thresholdType) {
        this.thresholdType = thresholdType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_threshold_time, container, false);

        drivertimeTV = view.findViewById(R.id.drivertimeTV);
        codrivertimeTV = view.findViewById(R.id.codrivertimeTV);

        drivertimeTV.setOnClickListener(v -> showTimePickerDialog((TextView) v));
        codrivertimeTV.setOnClickListener(v -> showTimePickerDialog((TextView) v));

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

        if (thresholdType == 0){
            cmdStart = "$LCD+SET DRIVE TIME=";
        } else {
            cmdStart = "$LCD+SET REST TIME=";
        }
        cmd = cmdStart + "0," + drivertimeTV.getText().toString() + "\n" +
                cmdStart +"1," + codrivertimeTV.getText().toString();
        return cmd;
    }
}

package com.asuka.ptacomsample.third;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

import java.util.Calendar;
import java.util.Locale;

public class SystemTimeFragment extends Fragment {
    private TextView systemDateTV, systemTimeTV;
    private String cmd, cmdStart;
    private DatePickerFragment datePickerFragment;
    private String selectedDate, selectedTime, cmdDate, cmdTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system_time, container, false);

        systemDateTV = view.findViewById(R.id.systemDateTV);
        systemTimeTV = view.findViewById(R.id.systemTimeTV);

        systemDateTV.setOnClickListener(v -> showDatePickerDialog());
        systemTimeTV.setOnClickListener(v -> showTimePickerDialog((TextView) v));

        return view;
    }

    private void showDatePickerDialog() {
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSelectedListener((year, month, day) -> {
            // Handle the selected date
            selectedDate = String.format("%04d/%02d/%02d", year, month + 1, day);
            cmdDate = String.format("%04d%02d%02d", year, month + 1, day);
            systemDateTV.setText(selectedDate);
        });
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog(TextView tv) {
        Calendar now = Calendar.getInstance();
        int hourOfDay = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

//       No seconds in the time picker dialog
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
        selectedTime = String.format(Locale.getDefault(), "%02d%02d", hourOfDay, minute);
        cmdTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        tv.setText(selectedTime);
    }

    public String getCmd() {
        cmdStart = "$LCD+TIME ADJ=";
        cmd = cmdStart + cmdDate + cmdTime + "00\n";
        return cmd;
    }
}

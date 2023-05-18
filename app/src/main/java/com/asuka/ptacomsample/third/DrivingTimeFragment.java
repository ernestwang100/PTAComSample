package com.asuka.ptacomsample.third;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

import java.util.Calendar;
import java.util.Locale;

public class DrivingTimeFragment extends Fragment {
    private TextView drivertimeTV, codrivertimeTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driving_time, container, false);

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
}

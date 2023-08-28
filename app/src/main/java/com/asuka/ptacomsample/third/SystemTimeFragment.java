package com.asuka.ptacomsample.third;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.RecvThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SystemTimeFragment extends Fragment {
    private TextView systemDateTV, systemTimeTV;
    private String cmd, cmdStart, cmdEnd, selectedDate, selectedTime, cmdDate, cmdTime, temp[];
    private DatePickerFragment datePickerFragment;
    private boolean isDateSelected = false;
    private boolean isTimeSelected = false;
    private static final String TAG = "SystemTimeFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system_time, container, false);
        cmdStart = "$LCD+TIME ADJ=";
        cmdEnd = "\r\n";

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
            systemDateTV.setText(selectedDate);
            cmdDate = selectedDate.replace("/", "").trim();
            cmdDate = cmdDate.substring(2);
        });
        datePickerFragment.show(getFragmentManager(), "datePicker");
        isDateSelected = true;
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
        isTimeSelected = true;
    }

    private void onTimeSelected(TextView tv, int hourOfDay, int minute) {
        selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        tv.setText(selectedTime);
        cmdTime = selectedTime.replace(":", "").trim() + "00";
    }

    public String getCmd() {
        cmd = cmdStart + cmdDate + cmdTime + cmdEnd;
        return cmd;
    }

    public void updateValues(String[] temp) {
        if (temp != null && temp.length > 0) {
            Log.d(TAG, "updateValues: temp[0]: " + temp[0]);
            String dateTimeString = temp[0];
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyMMddHHmmss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            try {
                Date dateTime = inputFormat.parse(dateTimeString);
                String formattedDate = dateFormat.format(dateTime); // "2023/06/08"
                String formattedTime = timeFormat.format(dateTime); // "10:11:12"
                Log.d(TAG, "handleMessage: dateTime: " + dateTime);
                Log.d(TAG, "handleMessage: formattedDate, formattedTime: " + formattedDate + ", " + formattedTime);

                // Set the formatted date and time to the respective TextViews
                systemDateTV.setText(formattedDate);
                systemTimeTV.setText(formattedTime);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(TAG, "handleMessage: ParseException: " + e.getMessage());
            }
        }
    }
}


package com.asuka.ptacomsample.third;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private OnTimeSelectedListener onTimeSelectedListener;

    public interface OnTimeSelectedListener {
        void onTimeSelected(int hourOfDay, int minute);
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener listener) {
        this.onTimeSelectedListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (onTimeSelectedListener != null) {
            onTimeSelectedListener.onTimeSelected(hourOfDay, minute);
        }
    }
}

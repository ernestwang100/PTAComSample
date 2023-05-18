package com.asuka.ptacomsample.third;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

import java.util.Calendar;
import java.util.Locale;

public class Data24hFragment extends Fragment {
    private NumberPicker numberPicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data24h, container, false);

        numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(24);
        numberPicker.setMinValue(1);
        int hour = numberPicker.getValue();



        return view;
    }
}

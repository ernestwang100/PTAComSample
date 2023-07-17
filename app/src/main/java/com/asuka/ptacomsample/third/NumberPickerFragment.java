package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

import com.asuka.ptacomsample.R;

public class NumberPickerFragment extends DialogFragment {
    private int numberPickerId, maxValue, minValue, value;
    private OnNumberSelectedListener onNumberSelectedListener;

    public NumberPickerFragment() {
        // Required empty public constructor
    }

    public NumberPickerFragment(int numberPickerId, int maxValue, int minValue, int value) {
        this.numberPickerId = numberPickerId;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.value = value;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number_picker, container, false);

        NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
        Button confirmButton = view.findViewById(R.id.confirmButton);


        numberPicker.setId(numberPickerId);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setMinValue(minValue);
        numberPicker.setValue(value);

        confirmButton.setOnClickListener(v -> {
            if (onNumberSelectedListener != null) {
                int selectedValue = numberPicker.getValue();
                onNumberSelectedListener.onNumberSelected(numberPickerId, selectedValue);
            }
            dismiss();
        });

        return view;
    }

    public interface OnNumberSelectedListener {
        void onNumberSelected(int numberPickerId, int selectedValue);
    }

    public void setOnNumberSelectedListener(OnNumberSelectedListener listener) {
        onNumberSelectedListener = listener;
    }
}
